import java.io.*;
public class QLearningAI extends AI
{
	private int[] value = new int[65];
	private file dir, f;
	public QLearningAI(int h)
	{
		super(h);
		for (int i = 1; i <= 64; i++)
			value[i] = 0;
		value[1] = 100;
		value[8] = 100;
		value[57] = 100;
		value[64] = 100;
		value[2] = -25;
		value[7] = -25;
		value[9] = -25;
		value[10] = -25;
		value[15] = -25;
		value[16] = -25;
		value[49] = -25;
		value[50] = -25;
		value[58] = -25;
		value[55] = -25;
		value[56] = -25;
		value[63] = -25;		
	}
	private double max(double a, double b)
	{
		if (a >= b)
			return a;
		return b;
	}
	private double[] QTable()
	{
		dir = new File(System.getProperty("user.dir").toString());
		f = new File(dir, "QTable.txt");
		if (!f.exists())
		{
			try
			{
				f.createNewFile();
				
			}
		}
	}
}