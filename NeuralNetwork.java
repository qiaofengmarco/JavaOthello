import java.io.*;
import java.util.*;
public class NeuralNetwork
{
	private double rate = 0.001;
	private double[][] sum;
	private double[][][] w, v, m, dw;
	private double[][] vv, mm, b, db;
	private String path;
	private double[][] error;
	private int[] ct = new int[64];
	public NeuralNetwork(String name)
	{
		int kk = 0;
		path = name;
		vv = new double[4][100];
		mm = new double[4][100];
		b = new double[4][];
		db = new double[4][];
		sum = new double[3][];
		for (int i = 0; i <= 2; i++)
		{
			b[i] = new double[100];
			db[i] = new double[100];
			sum[i] = new double[100];
		}
		b[3] = new double[64];
		db[3] = new double[64];
		w = new double[4][][];
		dw = new double[4][][];
		v = new double[4][][];
		m = new double[4][][];
		w[0] = new double[64][100];
		dw[0] = new double[64][100];
		v[0] = new double[64][100];
		m[0] = new double[64][100];
		w[3] = new double[100][64];
		dw[3] = new double[100][64];
		v[3] = new double[100][64];
		m[3] = new double[100][64];
		for (int i = 1; i <= 2; i++)
		{
			w[i] = new double[100][100];
			dw[i] = new double[100][100];
			v[i] = new double[100][100];
			m[i] = new double[100][100];
		}
		
		
		error = new double[4][];
		for (int i = 0; i < 3; i++)
			error[i] = new double[100];
		error[3] = new double[1];
		
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
			{
				if (i == 0) sum[0][j] = 0; //clear
				sum[0][j] += x[i] * w[0][i][j];
			}
			sum[0][j] = LRelu(sum[0][j] + b[0][j]);
		}
		
		for (int k = 1; k <= 2; k++)
			for (int j = 0; j < 100; j++)
			{
				for (int i = 0; i < 100; i++)
				{
					if (i == 0) sum[k][j] = 0; //clear
					sum[k][j] += sum[k - 1][i] * w[k][i][j];
				}
				sum[k][j] = LRelu(sum[k][j] + b[k][j]);
			}
			
		ans = 0;
		for (int i = 0; i < 100; i++)
			ans += sum[2][i] * w[3][i][action];
		ans = Math.tanh(ans + b[3][action]);
		
