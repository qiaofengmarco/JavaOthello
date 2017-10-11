public class Main
{
	public static void main(String[] args)
	{
		Game.GUI game = new Game.GUI();
		game.play();
		game.setVisible(false);
		game.dispose();
	}
}