import java.io.*;
import java.util.*;
public class IOHandler
{
	private FileInputStream fi;
	private FileOutputStream fo;
	private DataInputStream oi;
	private DataOutputStream oo;
	private String fileName;
	private File f;
	public IOHandler(String name)
	{
		fileName = name;
	}
	public void openFileRead()
	{
		f = new File(fileName);
		if (!f.exists())
		{
			try 
			{
				f.createNewFile();
			}
			catch(IOException e){}
		}
		try 
		{
			fi = new FileInputStream(fileName);
			oi = new DataInputStream(fi);
		}
		catch(IOException e){}
	}
	public void openFileWrite()
	{
		f = new File(fileName);
		if (!f.exists())
		{
			try 
			{
				f.createNewFile();
			}
			catch(IOException e){}
		}
		try 
		{
			fo = new FileOutputStream(fileName);
			oo = new DataOutputStream(fo);
		}
		catch(IOException e){}
	}
	public void closeAll()
	{
		if (fi != null)
		{
			try
			{
				oi.close();
				fi.close();
				oi = null;
				fi = null;
			}
			catch(IOException e){}
		}
		if (fo != null)
		{
			try
			{
				oo.close();
				fo.close();
				oo = null;
				fo = null;
			}
			catch(IOException e){}
		}
	}
	public void flush()
	{
		try
		{
			oo.flush();
		}
		catch(IOException e){}
	}
	public void writeDouble(double d)
	{
		try 
		{
			oo.writeDouble(d);
		}
		catch (IOException e){}
	}
	public double readDouble()
	{
		double d = 0;
		try 
		{
			d = oi.readDouble();
		}
		catch (EOFException e)
		{
			closeAll();
			return Double.MIN_NORMAL;
		}
		catch (IOException e){}
		return d;
	}
	/*
	public static void main(String[] args)
	{
		IOHandler handler = new IOHandler("test.txt");
		handler.openFileWrite();
		handler.writeDouble(0.21);
		handler.writeDouble(0.22);
		handler.flush();
		handler.closeAll();
		double a, b;
		handler.openFileRead();
		a = handler.readDouble();
		b = handler.readDouble();
		System.out.printf("%2f %2f\n", a, b);
		handler.closeAll();
	}*/
}