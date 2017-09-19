import java.io.*;
import java.util.*;
public class NeuralNetwork
{
	private double rate = 0.001;
	private double[][] sum = new double[3][16];
	private double[][][] w, v, m;
	private double[][] vv, mm, b;
	private String path;
	public NeuralNetwork(String name)
	{
		int kk = 0;
		path = name;
		vv = new double[4][16];
		mm = new double[4][16];
		b = new double[4][];
		for (int i = 0; i <= 2; i++)
			b[i] = new double[16];
		b[3] = new double[2];
		w = new double[4][][];
		v = new double[4][][];
		m = new double[4][][];
		w[0] = new double[65][16];
		v[0] = new double[65][16];
		m[0] = new double[65][16];
		w[3] = new double[16][2];
		v[3] = new double[16][2];
		m[3] = new double[16][2];
		for (int i = 1; i <= 2; i++)
		{
			w[i] = new double[16][16];
			v[i] = new double[16][16];
			m[i] = new double[16][16];
		}
		
		File f = new File(name);
		if (!f.exists())
		{
			try 
			{
				f.createNewFile();
			}
			catch(IOException e){}
			IOHandler handler = new IOHandler(path);
			handler.openFileWrite();
			for (int i = 0; i < 65; i++)
				for (int j = 0; j < 16; j++)
				{
						kk = (int)(Math.random() * 2);
						w[0][i][j] = (Math.random() * 9 + 1) * 0.1 * ((int)Math.pow(-1, kk));
						handler.writeDouble(w[0][i][j]);
				}
			for (int i = 1; i < 3; i++)
				for (int j = 0; j < 16; j++)
					for (int k = 0; k < 16; k++)
					{
						kk = (int)(Math.random() * 2);
						w[i][j][k] = (Math.random() * 9 + 1) * 0.1 * ((int)Math.pow(-1, kk));
						handler.writeDouble(w[i][j][k]);
					}
			for (int i = 0; i < 16; i++)
				for (int j = 0; j < 2; j++)
				{
					kk = (int)(Math.random() * 2);
					w[3][i][j] = (Math.random() * 9 + 1) * 0.1 * ((int)Math.pow(-1, kk));
					handler.writeDouble(w[3][i][j]);
				}
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 16; j++)
				{
					kk = (int)(Math.random() * 2);
					b[i][j] = (Math.random() * 9 + 1) * 0.1 * ((int)Math.pow(-1, kk));
					handler.writeDouble(b[i][j]);
				}
			for (int i = 0; i < 2; i++)
			{
				kk = (int)(Math.random() * 2);
				b[3][i] = (Math.random() * 9 + 1) * 0.1 * ((int)Math.pow(-1, kk));
				handler.writeDouble(b[3][i]);
			}				
			handler.flush();
			handler.closeAll();
		}
		else
			load();
	}
	public void store()
	{
		IOHandler handler = new IOHandler(path);
		handler.openFileWrite();
		for (int i = 0; i < 65; i++)
			for (int j = 0; j < 16; j++)
				handler.writeDouble(w[0][i][j]);
		for (int i = 1; i < 3; i++)
			for (int j = 0; j < 16; j++)
				for (int k = 0; k < 16; k++)
					handler.writeDouble(w[i][j][k]);
		for (int i = 0; i < 16; i++)
			for (int j = 0; j < 2; j++)
				handler.writeDouble(w[3][i][j]);
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 16; j++)
				handler.writeDouble(b[i][j]);
		for (int i = 0; i < 2; i++)
			handler.writeDouble(b[3][i]);
		handler.flush();
		handler.closeAll();	
	}
	public void load()
	{
		IOHandler handler = new IOHandler(path);
		handler.openFileRead();
		for (int i = 0; i < 65; i++)
			for (int j = 0; j < 16; j++)
				w[0][i][j] = handler.readDouble();
		for (int i = 1; i < 3; i++)
			for (int j = 0; j < 16; j++)
				for (int k = 0; k < 16; k++)
					w[i][j][k] = handler.readDouble();
		for (int i = 0; i < 16; i++)
			for (int j = 0; j < 2; j++)
				w[3][i][j] = handler.readDouble();
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 16; j++)
				b[i][j] = handler.readDouble();
		for (int i = 0; i < 2; i++)
			b[3][i] = handler.readDouble();
		handler.closeAll();			
	}
	public double LRelu(double a)
	{
		if (a >= 0)
			return a;
		return 0.3 * a;
	}
	public double forward(int[] x, int hold) 
	//x is a 65 * 1 vector, representing state St(64 * 1 vector) and action A
	{
		double ans = 0;
		for (int j = 0; j < 16; j++)
		{
			for (int i = 0; i < 65; i++)
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
		if (hold == -1)
		{
			for (int i = 1; i < 16; i++)
				ans += sum[2][i] * w[3][i][0];
			return ans + b[3][0];
		}
		else if (hold == 1)
		{
			for (int i = 1; i < 16; i++)
				ans += sum[2][i] * w[3][i][1];
			return ans + b[3][1];			
		}
		return 0;
	}
	public double de(double a)
	{
		if (a >= 0)
			return 1.0;
		return 0.3;
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
	public void backward(double y, double t, int hold)
	{
		double[][] error = new double[2][16];
		double temp = 0;
		double error1 = de(t) * (t - y);
		for (int i = 0; i < 16; i++)
		{
			if (hold == -1)
			{
				//output layer
				adam(3, i, 0, error1 * t); 
				adam_b(3, 0, error1);
				
				//n-1 hidden layer
				error[1][i] = de(sum[2][i]) * w[3][i][0] * error1;
				adam(2, i, 0, error1 * sum[2][i]);			
			}
			else if (hold == 1)
			{
				//output layer
				adam(3, i, 1, error1 * t); 
				adam_b(3, 1, error1);
				
				//n-1 hidden layer
				error[1][i] = de(sum[2][i]) * w[3][i][1] * error1;
				adam(2, i, 1, error1 * sum[2][i]);
			}
			adam_b(2, i, error1);
		}
		for (int k = 1; k >= 0; k--)
			for (int i = 0; i < 16; i++)
			{
				temp = 0;
				for (int j = 0; j < 16; j++)
				{
					temp += error[k][j] * w[k + 1][i][j];
					adam(k, i, j, error[k][i] * sum[k][i]);
				}
				if (k > 0)
					error[k - 1][i] = de(sum[k][i]) * temp;
				adam_b(k, i, error[k][i]); 
			}
	}
}