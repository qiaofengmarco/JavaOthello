import java.io.*;
import java.util.*;
public class VectorDNN
{
	private NetworkLayer[] layer;
	private int layers = 1;
	private double learning_rate = 0.01;
	private String path;
	private boolean finish = false;
	public VectorDNN(String name)
	{
		double kk;
		path = name;
		layer = new NetworkLayer[4];

		layer[0] = new NetworkLayer(64, 16);
		layer[1] = new NetworkLayer(16, 16);
		layer[2] = new NetworkLayer(16, 16);
		layer[3] = new NetworkLayer(16, 1);
		layers = 4;

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
					kk = Math.random() * 0.04 + 0.001;
					layer[i].w[j] = kk;
					handler.writeDouble(layer[i].w[j]);
				}
				for (int j = 0; j < layer[i].nodeNum; j++)
				{
					kk = Math.random() * 0.005 + 0.0001;
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
		layer[0].forward(x, 3);
		for (int i = 1; i < layers; i++)
			layer[i].forward(layer[i - 1].out, 3);
		ans = layer[layers - 1].out;
		return ans;
	}
	public void backward(double[] x, double[] t)
	{
		forward(x);
		layer[layers - 1].backwardOut(t, 3, 3);
		for (int i = layers - 2; i >= 1; i--)
			layer[i].backward(layer[i + 1].delta, 3, true);
		layer[0].backward(layer[1].delta, 3, false);
	}
	public double evaluate(double[][] x, double[][] tag, int size)
	{
		double error = 0;
		double[] predict;
		for (int i = 0; i < size; i++)
		{
			predict = forward(x[i]);
			for (int j = 0; j < layer[layers - 1].nodeNum; j++)
				if (Math.abs(predict[j] - tag[i][j]) > 0.01)
				{
					error += 1;
					break;
				}
		}
		return error / size;
	}
	public void minibatchSGD(double[][] x, double[][] tag, int batchSize)
	{
		double[][] w_grad_tot = new double[4][];
		double[][] b_grad_tot = new double[4][];
		
		for (int i = 1; i < 3; i++)
		{
			w_grad_tot[i] = new double[16 * 16];
			b_grad_tot[i] = new double[16];
		}
		b_grad_tot[0] = new double[16];
		w_grad_tot[0] = new double[64 * 16];
		b_grad_tot[3] = new double[1];
		w_grad_tot[3] = new double[16];
		
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
	public void SGD(double[] x, double[] tag)
	{
		double[][] w_grad_tot = new double[4][];
		double[][] b_grad_tot = new double[4][];
		
		for (int i = 1; i < 3; i++)
		{
			w_grad_tot[i] = new double[16 * 16];
			b_grad_tot[i] = new double[16];
		}
		b_grad_tot[0] = new double[16];
		w_grad_tot[0] = new double[64 * 16];
		b_grad_tot[3] = new double[1];
		w_grad_tot[3] = new double[16];
		
		backward(x, tag);
		for (int i = 0; i < layers; i++)
		{
			w_grad_tot[i] = VectorMatrix.addMatrix(w_grad_tot[i], layer[i].w_grad, layer[i].nodeNum, layer[i].pre_nodeNum, 1.0, 1.0);
			b_grad_tot[i] = VectorMatrix.addMatrix(b_grad_tot[i], layer[i].b_grad, layer[i].nodeNum, 1, 1.0, 1.0);
			layer[i].w_grad = VectorMatrix.addMatrix(layer[i].w_grad, w_grad_tot[i], layer[i].nodeNum, layer[i].pre_nodeNum, 1.0, -learning_rate);
			layer[i].b_grad = VectorMatrix.addMatrix(layer[i].b_grad, b_grad_tot[i], layer[i].nodeNum, 1, 1.0, -learning_rate);
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
	public void sampling(double[][] batch1, double[][] batch2, double[][] ans1, double[][] ans2)
	{
		int count = 0, temp;
		HashSet<Integer> set = new HashSet<Integer>();
		while (set.size() < 100)
		{
			temp = (int)(Math.random() * 100);
			set.add(new Integer(temp));
		}
		for (Integer i : set)
		{
			ans1[count] = batch1[i.intValue()];
			ans2[count] = batch2[i.intValue()];
			count++;
		}
	}
	public void train()
	{
		double[][] x = new double[100][64];
		double[][] tag = new double[100][1];
		//double[][] x1 = new double[100][64];
		//double[][] tag1 = new double[100][1];
		double sum;
		for (int i = 0; i < 100; i++)
		{
			sum = 0;
			for (int j = 0; j < 64; j++)
			{
				x[i][j] = Math.random();
				sum += x[i][j];
			}
			sum /= 64;
			tag[i][0] = sum;
		}
		//sampling(x, tag, x1, tag1);
		for (int i = 0; i < 100; i++)
			SGD(x[i], tag[i]);
		System.out.printf("Error rate: %f\n", evaluate(x, tag, 100));
		store();
	}
	public static void main(String[] args)
	{
		VectorDNN dnn = new VectorDNN("test.txt");
		for (int i = 0; i < 50; i++)
		{
			System.out.printf("Round %d:\n", i + 1);
			dnn.train();
		}
	}
}