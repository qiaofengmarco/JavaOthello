import java.io.*;
import java.util.*;
public class VectorDNN
{
	private NetworkLayer[] layer;
	private int layers = 1;
	private double learning_rate = 0.02;
	private String path;
	public VectorDNN(String name)
	{
		double kk;
		path = name;
		layer = new NetworkLayer[6];

		layer[0] = new NetworkLayer(64, 16);
		layer[1] = new NetworkLayer(16, 16);
		layer[2] = new NetworkLayer(16, 16);
		layer[3] = new NetworkLayer(16, 16);
		layer[4] = new NetworkLayer(16, 16);
		layer[5] = new NetworkLayer(16, 1);
		layers = 6;

		File f = new File(path);
		if (!f.exists())
		{
			try 
			{
				f.createNewFile();
			}
			catch(IOException e){}
			IOHandler handler = new IOHandler(path);
			handler.openFileWrite();
			for (int i = 0; i < layers; i++)
			{
				for (int j = 0; j < layer[i].pre_nodeNum * layer[i].nodeNum; j++)
				{
					kk = Math.random() + 0.001;
					layer[i].w[j] = kk;
					handler.writeDouble(layer[i].w[j]);
				}
				for (int j = 0; j < layer[i].nodeNum; j++)
				{
					kk = Math.random() + 0.001;
					layer[i].b[j] = kk;
					handler.writeDouble(layer[i].b[j]);
				}
			}
			handler.flush();
			handler.closeAll();
		}
		else
			load();
	}
	public double[] forward(double[] x)
	{
		double[] ans;
		layer[0].in = x;
		layer[0].forward(x, 2);
		for (int i = 1; i < layers - 1; i++)
			layer[i].forward(layer[i - 1].out, 2);
		layer[layers - 1].forward(layer[layers - 2].out, 0);
		ans = layer[layers - 1].out;
		return ans;
	}
	public void backward(double[] x, double[] t)
	{
		forward(x);
		layer[layers - 1].backwardOut(t, 0);
		for (int i = layers - 2; i >= 0; i--)
			layer[i].backward(layer[i + 1].delta, 2);
	}
	public double evaluate(double[][] x, double[][] tag, int size)
	{
		double error = 0;
		double[] predict;
		for (int i = 0; i < size; i++)
		{
			predict = forward(x[i]);
			for (int j = 0; j < layer[layers - 1].nodeNum; j++)
				if (predict[j] != tag[i][j])
				{
					error += 1;
					break;
				}
		}
		return error / size;
	}
	public double calcError(double[] y, double[] tag, int size)
	{
		double error = 0;
		for (int i = 0; i < size; i++)
			error += 0.5 * Math.pow(y[i] - tag[i], 2);
		error = error / size;
		return error;
	}
	public boolean checkGradient(double[] x, double[] tag, int size)
	{
		double[] y;
		double epsilon = 0.000001, e1, e2, expected;
		y = forward(x);
		for (int i = 0; i < layers; i++)
		{
			for (int j = 0; j < layer[i].pre_nodeNum * layer[i].nodeNum; j++)
			{
				System.out.printf("%d %d\n", i, j);
				layer[i].w[j] += epsilon;
				e1 = calcError(forward(x), tag, size);
				layer[i].w[j] -= 2 * epsilon;
				e2 = calcError(forward(x), tag, size);
				expected = (e2 - e1) / (2 * epsilon);
				if (Math.abs(expected - layer[i].w_grad[j]) > 0.000001)
				{
					System.out.printf("%f\n", Math.abs(expected - layer[i].w_grad[j]));
					return false;
				}
			}
		}
		return true;
	}
	public void minibatchSGD(double[][] x, double[][] tag, int batchSize)
	{
		double[][] w_grad_tot = new double[6][];
		double[][] b_grad_tot = new double[6][];
		
		for (int i = 1; i < 5; i++)
		{
			w_grad_tot[i] = new double[16 * 16];
			b_grad_tot[i] = new double[16];
		}
		b_grad_tot[0] = new double[16];
		w_grad_tot[0] = new double[64 * 16];
		b_grad_tot[5] = new double[1];
		w_grad_tot[5] = new double[16];
		
		for (int k = 0; k < batchSize; k++)
		{
			backward(x[k], tag[k]);
			for (int i = 0; i < layers; i++)
			{
				w_grad_tot[i] = VectorMatrix.addMatrix(w_grad_tot[i], layer[i].w_grad, layer[i].nodeNum, layer[i].pre_nodeNum, 1, 1.0 / batchSize);
				b_grad_tot[i] = VectorMatrix.addMatrix(b_grad_tot[i], layer[i].b_grad, layer[i].nodeNum, 1, 1, 1.0 / batchSize);
			}
		}
		for (int i = 0; i < layers; i++)
		{
			layer[i].w_grad = VectorMatrix.addMatrix(layer[i].w_grad, w_grad_tot[i], layer[i].nodeNum, layer[i].pre_nodeNum, 1.0 - learning_rate * 0.01, -learning_rate);
			layer[i].b_grad = VectorMatrix.addMatrix(layer[i].b_grad, b_grad_tot[i], layer[i].nodeNum, 1, 1, -learning_rate);
		}
	}
	public void store()
	{
		IOHandler handler = new IOHandler(path);
		handler.openFileWrite();
		for (int i = 0; i < layers; i++)
		{
			for (int j = 0; j < layer[i].pre_nodeNum * layer[i].nodeNum; j++)
				handler.writeDouble(layer[i].w[j]);
			for (int j = 0; j < layer[i].nodeNum; j++)
				handler.writeDouble(layer[i].b[j]);
		}
		handler.flush();
		handler.closeAll();	
	}
	public void load()
	{
		IOHandler handler = new IOHandler(path);
		handler.openFileRead();
		for (int i = 0; i < layers; i++)
		{
			for (int j = 0; j < layer[i].pre_nodeNum * layer[i].nodeNum; j++)
				layer[i].w[j] = handler.readDouble();
			for (int j = 0; j < layer[i].nodeNum; j++)
				layer[i].b[j] = handler.readDouble();
		}
		handler.closeAll();			
	}
	public static void main(String[] args)
	{
		VectorDNN dnn = new VectorDNN("test.txt");
		double[] x = new double[64];
		double[] tag = new double[1];
		int[] a = Board.getInit();
		for (int i = 0; i < 64; i++)
			x[i] = (double) a[i];
		tag[0] = 0.5;
		if (dnn.checkGradient(x, tag, 1))
			System.out.println("Successful!");
		else
			System.out.println("Failed!");
	}
}