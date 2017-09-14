import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class TrainFrame extends JFrame
{
	public Chess[][] p;
	public JPanel big;
	private AI ai;
	private AlphaBetaAI ab;
	private Table table = new Table();
	private int now = 1;
	private boolean lock = true, done = false;
	public TrainFrame()
	{
		super("Othello");
		p = new Chess[8][8];
		big = new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				for (int i = 0; i < 8; i++)
					for (int j = 0; j < 8; j++)
					{
						p[i][j].color = table.bigTable[i + 1][j + 1];
						p[i][j].repaint();
					}
			}
		};
		big.setLayout(new GridLayout(8, 8, 2, 2));
		big.setBackground(Color.white);
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
			{
				p[i][j] = new Chess();
				p[i][j].color = table.bigTable[i + 1][j + 1];
				big.add(p[i][j]);
			}
		add(big);
		setBackground(Color.white);
		setSize(600, 600);
	}
	public void init()
	{
		int kk = (int)(Math.random() * 2);
		kk = (int)Math.pow(-1, kk);
		ab = new AlphaBetaAI(kk);
		ai = new AI(-kk);
		ai.table = table;
		ab.table = table;
		String ss = (kk == 1)?"black":"white";
		System.out.printf("AlphaBetaAI is %s\n", ss);
		big.repaint();
		big.setFocusable(true);
		setVisible(true);
	}
	public void checkWinner()
	{
		int b = 0, a = 0;
		for (int i = 1; i <= 8; i++)
			for (int j = 1; j <= 8; j++)
				if (table.bigTable[i][j] == ab.hold)
					b++;
				else if (table.bigTable[i][j] == ai.hold)
					a++;
		if (b > a)
			System.out.println("AlphaBetaAI wins!");
		else if (a > b)
			System.out.println("Simple AI wins!");
		else 
			System.out.println("Draw!");
	}
	public void play()
	{
		int step1, count1 = 0;
		int[] moves = new int[65];
		init();
		while (true)
		{
			big.repaint();
			try
			{
				Thread.sleep(200);
			}
			catch (Exception e){}	
			if (now == ai.hold)
			{
				ai.table = table;
				step1 = ai.move();
				if (step1 > 0)
				{
					table = ai.table;
					count1 = 0;
				}
				else
				{
					if (count1 > 0)
					{
						checkWinner();
						break;
					}
					count1++;
				}
				now = ab.hold;
			}
			else if (now == ab.hold)
			{
				ab.table = table;
				step1 = ab.move();
				if (step1 > 0)
				{
					table = ab.table;
					count1 = 0;
				}
				else
				{
					if (count1 > 0)
					{
						checkWinner();
						break;
					}
					count1++;
				}
				now = ai.hold;
			}
		}
	}
}