		return ans;
	}
	public void backward(int[][] x, int[] action, double[] y, int minibatchSize)
	{
		double t, temp = 0;
		for (int kk = 0; kk < minibatchSize; kk++)
		{
			t = forward(x[kk], action[kk]);	
			ct[action[kk]]++;
			temp = 0;
						
			//[output -> (n-1) hidden]	
			//dE / dnet_j = df * (y - t)
			//dE /dw_i,j = dE / dnet_j * x_i,j		
			error[3][0] = (1 - Math.pow(y[kk], 2.0)) * (y[kk] - t);			
			for (int i = 0; i < 100; i++)
			{
				//update w_3,i,action and b_3,action 
				dw[3][i][action[kk]] +=  error[3][0] * sum[2][i]; 
			}
			db[3][action[kk]] += error[3][0];		
		
		
		
			//[(n-1) hidden -> (n-2) hidden]
			//dE / dnet_j = df(j) * sigma(error_l,k * w_l,j,k) where l is round and k is from j's downstream 
			//dE /dw_i,j = dE / dnet_j * x_i,j		
			for (int i = 0; i < 100; i++)
			{
				for (int j = 0; j < 100; j++)
				{
					//the statement below simply means:
					//temp = error[2][0] * w[3][j][action];
					//error[2][j] = temp * de(sum[2][j]);
				
					error[2][j] = error[3][0] * w[3][j][action[kk]] * de(sum[2][i]);
					dw[2][i][j] += error[2][j] * sum[1][i];
				}			
				db[2][i] += error[2][i];
			}
		
		
		
			//[(n-2) hidden -> (n-3) hidden]
			//dE / dnet_j = df(j) * sigma(error_l,k * w_l,j,k) where l is round and k is from j's downstream 
			//dE /dw_i,j = dE / dnet_j * x_i,j		
			for (int i = 0; i < 100; i++)
			{
				for (int j = 0; j < 100; j++)
				{
					temp = 0;
					for (int k = 0; k < 100; k++)
						temp += error[2][k] * w[2][j][k];
					error[1][j] = temp * de(sum[1][i]);
					dw[1][i][j] += error[1][j] * sum[0][i];
				}
				db[1][i] += error[1][i]; 
			}
		
		
		
			//[(n-3) hidden -> input]
			//dE / dnet_j = df(j) * sigma(error_l,k * w_l,j,k) where l is round and k is from j's downstream 
			//dE /dw_i,j = dE / dnet_j * x_i,j		
			for (int i = 0; i < 64; i++)
			{
				for (int j = 0; j < 100; j++)
				{
					temp = 0;
					for (int k = 0; k < 100; k++)
						temp += error[1][k] * w[1][j][k];
					error[0][j] = temp * de(sum[0][i]);
					dw[0][i][j] += error[0][j] * x[kk][i];
				}
				db[0][i] = error[0][i];
			}
		}
		
		for (int kk = 0; kk < minibatchSize; kk++)
			if (ct[action[kk]] > 0)
			{
				for (int i = 0; i < 100; i++)
				{
					dw[3][i][action[kk]] = (1.0 / ct[action[kk]]) * dw[3][i][action[kk]];
					m[3][i][action[kk]] = 0.9 * m[3][i][action[kk]] + 0.1 * dw[3][i][action[kk]];
					v[3][i][action[kk]] = 0.999 * v[3][i][action[kk]] + 0.001 * dw[3][i][action[kk]] * dw[3][i][action[kk]];
					w[3][i][action[kk]] -= rate * m[3][i][action[kk]] / (Math.sqrt(v[3][i][action[kk]]) + 1e-8);
					dw[3][i][action[kk]] = 0;
				}
				db[3][action[kk]] = (1.0 / ct[action[kk]]) * db[3][action[kk]];
				mm[3][action[kk]] = 0.9 * mm[3][action[kk]] + 0.1 * db[3][action[kk]];
				vv[3][action[kk]] = 0.999 * vv[3][action[kk]] + 0.001 * db[3][action[kk]] * db[3][action[kk]];
				b[3][action[kk]] -= rate * mm[3][action[kk]] / (Math.sqrt(vv[3][action[kk]]) + 1e-8);
				db[3][action[kk]] = 0;
				ct[action[kk]] = 0;
			}
			
		for (int i = 0; i < 64; i++)
			for (int j = 0; j < 100; j++)
			{
				dw[0][i][j] = (1 / minibatchSize) * dw[0][i][j];
				m[0][i][j] = 0.9 * m[0][i][j] + 0.1 * dw[0][i][j];
				v[0][i][j] = 0.999 * v[0][i][j] + 0.001 * dw[0][i][j] * dw[0][i][j];
				w[0][i][j] -= rate * m[0][i][j] / (Math.sqrt(v[0][i][j]) + 1e-8);
				dw[0][i][j] = 0;
			}
		for (int k = 1; k <= 2; k++)
			for (int i = 0; i < 100; i++)
				for (int j = 0; j < 100; j++)
				{
					dw[k][i][j] = (1 / minibatchSize) * dw[k][i][j];
					m[k][i][j] = 0.9 * m[k][i][j] + 0.1 * dw[k][i][j];
					v[k][i][j] = 0.999 * v[k][i][j] + 0.001 * dw[k][i][j] * dw[k][i][j];
					w[k][i][j] -= rate * m[k][i][j] / (Math.sqrt(v[k][i][j]) + 1e-8);
					dw[k][i][j] = 0;
				}
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 100; j++)
			{
				db[i][j] = (1 / minibatchSize) * db[i][j];
				mm[i][j] = 0.9 * mm[i][j] + 0.1 * db[i][j];
				vv[i][j] = 0.999 * vv[i][j] + 0.001 * db[i][j] * db[i][j];
				b[i][j] -= rate * mm[i][j] / (Math.sqrt(vv[i][j]) + 1e-8);
				db[i][j] = 0;
			}
	}
}