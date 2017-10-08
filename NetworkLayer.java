public class NetworkLayer
{
	public double[] out, delta, in, b, w, w_grad, b_grad;
	public int pre_nodeNum = 1, nodeNum = 1;
	public double rate = 0.25;
	public NetworkLayer(int n1, int n2)
	{
		pre_nodeNum = n1;
		nodeNum = n2;
		
		in = new double[n1];
		delta = new double[n1];
		
		b = new double[n2];
		out = new double[n2];
		w = new double[n2 * n1];
		w_grad = new double[n2 * n1];
		b_grad = new double[n2];
	}
	public void forward(double[] a, int act_type)
	{
		in = a;
		
		//W * x
		out = VectorMatrix.mulMatrixVector(w, nodeNum, in, pre_nodeNum, 't');
		
		//Activate
		if (act_type > 0)
		{
			if (act_type == 1)
			{
				for (int i = 0; i < nodeNum; i++)
					out[i] = Activator.Relu(out[i] + b[i]);
			}
			else if (act_type == 2)
			{
				for (int i = 0; i < nodeNum; i++)
					out[i] = Activator.LRelu(rate, out[i] + b[i]);
			}
			else if (act_type == 3)
			{
				for (int i = 0; i < nodeNum; i++)
					out[i] = Activator.Sigmoid(out[i] + b[i]);
			}
		}
		else
		{
			for (int i = 0; i < nodeNum; i++)
				out[i] += b[i];
		}
	}
	public void backward(double[] pre_delta, int act_type)
	{
		double[] deInput = new double[pre_nodeNum];
		if (act_type == 0)
		{
			for (int i = 0; i < pre_nodeNum; i++)
				deInput[i] = 1;
		}
		else if (act_type == 1)
		{
			for (int i = 0; i < pre_nodeNum; i++)
				deInput[i] = Activator.dRelu(in[i]);
		}
		else if (act_type == 2)
		{
			for (int i = 0; i < pre_nodeNum; i++)
				deInput[i] = Activator.dLRelu(rate, in[i]);
		}
		else if (act_type == 3)
		{
			for (int i = 0; i < pre_nodeNum; i++)
				deInput[i] = Activator.dSigmoid(in[i]);
		}
		
		// delta = (W^T * pre_delta) * deOut
		delta = VectorMatrix.mulMatrixVector(w, nodeNum, pre_delta, nodeNum, 'n');
		for (int i = 0; i < pre_nodeNum; i++)
			delta[i] *= deInput[i];
		
		w_grad = VectorMatrix.mulVectorVector(pre_delta, nodeNum, in, pre_nodeNum);
		b_grad = pre_delta;
	}
	public void backwardOut(double[] t, int act_type)
	{
		double[] d = new double[nodeNum];
		if (act_type == 0)
		{
			for (int i = 0; i < nodeNum; i++)
				d[i] = t[i] - out[i];
		}
		else if (act_type == 1)
		{
			for (int i = 0; i < nodeNum; i++)
				d[i] = Activator.dRelu(out[i]) * (t[i] - out[i]);
		}
		else if (act_type == 2)
		{
			for (int i = 0; i < nodeNum; i++)
				d[i] = Activator.dLRelu(rate, out[i]) * (t[i] - out[i]);
		}
		else if (act_type == 3)
		{
			for (int i = 0; i < nodeNum; i++)
				d[i] = Activator.dSigmoid(out[i]) * (t[i] - out[i]);
		}
		
		w_grad = VectorMatrix.mulVectorVector(delta, nodeNum, in, pre_nodeNum);
		b_grad = d;
		
		delta = VectorMatrix.mulMatrixVector(w, nodeNum, d, nodeNum, 'n');
	}
}