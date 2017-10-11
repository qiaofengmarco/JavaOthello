package Game;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import Game.*;
import AI.*;
public class GUI extends JFrame implements MouseListener
{
	public Chess[][] p;
	public JPanel big;
	private Player player;
	private UCTAI ai;
	private Board table = new Board();
	private int now = 1;
	private boolean lock = true, done = false;
	public GUI()
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
						if (table.bigTable[i + 1][j + 1] * ai.hold == -2)
							table.bigTable[i + 1][j + 1] /= 2;
						p[i][j].color = table.bigTable[i + 1][j + 1];
						p[i][j].repaint();
						if ((table.bigTable[i + 1][j + 1] == -2) || (table.bigTable[i + 1][j + 1] == 2))
							table.bigTable[i + 1][j + 1] /= 2;
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
		addMouseListener(this);
		setBackground(Color.white);
		setSize(600, 600);
		setResizable(false);
	}
	public void init()
	{
		InitFrame frame =  new InitFrame();
		while (!frame.ok)
		{
			try
			{
				Thread.sleep(10);
			}
			catch (Exception e){}
		}
		try
		{
			Thread.sleep(200);
		}
		catch (Exception e){}		
		player = new Player(frame.phold);
		ai = new UCTAI(frame.ahold);
		ai.table = table;
		setVisible(true);
		big.repaint();
		big.setFocusable(true);
	}
	public void mousePressed(MouseEvent event)
	{
		int x1, y1, tx, ty, width, length;
		if (lock) return;
		if (now == player.hold)
		{
			tx = event.getX();
			ty = event.getY();
			Point end = SwingUtilities.convertPoint(big, big.getWidth(), big.getHeight(), this);
			Point top = SwingUtilities.convertPoint(big, 0, 0, this);
			width = (end.x - top.x) / 8;
			length = (end.y - top.y) / 8;
			if ((tx >= top.x) && (ty >= top.y) && (tx <= end.x) && (ty <= end.y))
			{
				x1 = (tx - top.x) / width + 1;
				y1 = (ty - top.y) / length + 1;
				if (table.checkStep(y1, x1, player.hold))
				{
					table.set(y1, x1, player.hold);
					ai.table = new Board(table);
					now = ai.hold;
					lock = true;
					done = true;
					//big.repaint();
				}
			}
		}
	}
	public void mouseReleased(MouseEvent event){}
	public void mouseClicked(MouseEvent event){}
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void checkWinner()
	{
		int p = 0, a = 0;
		for (int i = 1; i <= 8; i++)
			for (int j = 1; j <= 8; j++)
				if (table.bigTable[i][j] == ai.hold)
					a++;
				else if (table.bigTable[i][j] == player.hold)
					p++;
		if (p > a)
			System.out.println("Congratulation! You win!");
		else if (a > p)
			System.out.println("Sorry! AI wins!");
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
			//big.repaint();
			
			if (now == ai.hold)
			{
				ai.table = new Board(table);
				step1 = ai.move();
				if (step1 >= 0)
				{
					table = new Board(ai.table);
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
				now = player.hold;
			}
			else if (now == player.hold)
			{
				big.repaint();
				moves = table.nextSteps(player.hold);
				if ((count1 > 0) && (moves[0] == 0))
				{
					checkWinner();
					break;
				}
				else if (moves[0] == 0)
				{
					count1++;
				}
				else
				{
					lock = false;
					done = false;
					while (!done)
					{
						try
						{
							Thread.sleep(10);
						}
						catch (Exception e){}
					}
					count1 = 0;
				}
				now = ai.hold;
			}
		}
	}
}