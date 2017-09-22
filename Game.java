public class Game
{
	public static void main(String[] args)
	{
		Playground game = new Playground();
		//game.setSize(600, 600);
		//game.setVisible(true);
		game.play();
		game.setVisible(false);
		game.dispose();
	}
}