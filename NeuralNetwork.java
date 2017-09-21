import java.io.*;
import java.util.*;
public class NeuralNetwork
{
	private double rate = 0.001;
	private double[][] sum;
	private double[][][] w, v, m;
	private double[][] vv, mm, b;
	private String path;
	public NeuralNetwork(String name)
	{
		int kk = 0;
		path = name;
		vv = new double[4][100];
		mm = new double[4][100];
		b = new double[4][];
		sum = new double[4][];
		for (int i = 0; i <= 2; i++)
		{
			b[i] = new double[100];
			sum[i] = new double[100];
		}
		sum[3] = new double[64];
		b[3] = new double[64];
		w = new double[4][][];
		v = new double[4][][];
		m = new double[4][][];
		w[0] = new double[64][100];
		v[0] = new double[64][100];
		m[0] = new double[64][100];
		w[3] = new double[100][64];
		v[3] = new double[100][64];
		m[3] = new double[100][64];
		for (int i = 1; i <= 2; i++)
		{
			w[i] = new double[100][100];
			v[i] = new double[100][100];
			m[i] = new double[100][100];
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
			for (int i = 0; i < 64; i++)
				for (int j = 0; j < 100; j++)
				{
						kk = (int)(Math.random() * 2);
						w[0][i][j] = (Math.random() * 29 + 1) * 0.01 * ((int)Math.pow(-1, kk));
						handler.writeDouble(w[0][i][j]);
				}
			for (int i = 1; i < 3; i++)
				for (int j = 0; j < 100; j++)
					for (int k = 0; k < 100; k++)
					{
						kk = (int)(Math.random() * 2);
						w[i][j][k] = (Math.random() * 29 + 1) * 0.01 * ((int)Math.pow(-1, kk));
						handler.writeDouble(w[i][j][k]);
					}
			for (int i = 0; i < 100; i++)
				for (int j = 0; j < 64; j++)
				{
					kk = (int)(Math.random() * 2);
					w[3][i][j] = (Math.random() * 29 + 1) * 0.01 * ((int)Math.pow(-1, kk));
					handler.writeDouble(w[3][i][j]);
				}
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 100; j++)
				{
					kk = (int)(Math.random() * 2);
					b[i][j] = (Math.random() * 29 + 1) * 0.01 * ((int)Math.pow(-1, kk));
					handler.writeDouble(b[i][j]);
				}
			for (int i = 0; i < 64; i++)
			{
				kk = (int)(Math.random() * 2);
				b[3][i] = (Math.random() * 29 + 1) * 0.01 * ((int)Math.pow(-1, kk));
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
		for (int i = 0; i < 64; i++)
			for (int j = 0; j < 100; j++)
				handler.writeDouble(w[0][i][j]);
		for (int i = 1; i < 3; i++)
			for (int j = 0; j < 100; j++)
				for (int k = 0; k < 100; k++)
					handler.writeDouble(w[i][j][k]);
		for (int i = 0; i < 100; i++)
			for (int j = 0; j < 64; j++)
				handler.writeDouble(w[3][i][j]);
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 100; j++)
				handler.writeDouble(b[i][j]);
		for (int i = 0; i < 64; i++)
			handler.writeDouble(b[3][i]);
		handler.flush();
		handler.closeAll();	
	}
	public void load()
	{
		IOHandler handler = new IOHandler(path);
		handler.openFileRead();
		for (int i = 0; i < 64; i++)
			for (int j = 0; j < 100; j++)
				w[0][i][j] = handler.readDouble();
		for (int i = 1; i < 3; i++)
			for (int j = 0; j < 100; j++)
				for (int k = 0; k < 100; k++)
					w[i][j][k] = handler.readDouble();
		for (int i = 0; i < 100; i++)
			for (int j = 0; j < 64; j++)
				w[3][i][j] = handler.readDouble();
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 100; j++)
				b[i][j] = handler.readDouble();
		for (int i = 0; i < 64; i++)
			b[3][i] = handler.readDouble();
		handler.closeAll();			
	}
	public double LRelu(double a)
	{
		if (a >= 0)
			return a;
		return 0.3 * a;
	}
	public double de(double a)
	{
		if (a >= 0)
			return 1.0;
		return 0.3;
	}
	public double forward(int[] x, int action) 
	//x is a 64 * 1 vector
	{
		double ans = 0;
		for (int j = 0; j < 100; j++)
		{
			for (int i = 0; i < 64; i++)
				sum[0][j] += x[i] * w[0][i][j];
			sum[0][j] = LRelu(sum[0][j] + b[0][j]);
		}
		for (int k = 1; k <= 2; k++)
			for (int j = 0; j < 100; j++)
			{
				for (int i = 0; i < 100; i++)
					sum[k][j] += sum[k - 1][i] * w[k][i][j];
				sum[k][j] = LRelu(sum[k][j] + b[k][j]);
			}
		for (int i = 0; i < 64; i++)
		{
			for (int j = 0; j < 100; j++)
				sum[3][j] += sum[2][i] * w[3][i][j];
			sum[3][i] = Math.tanh(sum[3][i] + b[3][i]);
		}
		return sum[3][action];
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
	public void backward(double[] y, double[] t)
	{
		double[][] error;
		error = new double[3][];
		for (int i = 0; i < 2; i++)
			error[i] = new double[100];
		error[2] = new double[64];
		double temp = 0;
		
		//error of output layer
		for (int i = 0; i < 64; i++)
			error[2][i] = (1 - Math.pow(y[i], 2.0)) * (y[i] - t[i]);
		
		for (int i = 0; i < 100; i++)
		{
			temp = 0;
			for (int j = 0; j < 64; j++)
			{
				//output layer weight update
				adam(3, i, j, error[2][j] * sum[3][j]); 
				adam_b(3, j, error[2][j]);
				
				//the last hidden layer weight update
				temp += error[2][j] * w[3][i][j];
				adam(2, i, j, error[2][i] * sum[2][i]);				
			}
			error[1][i] = de(sum[2][i]) * temp;
			adam_b(2, i, error[2][i]);
		}
		
		for (int k = 1; k >= 0; k--)
			for (int i = 0; i < 100; i++)
			{
				temp = 0;
				for (int j = 0; j < 100; j++)
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