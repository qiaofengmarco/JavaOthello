import java.util.ArrayList;

public class UTCTreeNode{
    public ArrayList<UTCTreeNode> children = new ArrayList<UTCTreeNode>();
    public UTCTreeNode parent = null;
    public int[] data = new int[64];
	public int expand = 0;
	public int[] next = new int[65];
	public int hold;
	
    public UTCTreeNode(int[] data, int h) {
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
		UTCTreeNode a = (UTCTreeNode) o;
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
	
    public ArrayList<UTCTreeNode> getChildren() {
        return children;
    }
	
    public void setParent(UTCTreeNode parent) {
        this.parent = parent;
    }
    public void addChild(UTCTreeNode child) {
        child.setParent(this);
        this.children.add(child);
    }
    public UTCTreeNode addChild(int[] data) {
        UTCTreeNode child = new UTCTreeNode(data, hold);
        child.setParent(this);
        this.children.add(child);
		return child;
    }

    public int[] getData() {
		int[] ans = new int[64];
		for (int i = 0; i < 64; i++)
			ans[i] = this.data[i];
        return ans;
    }

    public void setData(int[] data) {
		for (int i = 0; i < 64; i++)
			this.data[i] = data[i];
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