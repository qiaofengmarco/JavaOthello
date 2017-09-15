public class AlphaBetaAI extends AI
{
	private int[] value = new int[65];
	public AlphaBetaAI(int h)
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
	private double min(double a, double b)
	{
		if (a <= b)
			return a;
		return b;
	}
	private double AlphaBeta(int s, Table pt, int depth, double a, double b, int h)
	{
		int[] moves = new int[65];
		pt.set((s - 1) / 8 + 1, (s - 1) % 8 + 1, h);
		moves = pt.nextSteps(h);
		if ((depth == 0) || (moves[0] == 0))
		{
			double sum = 0;
			for (int i = 1; i <= 8; i++)
				for (int j = 1; j <= 8; j++)
				{
					if (pt.bigTable[i][j] == hold)
						sum += value[(i - 1) * 8 + j] * (32.0 / (double)(pt.hand)) + 32.0 / (double)(65.0 - pt.hand);
					else if (table.bigTable[i][j] == -hold)
						sum -= value[(i - 1) * 8 + j] * (32.0 / (double)(pt.hand)) + 32.0 / (double)(65.0 - pt.hand);
				}
			return sum;
		}
		if (h == hold)
		{
			for (int i = 1; i <= moves[0]; i++)
			{
				a = max(a, AlphaBeta(moves[i], pt, depth - 1, a, b, -h));
				if (b <= a)
					break;
				return a;
			}
		}
		else 
		{
			for (int i = 1; i <= moves[0]; i++)
			{
				b = min(b, AlphaBeta(moves[i], pt, depth - 1, a, b, -h));
				if (b <= a)
					break;
				return b;
			}			
		}
		return 0;
	}
	@Override
	public int move()
	{
		int[] steps = new int[65];
		int step = 1;
		double max = -100000, temp;
		steps = table.nextSteps(hold);
		if (steps[0] > 0)
		{
			if (steps[0] == 1)
				step = steps[1];
			else
			{
				for (int i = 1; i <= steps[0]; i++)
				{
					temp = AlphaBeta(steps[i], table, 4, -100000, 100000, hold);
					if (temp > max)
					{
						max = temp;
						step = steps[i];
					}
				}
			}
			table.set((step - 1) / 8 + 1, (step - 1) % 8 + 1, hold);			
			return step;
		}
		return -1;
	}
}