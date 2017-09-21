public class Transition
{
	public int[] s1, s2;
	public int action;
	public double reward;
	public Transition(int[] a1, int[] a2, int a, double r)
	{
		s1 = a1;
		s2 = a2;
		action = a;
		reward = r;
	}
}