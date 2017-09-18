public class NeuralNetwork
{
	private rate = 0.002;
	private double[][] sum = new double[3][16];
	public double[][][] w;
	public double[][][] v;
	public double[][] vv;
	public double[][] b;
	public NeuralNetwork()
	{
		int kk = 0;
		vv = new double[4][16];
		mm = new double[4][16];
		b = new double[4][];
		for (int i = 0; i <= 2; i++)
			b[i] = new double[16];
		b[3] = new double[1];
		w = new double[4][][];
		v = new double[4][][];
		m = new double[4][][];
		w[0] = new double[64][16];
		v[0] = new double[64][16];
		m[0] = new double[64][16];
		w[3] = new double[16][1];
		v[3] = new double[16][1];
		m[3] = new double[16][1];
		for (int i = 1; i <= 2; i++)
		{
			w[i] = new double[16][16];
			v[i] = new double[16][16];
			m[i] = new double[16][16];
		}
		for (int i = 0; i < 64; i++)
			for (int j = 0; j < 16; j++)
			{
					kk = (int)(Math.random() * 2);
					w[0][i][j] = (Math.random() * 9 + 1) * 0.1 * ((int)Math.pow(-1, kk));
			}
		for (int i = 1; i < 3; i++)
			for (int j = 0; j < 16; j++)
				for (int k = 0; k < 16; k++)
				{
					kk = (int)(Math.random() * 2);
					w[i][j][k] = (Math.random() * 9 + 1) * 0.1 * ((int)Math.pow(-1, kk));
				}
		for (int i = 0; i < 16; i++)
		{
			kk = (int)(Math.random() * 2);
			w[3][i][0] = (Math.random() * 9 + 1) * 0.1 * ((int)Math.pow(-1, kk));
		}
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 16; j++)
			{
				kk = (int)(Math.random() * 2);
				b[i][j] = (Math.random() * 9 + 1) * 0.1 * ((int)Math.pow(-1, kk));
			}
	}
	public double LRelu(double a)
	{
		if (a >= 0)
			return a;
		return 0.01 * a;
	}
	public double forward(int[] x)
	{
		double ans = 0;
		for (int j = 0; j < 16; j++)
		{
			for (int i = 0; i < 64; i++)
				sum[0][j] += x[i] * w[0][i][j];
			sum[0][j] = LRelu(sum[0][j] + b[0][j]);
		}
		for (int k = 1; k <= 2; k++)
			for (int j = 1; j < 16; j++)
			{
				for (int i = 1; i < 16; i++)
					sum[k][j] += sum[k - 1][j] * w[k][i][j];
				sum[k][j] = LRelu(sum[k][j] + b[k][j]);
			}
		for (int i = 1; i < 16; i++)
			ans += sum[2][i] * w[3][i];
		return LRelu(ans + b[3][0]);
	}
	public double de(double a)
	{
		if (a >= 0)
			return 1.0;
		return 0.01;
	}
	public void adam(int i, int j, int k, double a)
	{
		m[i][j][k] = 0.9 * m[i][j][k] + 0.1 * a;
		v[i][j][k] = 0.999 * v[i][j][k] + 0.001 * a * a;
		w[i][j][k] -= rate * m[i][j][k] / (Math.sqrt(v[i][j][k]) + 1e-6);
	}
	public void adam_b(int i, int j, double a)
	{
		mm[i][j] = 0.9 * mm[i][j] + 0.1 * a;
		vv[i][j] = 0.999 * vv[i][j] + 0.001 * a * a;
		b[i][j] -= rate * mm[i][j] / (Math.sqrt(vv[i][j]) + 1e-6);
	}
	public double backward(double y, double t)
	{
		double[][] error = new double[2][16];
		double error1 = de(t) * (t - y);
		double temp = 0;
		for (int i = 0; i < 16; i++)
		{
			v[3][i][0] = 0.9 * v[3][i][0] + 0.002 * error1 * t;
			w[3][i][0] -= v[3][i][0];
			vv[3][i] = 0.9 * vv[3][i] + 0.002 * error1;
			b[3][i] -= v[3][i];
			
			error[1][i] = de(sum[2][i]) * w[3][i][0] * error1;
			adam(2, i, 0, error1 * sum[2][0]);
			adam_b(2, i, error1);
		}
		for (int k = 1; k >= 0; k--)
			for (int i = 0; i < 16; i++)
			{
				temp = 0;
				for (int j = 0; j < 16; j++)
					temp += error[k + 1][j] * w[k + 1][i][j];
				if (k > 0)
					error[k - 1][i] = de(sum[k][i]) * temp;
				adam_b(k, i, error[k][i]);
				for (int j = 0; j < 16; j++)
					adam(k, i, j, error[k][i] * sum[k][j]);
			}
	}
}