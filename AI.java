public class AI extends Player
{
	public Table table;
	AI(int h)
	{
		super(h);
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
			//System.out.printf("%d\n", steps[0]);
			table.set((step - 1) / 8 + 1, (step - 1) % 8 + 1, hold);			
			return step;
		}
		return -1;
	}
}