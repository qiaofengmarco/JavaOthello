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
		super.paintComponent(g);
		if (color == 1)
			g.fillOval(0, 0, getWidth(), getHeight());
		else if (color == -1)
			g.drawOval(0, 0, getWidth(), getHeight());
		g.drawRect(0, 0, getWidth(), getHeight());
	}
}