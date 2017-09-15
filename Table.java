public class Table
{
	public int[][] bigTable;
	public int bhand = 2, whand = 2, hand = 4;
	private int[] MoveX = {0,  0, 0, -1,  1, -1, 1, -1, 1};
	private int[] MoveY = {0, -1, 1, -1, -1,  0, 0,  1, 1};
	Table()
	{
		bigTable = new int[9][];
		for (int i = 0; i <= 8; i++)
		{
			bigTable[i] = new int[9];
			for (int j = 1; j <= 8; j++)
				bigTable[i][j] = 0;
		}	
		bigTable[4][4] = -1;
		bigTable[4][5] = 1;
		bigTable[5][4] = 1;
		bigTable[5][5] = -1;
	}
	public int getHand(int a)
	{
		if (a == 1)
			return bhand;
		else if (a == -1) 
			return whand;
		return 0;
	}
	public void set(int x, int y, int who)
	{
		int x1, y1, count = 0;
		for (int i = 1; i <= 8; i++)
		{
			count = 0; 
			x1 = x + MoveX[i];
			y1 = y + MoveY[i];
			count++;
			if ((x1 < 1) || (x1 > 8) || (y1 < 1) || (y1 > 8)) continue;
			if (bigTable[x1][y1] != -who) continue;
			while (bigTable[x1][y1] == -who)
			{
				x1 += MoveX[i];
				y1 += MoveY[i];
				count++;
				if ((x1 < 1) || (x1 > 8) || (y1 < 1) || (y1 > 8)) break; 
				if (bigTable[x1][y1] == 0) break;
				if (bigTable[x1][y1] == who)
				{
					for (int j = 1; j <= count - 1; j++)
					{
						bigTable[x + j * MoveX[i]][y + j * MoveY[i]] = who;
						//System.out.printf("%d, %d; %d, %d\n", x, y, x + j * MoveX[i], y + j * MoveY[i]);
					}
					break;
				}
			}
		}
		bigTable[x][y] = who;
		hand++;
		if (who == 1)
			bhand++;
		else if (who == -1)
			whand++;
	}
	public boolean checkStep(int x, int y, int who)
	{
		int x1, y1;
		if ((x < 1) || (x > 8) || (y < 1) || (y > 8)) return false;
		if (bigTable[x][y] != 0) return false;
		for (int i = 1; i <= 8; i++)
		{
			x1 = x + MoveX[i];
			y1 = y + MoveY[i];
			if ((x1 < 1) || (x1 > 8) || (y1 < 1) || (y1 > 8)) continue;
			if (bigTable[x1][y1] != -who) continue;
			while (bigTable[x1][y1] == -who)
			{
				x1 += MoveX[i];
				y1 += MoveY[i];
				if (((x1 < 1) || (x1 > 8) || (y1 < 1) || (y1 > 8))) break;
				if (bigTable[x1][y1] == 0) break;
				if (bigTable[x1][y1] == who) 
				{
					return true;
				}
			}			
		}
		return false;
	}
	public int[] nextSteps(int who)
	{
		int x1, y1;
		int[] ans = new int[65];
		for (int i = 0; i < 65; i++)
			ans[i] = 0;
		for (int i = 1; i <= 8; i++)
			for (int j = 1; j <= 8; j++)
				if (checkStep(i, j, who))
				{
					ans[0]++;
					ans[ans[0]] = (i - 1) * 8 + j;
				}
		return ans;		
	}
}