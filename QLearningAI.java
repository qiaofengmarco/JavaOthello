import java.io.*;
import java.util.*;
import com.google.common.collect.*;
public class QLearningAI extends AI
{
	private double[] value = new double[65];
	private	int[] steps = new int[65];
	private NeuralNetwork dqn;
	private HashBasedTable<Integer[], Integer, Double> Q = HashBasedTable.create();
	private LinkedList<Transition> D = new LinkedList<Transition>();
	private double epsilon = 0.9, gamma = 0.5;
	public QLearningAI(int h)
	{
		super(h);
		if (h == 1)
			dqn = new NeuralNetwork("black.txt");
		else if (h == -1)
			dqn = new NeuralNetwork("white.txt");
		for (int i = 1; i <= 64; i++)
			value[i] = 0;
		value[1] = 1;
		value[8] = 1;
		value[57] = 1;
		value[64] = 1;
		value[2] = -0.25;
		value[7] = -0.25;
		value[9] = -0.25;
		value[10] = -0.25;
		value[15] = -0.25;
		value[16] = -0.25;
		value[49] = -0.25;
		value[50] = -0.25;
		value[58] = -0.25;
		value[55] = -0.25;
		value[56] = -0.25;
		value[63] = -0.25;		
	}
	private double max(double a, double b)
	{
		if (a >= b)
			return a;
		return b;
	}
	private double evaluate(int[] x)
	{
		double sum = 0;
		int hand = 0;
		for (int i = 0; i < 64; i++)
			if (x[i] != 0)
				hand++;
		for (int i = 0; i < 64; i++)
		{
			if (x[i] == hold)
				sum += (value[i] * (32.0 / (double)(hand)) + 0.32 / (double)(65.0 - hand)) * 0.1;
			else if (x[i] == -hold)
				sum -= (value[i] * (32.0 / (double)(hand)) + 0.32 / (double)(65.0 - hand)) * 0.1;
		}
		return Math.tanh(sum);
	}
	public int[][] epsilon_greedy_move(int[] now)
	{
		int action;
		int[] next;
		int[][] out = new int[2][64];
		Random r = new Random();
		double p = r.nextDouble(), max = -100000, temp, reward = 0;
		int[] steps;
		AlphaBetaAI opponent;
		Integer[] st = Arrays.stream(now).boxed().toArray(Integer[]::new);
		Integer a;
		Double d;
		
		//if AI cannot move now, then jump to next moveable state
		steps = Board.searchNext(now, hold);
		while (steps[0] == 0)
		{
			opponent = new AlphaBetaAI(now, -hold);
			opponent.move();
			now = opponent.table.getTable();
			steps = Board.searchNext(now, hold);
			st = Arrays.stream(now).boxed().toArray(Integer[]::new);
		}
		out[0] = now;
		
		//e-greedy policy
		action = steps[1];
		if (p <= epsilon) //do argmax Q(s, a)
		{
			for (int i = 1; i <= steps[0]; i++)
			{
				a = new Integer(steps[i]);
				if (Q.contains(st, a))
				{
					temp = Q.get(st, a).doubleValue();
					if (temp > max)
					{
						max = temp;
						action = steps[i];
					}
				}
				else
				{
					temp = dqn.forward(now, steps[i]);
					d = new Double(temp);
					Q.put(st, a, d);
					if (temp > max)
					{
						max = temp;
						action = steps[i];
					}
				}				
			}
		}
		else //act randomly
			action = steps[(int)(Math.random() * steps[0]) + 1];
		next = Board.nextState(now, action, hold);
		
		//opponent move
		opponent = new AlphaBetaAI(next, -hold);
		int opmove = opponent.move();
		next = opponent.table.getTable();
		out[1] = next;
		if (opmove >= 0)
			reward = evaluate(now) - evaluate(next);
		else
			reward = evaluate(now);
		
		//store transition in memory D
		Transition tr = new Transition(now, next, action, reward);
		D.offer(tr);
		if (D.size() > 32)
			D.pollFirst();
		
		return out;
	}
	public double maxQ(int[] now)
	//max Q(s', a')
	{
		double max = -100000, temp;
		Integer[] st = Arrays.stream(now).boxed().toArray(Integer[]::new);
		Integer a;
		Double d;
		int[] nextStep = Board.searchNext(now, hold);
		if (nextStep[0] == 0) return 0.0;
		for (int i = 1; i <= nextStep[0]; i++)
		{
			a = new Integer(nextStep[i]);
			if (Q.contains(st, a))
			{
				temp = Q.get(st, a).doubleValue();
				if (temp > max)
					max = temp;
			}
			else
			{
				temp = dqn.forward(now, nextStep[i]);
				d = new Double(temp);
				Q.put(st, a, d);
				if (temp > max)
					max = temp;
			}
		}
		return max;
	}
	public Transition[] sampling()
	{
		Transition[] ans = new Transition[8];
		int count = 0;
		int m = (int)(Math.random() * 4);
		for (int i = 0; i < 32; i++)
			if (i % 4 == m)
			{
				ans[count] = D.get(i);
				count++;
			}
		return ans;
	}
	public void qLearning()
	{
		double reward, temp;
		Double d;
		double[] y = new double[8];
		int[] now, next;
		int[][] out = new int[2][];
		Transition[] minibatch = new Transition[8];
		
		for (int episode = 1; episode <= 200; episode++)
		{
			now = Board.getInit();
			System.out.printf("episode: %d\n", episode);
			for (int T = 1; T <= 200; T++)
			{
				out = epsilon_greedy_move(now); //e-greedy and store the transition
				now = out[0];
				next = out[1];
				
				if (D.size() == 32)
				{
					minibatch = sampling(); //sampling minibatch
					
					for (int i = 0; i < 8; i++)
					{						
						Integer[] st = Arrays.stream(minibatch[i].s1).boxed().toArray(Integer[]::new);				
						Integer a = new Integer(minibatch[i].action);
												
						// yi = ri                          for terminal s_t+1
						//    = ri + gamma * max Q(s', a')  for non-terminal s_t+1
						if (Board.terminal(minibatch[i].s2))
							y[i] = minibatch[i].reward;
						else
							y[i] = minibatch[i].reward + gamma * maxQ(minibatch[i].s2);
						
						//gradient descent
						//only doing backward update will change the weights of dqn
						//update new Q(s, a) at the same time
						temp = dqn.backward(minibatch[i].s1, minibatch[i].action, y[i]);
						d = new Double(temp);
						Q.put(st, a, d);

					}
				
				}
				
				if (Board.terminal(next))
					now = Board.getInit();
				else 
					now = next;
			}			
		}
	}
	public static void main(String[] args)
	{
		int kk = (int)(Math.random() * 2);
		kk = (int)Math.pow(-1, kk);
		if (kk == 1)
			System.out.println("Training black...");
		else
			System.out.println("Training white...");
		QLearningAI ai = new QLearningAI(kk);
		ai.qLearning();
		System.out.println("Finish");
	}
}