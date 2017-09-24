import java.util.*;
public class UTCAI extends AI
{
	private Map<UTCTreeNode, Double> Q = new HashMap<UTCTreeNode, Double>();
	private Map<UTCTreeNode, Integer> N = new HashMap<UTCTreeNode, Integer>();
	public UTCAI(int h)
	{
		super(h);
	}
	public UTCAI(int x[], int h)
	{
		super(x, h);
	}
	private UTCTreeNode TreePolicy(UTCTreeNode v)
	{
		while (!Board.terminal(v.data))
		{
			if (v.next[0] == -1)
				return null;
			if (v.expand < v.next[0])
				return Expand(v);
			else
				v = BestChild(v, 1 / Math.sqrt(2));
		}
		return v;
	}
	private UTCTreeNode Expand(UTCTreeNode v)
	{
		if (v.next[0] == 0) return v;
		v.expand++;
		int action = v.next[v.expand];
		int[] next = Board.nextState(v.data, action, hold);
		return v.addChild(next);
	}
	private UTCTreeNode BestChild(UTCTreeNode v, double c)
	{
		double max = -2147483647, temp = 0, qv, nv, nvthis;
		UTCTreeNode[] anstemp = new UTCTreeNode[v.children.size()];
		UTCTreeNode ans = null;
		int count = 0;
		if ((v.isLeaf()) || (v.next[0] == 0)) return null;
		if (v.next[0] == 1) return v.children.get(0);
		ans = v.children.get(0);
		nvthis = N.get(v).doubleValue();
		for (int i = 1; i <= v.next[0]; i++)
		{
			qv = Q.get(v.children.get(i - 1)).doubleValue();
			nv = N.get(v.children.get(i - 1)).doubleValue();
			temp = qv / nv + c * Math.sqrt(2 * Math.log(nvthis) / nv);
			if (temp > max)
			{
				max = temp;
				count = 1;
				anstemp[0] = v.children.get(i);
			}
			else if (temp == max)
			{
				count++;
				anstemp[count - 1] = v.children.get(i);
			}
		}
		if (count == 1)
			ans = anstemp[0];
		else
			ans = anstemp[(int)(Math.random() * count)];
		return ans;
	}
	private int BestChildAction(UTCTreeNode v, double c)
	{
		double max = -2147483647, temp = 0, qv, nv, nvthis;
		int[] anstemp = new int[64];
		int ans;
		int count = 0;
		if ((v.isLeaf()) || (v.next[0] == 0)) return -1;
		if (v.next[0] == 1) return v.next[1];
		ans = v.next[1];
		nvthis = N.get(v).doubleValue();
		for (int i = 1; i <= v.next[0]; i++)
		{
			qv = Q.get(v.children.get(i - 1)).doubleValue();
			nv = N.get(v.children.get(i - 1)).doubleValue();
			temp = qv / nv + c * Math.sqrt(2 * Math.log(nvthis) / nv);
			if (temp > max)
			{
				max = temp;
				count = 1;
				anstemp[0] = v.next[i];
			}
			else if (temp == max)
			{
				count++;
				anstemp[count - 1] = v.next[i];
			}
		}
		if (count == 1)
			ans = anstemp[0];
		else
			ans = anstemp[(int)(Math.random() * count)];
		return ans;
	}
	private double DefaultPolicy(int[] s)
	{
		int action = 0, now = hold;
		int[] next1, next2;
		while (!Board.terminal(s))
		{
			if (now == hold)
			{
				next1 = Board.searchNext(s, hold);
				if (next1[0] > 0)
				{
					action = next1[(int)(Math.random() * next1[0]) + 1];
					s = Board.nextState(s, action, hold);
				}
				now = -hold;
			}
			else
			{
				next2 = Board.searchNext(s, -hold);
				if (next2[0] > 0)
				{
					action = next2[(int)(Math.random() * next2[0]) + 1];
					s = Board.nextState(s, action, -hold);
				}
				now = hold;				
			}
		}
		return 100 * Board.getWinner(s) * hold;			
	}
	private void BackupNegaMax(UTCTreeNode v, double delta)
	{
		while (v != null)
		{
			if (N.containsKey(v))
				N.put(v, new Integer(N.get(v).intValue() + 1));
			else 
				N.put(v, new Integer(1));
			if (Q.containsKey(v))
				Q.put(v, new Double(Q.get(v).doubleValue() + delta));
			else
				Q.put(v, new Double(delta));
			delta = -delta;
			v = v.parent;
		}
	}
	private int UTCSearch(int[] s)
	{
		UTCTreeNode v = new UTCTreeNode(s, hold);
		UTCTreeNode v1;
		double delta;
		if (v.next[0] == 0) return -1;
		while (v.expand < v.next[0])
		{
			v1 = TreePolicy(v);
			if (v1 == null) break;
			//System.out.printf("%d %d\n", v.expand, v.next[0]);
			//System.out.println("aaa");
			delta = DefaultPolicy(v1.data);
			//System.out.println("bbb");
			BackupNegaMax(v1, delta);
		}
		//System.out.println("ccc");
		return BestChildAction(v, 0);
	}
	@Override
	public int move()
	{
		int[] steps = new int[65];
		int step;
		steps = table.nextSteps(hold);
		if (steps[0] > 0)
		{
			if (steps[0] == 1)
				step = steps[1];
			else
				step = UTCSearch(table.getTable());
			table.set(step / 8 + 1, step % 8 + 1, hold);			
			return step;
		}
		return -1;		
	}
}