import java.io.*;
import java.util.*;
import com.google.common.collect.*;
public class QLearningAI extends AI
{
	private double[] value = new double[65];
	private	int[] steps = new int[65];
	private NeuralNetwork dqn;
	private Table<Integer[], Integer, Double> Q;
	private double epsilon = 0.65;
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
		for (int i = 0; i <= 64; i++)
			if (x[i] != 0)
				hand++;
		for (int i = 0; i <= 64; i++)
		{
			if (x[i] == hold)
				sum += (value[i] * (32.0 / (double)(hand)) + 0.32 / (double)(65.0 - hand)) * 0.1;
			else if (x[i] == -hold)
				sum -= (value[i] * (32.0 / (double)(hand)) + 0.32 / (double)(65.0 - hand)) * 0.1;
		}
		return Math.tanh(sum);
	}
	public int[] epsilon_greedy(int[] now, int hold)
	{
		int action;
		Random r = new Random();
		double p = r.nextDouble();
		if (p <= epsilon) //do argmax Q(s, a)
		{
		}
		else //act randomly
		{
			int[] steps;
			steps = Board.searchNext(now, hold);
		}
	}
	public void qLearning()
	{
		int action;
		double reward, temp, y;
		int[] now, next;
		Board board = new Board();
		Q = HashBasedTable.create();
		Map<Integer[], Integer[]> theta = new HashMap<Integer[], Integer[]>();
		for (int episode = 1; episode <= 300; episode++)
		{
			s1 = new S(board.getTable());
			now = s1;
			for (int t = 1; t <= 100; t++)
			{
				
				reward = evaluate(next) - temp;
				
				if (terminal())
					y = reward;
				else
					y = reward + 0.5 * maxq(now);
			}			
		}
	}
}