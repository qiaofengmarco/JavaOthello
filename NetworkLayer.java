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
	public void backward(double[] pre_delta, int act_type, boolean calc_delta)
	{
		if (calc_delta)
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
			delta = VectorMatrix.mulMatrixVector(w, pre_nodeNum, pre_delta, nodeNum, 'n');
			for (int i = 0; i < pre_nodeNum; i++)
				delta[i] *= deInput[i];
		}
		//for (int j = 0; j < nodeNum; j++)
			//System.out.printf("%f ", pre_delta[j]);
		//System.out.println();		
		w_grad = VectorMatrix.mulVectorVector(pre_delta, nodeNum, in, pre_nodeNum);
		//for (int j = 0; j < pre_nodeNum * nodeNum; j++)
			//System.out.printf("%f ", w_grad[j]);
		//System.out.println();
		b_grad = pre_delta;
	}
	public void backwardOut(double[] t, int act_type, int act_type2)
	{
		double[] d = new double[nodeNum];
		System.out.printf("%f\n", out[0]);
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
		
		//for (int j = 0; j < nodeNum; j++)
			//System.out.printf("%f ", d[j]);
		//System.out.println();		
		
		w_grad = VectorMatrix.mulVectorVector(d, nodeNum, in, pre_nodeNum);
		b_grad = d;
		
		double[] deInput = new double[pre_nodeNum];
		if (act_type2 == 0)
		{
			for (int i = 0; i < pre_nodeNum; i++)
				deInput[i] = 1;
		}
		else if (act_type2 == 1)
		{
			for (int i = 0; i < pre_nodeNum; i++)
				deInput[i] = Activator.dRelu(in[i]);
		}
		else if (act_type2 == 2)
		{
			for (int i = 0; i < pre_nodeNum; i++)
				deInput[i] = Activator.dLRelu(rate, in[i]);
		}
		else if (act_type2 == 3)
		{
			for (int i = 0; i < pre_nodeNum; i++)
				deInput[i] = Activator.dSigmoid(in[i]);
		}
		
		// delta = (W^T * pre_delta) * deOut
		delta = VectorMatrix.mulMatrixVector(w, pre_nodeNum, d, nodeNum, 'n');
		for (int i = 0; i < pre_nodeNum; i++)
			delta[i] *= deInput[i];		
		
		//for (int j = 0; j < nodeNum; j++)
			//System.out.printf("%f ", delta[j]);
		//System.out.println();	
	}
}