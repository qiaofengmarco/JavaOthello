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
	private double epsilon = 0.9, gamma = 0.99;
	private int maxSize = 1000, minibatchSize = 0;
	public QLearningAI(int h)
	{
		super(h);
		for (int i = 1; i <= 64; i++)
			value[i] = 0;
		value[1] = 0.9;
		value[2] = -0.6;
		value[3] = 0.1;
		value[4] = 0.1;
		value[5] = 0.1;
		value[6] = 0.1;
		value[7] = -0.6;
		value[8] = 0.9;
		value[9] = -0.6;
		value[10] = -0.8;
		value[11] = 0.05;
		value[12] = 0.05;
		value[13] = 0.05;
		value[14] = 0.05;
		value[15] = -0.8;
		value[16] = -0.6;
		value[17] = 0.1;
		value[18] = 0.05;
		value[19] = 0.01;
		value[20] = 0.01;
		value[21] = 0.01;
		value[22] = 0.01;
		value[23] = 0.05;
		value[24] = 0.1;
		value[25] = 0.1;
		value[26] = 0.05;
		value[27] = 0.01;
		value[28] = 0.01;
		value[29] = 0.01;
		value[30] = 0.01;
		value[31] = 0.05;
		value[32] = 0.1;
		value[33] = 0.1;
		value[34] = 0.05;
		value[35] = 0.01;
		value[36] = 0.01;
		value[37] = 0.01;
		value[38] = 0.01;
		value[39] = 0.05;
		value[40] = 0.1;
		value[41] = 0.1;
		value[42] = 0.05;
		value[43] = 0.01;
		value[44] = 0.01;
		value[45] = 0.01;
		value[46] = 0.01;
		value[47] = 0.05;
		value[48] = 0.1;
		value[49] = -0.6;
		value[50] = -0.8;
		value[51] = -0.05;
		value[52] = -0.05;
		value[53] = -0.05;
		value[54] = -0.05;
		value[55] = -0.8;
		value[56] = -0.6;
		value[57] = 0.9;
		value[58] = -0.6;
		value[59] = 0.1;
		value[60] = 0.1;
		value[61] = 0.1;
		value[62] = 0.1;
		value[63] = -0.6;
		value[64] = 0.9;		
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
		for (int i = 1; i <= 8; i++)
			for (int j = 1; j <= 8; j++)
			{
				if (table.bigTable[i][j] != 0) continue;
				if (table.checkStep(i, j, hold))
					sum += 0.01;
				if (table.checkStep(i, j, -hold))
					sum -= 0.01;
				sum += value[(i - 1) * 8 + j] * table.bigTable[i][j] * hold;
			}
		return sum;
	}
	public int[][] epsilon_greedy_move(int[] now)
	{
		int action;
		int[] next, s1;
		int[][] out = new int[2][64];
		double p = Math.random(), max = -100000, temp, reward = 0, reward_temp = 0, now_value = 0;
		int[] steps;
		UCTAI opponent;
		Integer[] st = Arrays.stream(now).boxed().toArray(Integer[]::new);
		Integer a;
		Double d;
		boolean flag = false;
		
		//if AI cannot move now, then jump to next moveable state
		now_value = evaluate(now);
		steps = Board.searchNext(now, hold);
		s1 = now;
		while (steps[0] == 0)
		{
			opponent = new UCTAI(s1, -hold);
			opponent.move();
			s1 = opponent.table.getTable();
			steps = Board.searchNext(s1, hold);
			if (Board.terminal(s1)) 
			{
				flag = true; //AI is not moveable and the game is finish
				break;
			}
		}
		if (!flag) //AI can move originally or AI can move afterwards
		{
			reward_temp = evaluate(s1) - now_value;
			now = s1;
			now_value = evaluate(now);
		}
		out[0] = now;
		out[1] = now;
		st = Arrays.stream(now).boxed().toArray(Integer[]::new);
		
		//e-greedy policy
		if (steps[0] > 0) //AI is moveable
		{
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
			opponent = new UCTAI(next, -hold);
			int opmove = opponent.move();
			if (opmove >= 0) //opponent is moveable
			{
				next = opponent.table.getTable();
				reward = evaluate(next) - now_value + reward_temp;
			}
			else //opponent is not moveable
			{
				reward = now_value + reward_temp;
			}
			out[1] = next;
			
			//store transition in memory D
			Transition tr = new Transition(now, next, action, reward);
			D.offer(tr);
			if (D.size() > maxSize)
				D.pollFirst();
		}
		else
		{
			out[1] = now;
		}

		
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
		Transition[] ans = new Transition[32];
		int count = 0, size = D.size(), temp;
		if (size <= 32)
		{
			for (int i = 0; i < size; i++)
				ans[i] = D.get(i);
			minibatchSize = size;
			return ans;
		}
		HashSet<Integer> set = new HashSet<Integer>();
		while (set.size() < 32)
		{
			temp = (int)(Math.random() * size);
			set.add(new Integer(temp));
		}
		for (Integer i : set)
		{
			ans[count] = D.get(i.intValue());
			count++;
		}
		minibatchSize = 32;
		return ans;
	}
	public void qLearning()
	{
		double reward, temp;
		Double d;
		Integer a;
		Integer[] st;
		double[] y = new double[32];
		int[] now, next;
		int[][] out = new int[2][];
		int[][] input = new int[32][64];
		int[] act = new int[32];
		Transition[] minibatch = new Transition[32];
		
		if (hold == 1)
			dqn = new NeuralNetwork("black.txt");
		else if (hold == -1)
			dqn = new NeuralNetwork("white.txt");
		
		for (int episode = 1; episode <= 200; episode++)
		{
			now = Board.getInit();
			System.out.printf("episode: %d\n", episode);
			for (int T = 1; T <= 200; T++)
			{
				//Act e-greedy policy and store the transition
				//Then set now = next
				out = epsilon_greedy_move(now); 
				now = out[0];
				next = out[1];
				if ((Board.terminal(next)) || (now == next))
					now = Board.getInit();
				else 
					now = next;
				
				
				//update
				if (D.size() > 0)
				{
					minibatch = sampling(); //sampling minibatch
					
					for (int i = 0; i < minibatchSize; i++)
					{					
						st = Arrays.stream(minibatch[i].s1).boxed().toArray(Integer[]::new);
						a = new Integer(minibatch[i].action);
						input[i] = minibatch[i].s1;
						act[i] = minibatch[i].action;
												
						// yi = ri                          for terminal s_t+1
						//    = ri + gamma * max Q(s', a')  for non-terminal s_t+1
						if (Board.terminal(minibatch[i].s2))
							y[i] = minibatch[i].reward;
						else
							y[i] = minibatch[i].reward + gamma * maxQ(minibatch[i].s2);
					}
					
					//gradient descent
					dqn.backward(input, act, y, minibatchSize);
				}
			}
			if ((episode % 50 == 0) && (episode > 0)) 
				dqn.store();
		}
	}
	@Override
	public int move()
	{
		int[] steps = new int[65];
		int step = -1;
		double max = -100000, temp;
		Integer a;
		Double d;
		double p = Math.random();
		int[] now = table.getTable();
		Integer[] st = Arrays.stream(now).boxed().toArray(Integer[]::new);
		steps = table.nextSteps(hold);
		if (steps[0] > 0)
		{
			if (steps[0] == 1)
				step = steps[1];
			else
			{
				if (p > epsilon)
					step = steps[(int)(Math.random()*steps[0]) + 1];
				else
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
								step = steps[i];
							}
							else
							{
								temp = dqn.forward(now, steps[i]);
								d = new Double(temp);
								Q.put(st, a, d);
								if (temp > max)
								{
									max = temp;
									step = steps[i];
								}
							}
						}			
					}
				}
			}
			table.set(step / 8 + 1, step % 8 + 1, hold);			
			return step;
		}
		return -1;		
	}
	public static void main(String[] args)
	{
		int kk = (int)(Math.random() * 2);
		kk = (int)Math.pow(-1, kk);
		/*if (kk == 1)
			System.out.println("Training black...");
		else
			System.out.println("Training white...");*/
		System.out.println("Training black...");
		QLearningAI ai = new QLearningAI(1);
		ai.qLearning();
		System.out.println("Finish");
	}
}