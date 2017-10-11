package Game;
public class Board
{
	public int[][] bigTable;
	public int bhand = 2, whand = 2, hand = 4;
	private int[] MoveX = {0,  0, 0, -1,  1, -1, 1, -1, 1};
	private int[] MoveY = {0, -1, 1, -1, -1,  0, 0,  1, 1};
	public Board()
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
	public Board(int[] s)
	{
		bigTable = new int[9][9];
		for (int i = 1; i <= 8; i++)
			for (int j = 1; j <= 8; j++)
			{
				bigTable[i][j] = s[(i - 1) * 8 + j - 1];
				if (bigTable[i][j] == 1)
				{
					bhand++;
					hand++;
				}
				else if (bigTable[i][j] == -1)
				{
					whand++;
					hand++;
				}
			}
	}
	public Board(Board b)
	{
		bigTable = new int[9][9];
		for (int i = 1; i <= 8; i++)
			for (int j = 1; j <= 8; j++)
				bigTable[i][j] = b.bigTable[i][j];
		hand = b.hand;
		bhand = b.bhand;
		whand = b.whand;
	}
	public static int[] getInit()
	{
		int[] ans = new int[64];
		for (int i = 0; i < 64; i++)
			ans[i] = 0;
		ans[27] = -1;
		ans[28] = 1;
		ans[35] = 1;
		ans[36] = -1;
		return ans;
	}
	public static int[] nextState(int[] s1, int action, int hold)
	{
		Board t = new Board(s1);
		t.set(action / 8 + 1, action % 8 + 1, hold);
		return t.getTable();
	}
	public static int[] searchNext(int[] s1, int hold)
	{
		Board t = new Board(s1);
		return t.nextSteps(hold);
	}
	public static int calcHand(int[] s)
	{
		int total = 0;
		for (int i = 0; i < 64; i++)
			if (s[i] != 0)
				total++;
		return total;
	}
	public static int getWinner(int[] s)
	{
		int ans = -1;
		int a = 0, b = 0;
		for (int i = 0; i < 64; i++)
			if ((s[i] == 1) || (s[i] == 2))
				a++;
			else if ((s[i] == -1) || (s[i] == -2))
				b++;
		if (a > b)
			return 1;
		else if (a < b)
			return -1;
		return 0;
	}
	public int[] getTable()
	{
		int[] ans = new int[64];
		for (int i = 1; i <= 8; i++)
			for (int j = 1; j <= 8; j++)
				ans[(i - 1) * 8 + j - 1] = bigTable[i][j];
		return ans;
	}
	public void set(int x, int y, int who)
	{
		int x1, y1, count = 0;
		if ((x < 1) || (x > 8) || (y < 1) || (y > 8)) return;
		for (int i = 1; i <= 8; i++)
		{
			count = 0; 
			x1 = x + MoveX[i];
			y1 = y + MoveY[i];
			count++;
			if ((x1 < 1) || (x1 > 8) || (y1 < 1) || (y1 > 8)) continue;
			if ((bigTable[x1][y1] != -who) && (bigTable[x1][y1] != -2 * who)) continue;
			while ((bigTable[x1][y1] == -who) || (bigTable[x1][y1] == -2 * who))
			{
				x1 += MoveX[i];
				y1 += MoveY[i];
				count++;
				if ((x1 < 1) || (x1 > 8) || (y1 < 1) || (y1 > 8)) break; 
				if (bigTable[x1][y1] == 0) break;
				if ((bigTable[x1][y1] == who) || (bigTable[x1][y1] == 2 * who))
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
		bigTable[x][y] = who * 2;
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
			if ((bigTable[x1][y1] != -who) && (bigTable[x1][y1] != -2 * who)) continue;
			while ((bigTable[x1][y1] == -who) || (bigTable[x1][y1] == -2 * who))
			{
				x1 += MoveX[i];
				y1 += MoveY[i];
				if (((x1 < 1) || (x1 > 8) || (y1 < 1) || (y1 > 8))) break;
				if (bigTable[x1][y1] == 0) break;
				if ((bigTable[x1][y1] == who) || (bigTable[x1][y1] == 2 * who))
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
					ans[ans[0]] = (i - 1) * 8 + j - 1;
				}
		return ans;		
	}
	public static boolean terminal(int[] s)
	{
		Board b = new Board(s);
		if (Board.calcHand(s) == 64) return true;
		int[] a1, b1;
		a1 = b.nextSteps(1);
		if (a1[0] > 0) return false;
		b1 = b.nextSteps(-1);
		if (b1[0] > 0) return false;
		return true;
	}
}