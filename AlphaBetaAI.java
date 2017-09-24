public class AlphaBetaAI extends AI
{
	private double[] value = new double[65];
	public AlphaBetaAI(int h)
	{
		super(h);
		for (int i = 1; i <= 64; i++)
			value[i] = 0;
		value[1] = 100;
		value[8] = 100;
		value[57] = 100;
		value[64] = 100;
		value[2] = -1;
		value[7] = -1;
		value[9] = -1;
		value[10] = -1;
		value[15] = -1;
		value[16] = -1;
		value[49] = -1;
		value[50] = -1;
		value[58] = -1;
		value[55] = -1;
		value[56] = -1;
		value[63] = -1;
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
					sum += value[i] + Math.pow(1.2, Math.max(35, hands) - 35) * 0.09;
				else if (pt[i] == -hold)
					sum -= value[i] + Math.pow(1.2, Math.max(35, hands) - 35) * 0.09;
			}
			if (Board.terminal(pt)) sum *= 50;
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
		double max = -2147483647, temp;
		int[] s = new int[64];
		int depth = 4;
		int hands = Board.calcHand(table.getTable());
		steps = table.nextSteps(hold);
		int[] nexts = new int[65];
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
					temp = AlphaBeta(steps[i], s, depth, -2147483647, 2147483647, hold);
					if (temp > max)
					{
						max = temp;
						nexts[0] = 1;
						nexts[1] = steps[i];
					}
					else if (temp == max)
					{
						nexts[0]++;
						nexts[nexts[0]] = steps[i];
					}
				}
				if (nexts[0] == 1)
					step = nexts[1];
				else
					step = nexts[(int)(Math.random() * nexts[0]) + 1];
			}
			table.set(step / 8 + 1, step % 8 + 1, hold);	
			return step;
		}
		return -1;
	}
}