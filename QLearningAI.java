import java.io.*;
import java.util.*;
public class QLearningAI extends AI
{
	class Transition
	{
		public Table t1, t2;
		public int action, reward;
		public Transition()
		{
			t1 = new Table();
			t2 = new Table();
			action = 0;
			reward = 0;
		}
		public set(Table tt1, int a, int r, Table tt2)
		{
			t1 = tt1;
			t2 = tt2;
			action = a;
			reward = r;
		}
	}
	private double[] value = new double[65];
	private	int[] steps = new int[65];
	private file dir, f;
	public QLearningAI(int h)
	{
		super(h);
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
		for (int i = 0; i <= 64; i++)
		{
			if (x[i] == hold)
				sum += value[i] * (32.0 / (double)(pt.hand)) + 0.32 / (double)(65.0 - pt.hand);
			else if (x[i] == -hold)
				sum -= value[i] * (32.0 / (double)(pt.hand)) + 0.32 / (double)(65.0 - pt.hand);
		}
		return sum;		
	}
	private void Q_learning()
	{
		int step;
		Table pre = new Table();
		Table s = new Table();
		for (int episode = 1; episode <= 300; episode++)
		{
			for (int t = 1; t <= 100; t++)
			{
				
			}			
		}
	}
}