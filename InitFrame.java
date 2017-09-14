import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
public class InitFrame extends JFrame implements MouseListener
{
	ImageIcon icon1, icon2;
	JPanel p1, p2, big;
	public int phold = 0, ahold = 0;
	public boolean ok = false;
	public InitFrame()
	{
		super("Black or White?");
		p1 = new JPanel();
		p2 = new JPanel();
		big = new JPanel();
		p1.setBackground(Color.black);
		p2.setBackground(Color.white);
		big.setLayout(new GridLayout(1, 2, 1, 1));
		big.add(p1);
		big.add(p2);
		big.setBounds(0, 0, 400, 200);
		add(big);
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				if ((phold == 0) && (ahold == 0))
				{
					phold = 1;
					ahold = -1;
					ok = true;
				}
			}
		});
		addMouseListener(this);
		setSize(400, 235);
		setVisible(true);
		setFocusable(true);
	}
	public void mousePressed(MouseEvent event)
	{
		int x1, y1, tx, ty, width, length;
		tx = event.getX();
		ty = event.getY();
		Point end = SwingUtilities.convertPoint(big, big.getWidth(), big.getHeight(), this);
		Point top = SwingUtilities.convertPoint(big, 0, 0, this);
		width = (end.x - top.x) / 2;
		length = end.y - top.y;
		if ((tx >= top.x) && (ty >= top.y) && (tx <= end.x) && (ty <= end.y))
		{
			x1 = (tx - top.x) / width + 1;
			y1 = (ty - top.y) / length + 1;
			//System.out.printf("%d %d", x1, y1);
			if (x1 == 1)
			{
				phold = 1;
				ahold = -1;
				ok = true;
				setVisible(false);
			}
			else
			{
				phold = -1;
				ahold = 1;
				ok = true;
				setVisible(false);
			}
		}
	}
	public void mouseReleased(MouseEvent event){}
    public void mouseClicked(MouseEvent event){}
    public void mouseEntered(MouseEvent event){}
    public void mouseExited(MouseEvent event){}
	/*public static void main(String[] args)
	{
		InitFrame frame = new InitFrame();
	}*/
}