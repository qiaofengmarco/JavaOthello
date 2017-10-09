import java.util.ArrayList;

public class UCTTreeNode{
	public ArrayList<UCTTreeNode> children = new ArrayList<UCTTreeNode>();
	public UCTTreeNode parent = null;
	public int[] data = new int[64];
	public int expand = 0;
	public int[] next = new int[65];
	public int hold;
	
	public UCTTreeNode(int[] data, int h) 
	{
		for (int i = 0; i < 64; i++)
			this.data[i] = data[i];
		next = Board.searchNext(this.data, h);
	}
	
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (getClass() != o.getClass())
			return false;
		UCTTreeNode a = (UCTTreeNode) o;
		if (children == a.children)
			return true;
		if ((children == a.children) && (parent == a.parent) && (data == a.data) && (hold == a.hold))
			return true;
		return false;
	}
	public int hashCode()
	{
		return data.hashCode();
	}
	
	public void setParent(UCTTreeNode parent) 
	{
		this.parent = parent;
	}
	public void addChild(UCTTreeNode child) 
	{
		child.setParent(this);
		this.children.add(child);
	}
	public UCTTreeNode addChild(int[] data) 
	{
		UCTTreeNode child = new UCTTreeNode(data, hold);
		child.setParent(this);
		this.children.add(child);
		return child;
	}

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        if(this.children.size() == 0) 
            return true;
        else 
            return false;
    }
}