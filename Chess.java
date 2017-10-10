import javax.swing.*;
import java.awt.*;
public class Chess extends JPanel
{
	public int color = 0;
	public Chess() 
	{
		setBackground(Color.white);
	}
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g);
		g.drawRect(0, 0, getWidth(), getHeight());
		if ((color == 1) || (color == 2))
			g.fillOval(0, 0, getWidth(), getHeight());
		else if ((color == -1) || (color == -2))
			g.drawOval(0, 0, getWidth(), getHeight());
		if ((color == 2) || (color == -2))
		{
			if (color == -2)
				g2.setColor(Color.GREEN);
			else
				g2.setColor(Color.RED);
			g2.setStroke(new BasicStroke(6.0f));
			g.drawRect(0, 0, getWidth(), getHeight());
			g2.setStroke(new BasicStroke(1.0f));
			g2.setColor(Color.BLACK);
		}
	}
}