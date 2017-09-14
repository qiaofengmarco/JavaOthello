import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class GUI extends JFrame implements MouseListener
{
	public Chess[][] p;
	public JPanel big;
	private Player player;
	private AI ai;
	private Table table = new Table();
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
		addMouseListener(this);
		setBackground(Color.white);
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
		player = new Player(frame.phold);
		ai = new AI(frame.ahold);
		ai.table = table;
		//System.out.printf("%d, %d", player.hold, ai.hold);
		big.repaint();
		big.setFocusable(true);
		//frame.dispose();
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
					ai.table = table;
					now = ai.hold;
					lock = true;
					done = true;
					big.repaint();
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
				if (table.bigTable[i][j] == player.hold)
					p++;
				else
					a++;
		if (p > a)
			System.out.println("Congratulation! You win!");
		else
			System.out.println("Sorry! AI wins!");
	}
	public void play()
	{
		int step1, count1 = 0;
		int[] moves = new int[65];
		init();
		while (true)
		{
			big.repaint();
			if (now == ai.hold)
			{
				//System.out.println("aaa");
				ai.table = table;
				step1 = ai.move();
				if (step1 > 0)
				{
					table = ai.table;
					count1 = 0;
				}
				else
				{
					count1++;
					//System.out.println();
				}
				now = player.hold;
			}
			else if (now == player.hold)
			{
				moves = table.nextSteps(player.hold);
				if ((count1 > 0) && (moves[0] == 0))
				{
					checkWinner();
					break;
				}
				else if (moves[0] == 0)
				{
					count1++;
					now = ai.hold;
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
			}
		}
	}
}