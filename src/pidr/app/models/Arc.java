package pidr.app.models;

//Arc is a synonym of edge. This is to distinguish it from the matlab graph object
public class Arc {
	
	private int id;
	private Node headNode;
	private Node tailNode;
	private boolean directed;
	//color is actually a number as it corresponds to a "family" of arcs.
	private int color;
	
	public Arc(int id, Node headNode, Node tailNode, boolean directed, int color) {
		this.id = id;
		this.headNode = headNode;
		this.tailNode = tailNode;
		this.directed = directed;
		this.color = color;
	}
	
	public Arc(int id, Node headNode, Node tailNode, boolean directed) {
		this(id, headNode, tailNode, directed, 0);
	}
	
	public Arc(int id, Node headNode, Node tailNode) {
		this(id, headNode, tailNode, false, 0);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Node getHeadNode() {
		return headNode;
	}

	public void setHeadNode(Node headNode) {
		this.headNode = headNode;
	}

	public Node getTailNode() {
		return tailNode;
	}

	public void setTailNode(Node tailNode) {
		this.tailNode = tailNode;
	}

	public boolean isDirected() {
		return directed;
	}

	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Arc [id=" + id + ", headNode=" + headNode + ", tailNode=" + tailNode + ", directed=" + directed
				+ ", color=" + color + "]";
	}

}
