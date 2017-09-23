public class AlphaBetaAI extends AI
{
	private double[] value = new double[65];
	public AlphaBetaAI(int h)
	{
		super(h);
		for (int i = 1; i <= 64; i++)
			value[i] = 0;
		value[1] = 500;
		value[8] = 500;
		value[57] = 500;
		value[64] = 500;
		value[2] = -10;
		value[7] = -10;
		value[9] = -10;
		value[10] = -10;
		value[15] = -10;
		value[16] = -10;
		value[49] = -10;
		value[50] = -10;
		value[58] = -10;
		value[55] = -10;
		value[56] = -10;
		value[63] = -10;
	}
	public AlphaBetaAI(int x[], int h)
	{
		super(x, h);
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
	private double AlphaBeta(int s, int[] pt, int depth, double a, double b, int h)
	{
		int[] moves = new int[65];
		pt = Board.nextState(pt, s, h);
		moves = Board.searchNext(pt, h);
		if ((depth == 0) || (moves[0] == 0))
		{
			double sum = 0;
			int hands = Board.calcHand(pt);
			for (int i = 0; i < 64; i++)
			{
				if (pt[i] == hold)
					sum += value[i] + 32 / (double)(65.0 - hands);
				else if (pt[i] == -hold)
					sum -= value[i] + 32 / (double)(65.0 - hands);
			}
			if (Board.terminal(pt)) sum *= 100;
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
		return 0.0;
	}
	@Override
	public int move()
	{
		int[] steps = new int[65];
		int step = 1;
		double max = -100000, temp;
		int[] s = new int[64];
		int depth = 3;
		int hands = Board.calcHand(table.getTable());
		steps = table.nextSteps(hold);
		if (steps[0] > 0)
		{
			if (steps[0] == 1)
				step = steps[1];
			else
			{
				for (int i = 1; i <= 8; i++)
					for (int j = 1; j <= 8; j++)
						s[(i - 1) * 8 + j - 1] = table.bigTable[i][j];
				for (int i = 1; i <= steps[0]; i++)
				{
					if (hands <= 10)
						depth = 5;
					else if (hands <= 58)
						depth = 4;
					else
						depth = 3;
					temp = AlphaBeta(steps[i], s, depth, -100000, 100000, hold);
					if (temp > max)
					{
						max = temp;
						step = steps[i];
					}
				}
			}
			table.set(step / 8 + 1, step % 8 + 1, hold);	
			return step;
		}
		return -1;
	}
}