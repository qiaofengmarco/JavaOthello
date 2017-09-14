public class Game
{
	public static void main(String[] args)
	{
		GUI game = new GUI();
		game.setSize(600, 600);
		game.setVisible(true);
		game.play();
		game.setVisible(false);
		game.dispose();
	}
}