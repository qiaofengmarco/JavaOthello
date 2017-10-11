package NeuralNetwork;
public class Activator
{
	public static double Relu(double a)
	{
		if (a > 0)
			return a;
		return 0;
	}
	public static double dRelu(double a)
	{
		if (a > 0)
			return 1;
		return 0;
	}
	public static double LRelu(double rate, double a)
	{
		if (a > 0)
			return a;
		return rate * a;
	}
	public static double dLRelu(double rate, double a)
	{
		if (a > 0)
			return 1;
		return rate;
	}
	public static double Sigmoid(double a)
	{
		double ans = 1.0 / (1.0 + Math.exp(-a));
		return ans;
	}
	public static double dSigmoid(double a)
	{
		double ans = a * (1.0 - a);
		return ans;
	}
}