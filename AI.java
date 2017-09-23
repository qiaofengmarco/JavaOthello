public class AI
{
	public Board table;
	public int hold;
	public AI(int h)
	{
		hold = h;
		table = new Board();
	}
	public AI(int[] x, int h)
	{
		table = new Board(x);
		hold = h;
	}
	public void setBoard(int[] x)
	{
		table = new Board(x);
	}
	public int move()
	{
		int[] steps = new int[65];
		int step;
		steps = table.nextSteps(hold);
		if (steps[0] > 0)
		{
			if (steps[0] == 1)
				step = steps[1];
			else
				step = steps[(int)(Math.random() * steps[0]) + 1];
			table.set(step / 8 + 1, step % 8 + 1, hold);			
			return step;
		}
		return -1;
	}
}