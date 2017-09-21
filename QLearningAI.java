import java.io.*;
import java.util.*;
public class QLearningAI extends AI
{
	private double[] value = new double[65];
	private	int[] steps = new int[65];
	private NeuralNetwork dqn;
	private HashMap<SA, double> Q;
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
	public void Q_learning()
	{
		int action;
		LinkedList<TR> D = new LinkedList<TR>();
		HashMap<S, S> theta = new HashMap<S, S>();
		double reward, temp, y;
		Table t = new Table();
		int[] now, next;
		S s1, s2;
		
		Q = new HashMap<SA, double>();
		for (int episode = 1; episode <= 300; episode++)
		{
			s1 = new S(t.getTable());
			theta.put(state, state);
			now = s1.s;
			temp = evaluate(now);
			for (int t = 1; t <= 100; t++)
			{
				next = Table.trans(now, action, hold);
				s2 = new Table(next);
				reward = evaluate(next) - temp;
				
				if (terminal())
					y = reward;
				else
					y = reward + 0.5 * maxq(now);
			}			
		}
	}
}