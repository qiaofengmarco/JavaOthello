package AI;
public class Transition
{
	public int[] s1, s2;
	public int action;
	public double reward;
	public Transition(int[] a1, int[] a2, int a, double r)
	{
		for (int i = 0; i < 64; i++)
		{
			s1[i] = a1[i];
			s2[i] = a2[i];
		}
		action = a;
		reward = r;
	}
}