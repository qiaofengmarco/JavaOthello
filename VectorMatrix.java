import jcuda.*;
import jcuda.jcublas.*;
import jcuda.runtime.*;
public class VectorMatrix
{
	public static double dot(double[] a, double[] b, int n)
	{
		double ans; 
		JCublas.cublasInit();
		Pointer pa = new Pointer();
		Pointer pb = new Pointer();
		JCublas.cublasAlloc(n, Sizeof.DOUBLE, pa);
		JCublas.cublasAlloc(n, Sizeof.DOUBLE, pb);
		JCublas.cublasSetVector(n, Sizeof.DOUBLE, Pointer.to(a), 1, pa, 1);
		JCublas.cublasSetVector(n, Sizeof.DOUBLE, Pointer.to(b), 1, pb, 1);
		ans = JCublas.cublasDdot(n, pa, 1, pb, 1);
		JCublas.cublasFree(pa);
		JCublas.cublasFree(pb);
		JCublas.cublasShutdown();
		return ans;
	}
	public static double[] mulMatrixVector(double[] a, int n, double[] b, int m, char op)
	{
		double[] ans = new double[n];
		JCublas.cublasInit();
		Pointer da = new Pointer();
		Pointer db = new Pointer();
		Pointer dc = new Pointer();
		JCublas.cublasAlloc(n * m, Sizeof.DOUBLE, da);
		JCublas.cublasAlloc(m, Sizeof.DOUBLE, db);	
		JCublas.cublasAlloc(n, Sizeof.DOUBLE, dc);
		if (op == 't')
			JCublas.cublasSetMatrix(n, m, Sizeof.DOUBLE, Pointer.to(a), n, da, n);
		else
			JCublas.cublasSetMatrix(m, n, Sizeof.DOUBLE, Pointer.to(a), m, da, m);			
		JCublas.cublasSetVector(m, Sizeof.DOUBLE, Pointer.to(b), 1, db, 1);
		JCublas.cublasSetVector(n, Sizeof.DOUBLE, Pointer.to(ans), 1, dc, 1);
		JCublas.cublasDgemv(op, n, m, 1, da, n, db, 1, 0, dc, 1);
		JCublas.cublasGetVector(n, Sizeof.DOUBLE, dc, 1, Pointer.to(ans), 1);
		JCublas.cublasFree(da);
		JCublas.cublasFree(db);
		JCublas.cublasFree(dc);
		JCublas.cublasShutdown();
		return ans;
	}
	public static double[] mulVectorVector(double[] a, int n, double[] b, int m)
	{
		double[] ans = new double[n * m];
		JCublas.cublasInit();
		Pointer da = new Pointer();
		Pointer db = new Pointer();
		Pointer dc = new Pointer();
		JCublas.cublasAlloc(n, Sizeof.DOUBLE, da);
		JCublas.cublasAlloc(m, Sizeof.DOUBLE, db);	
		JCublas.cublasAlloc(n * m, Sizeof.DOUBLE, dc);
		JCublas.cublasSetVector(n, Sizeof.DOUBLE, Pointer.to(a), 1, da, 1);
		JCublas.cublasSetVector(m, Sizeof.DOUBLE, Pointer.to(b), 1, db, 1);
		JCublas.cublasDger(m, n, 1, db, 1, da, 1, dc, m);
		JCublas.cublasGetMatrix(m, n, Sizeof.DOUBLE, dc, m, Pointer.to(ans), m);
		JCublas.cublasFree(da);
		JCublas.cublasFree(db);
		JCublas.cublasFree(dc);
		JCublas.cublasShutdown();
		return ans;
	}
	public static double[] addMatrix(double[] a, double[] b, int n, int m, double alpha, double beta)
	{
		double[] ans = new double[n * m];
		JCublas.cublasInit();
		Pointer da = new Pointer();
		Pointer db = new Pointer();
		Pointer dc = new Pointer();	
		JCuda.cudaMalloc(da, n * m * Sizeof.DOUBLE);
		JCuda.cudaMalloc(db, n * m * Sizeof.DOUBLE);
		JCuda.cudaMalloc(dc, n * m * Sizeof.DOUBLE);
		JCuda.cudaMemcpy(da, Pointer.to(a), n * m * Sizeof.DOUBLE, cudaMemcpyKind.cudaMemcpyHostToDevice);
		JCuda.cudaMemcpy(db, Pointer.to(b), n * m * Sizeof.DOUBLE, cudaMemcpyKind.cudaMemcpyHostToDevice);
		JCublas2.cublasSetMatrix(m, n, Sizeof.DOUBLE, Pointer.to(a), m, da, m);
		JCublas2.cublasSetMatrix(m, n, Sizeof.DOUBLE, Pointer.to(b), m, db, m);
		cublasHandle handle = new cublasHandle();
		JCublas2.cublasCreate(handle);
		JCublas2.cublasDgeam(handle, cublasOperation.CUBLAS_OP_N, cublasOperation.CUBLAS_OP_N, m, n, Pointer.to(new double[]{alpha}), da, m, Pointer.to(new double[]{beta}), db, m, dc, m);
		JCublas2.cublasGetMatrix(n, m, Sizeof.DOUBLE, dc, n, Pointer.to(ans), n);
		JCublas.cublasFree(da);
		JCublas.cublasFree(db);
		JCublas.cublasFree(dc);
		JCublas.cublasShutdown();
		return ans;		
	}
}