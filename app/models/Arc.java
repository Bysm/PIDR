package app.models;

public class Arc {
	
	private int id;
	private Node headNode;
	private Node tailNode;
	private boolean directed;
	private int color;
	
	public Arc(int id, Node headNode, Node tailNode, boolean directed, int color) {
		this.id = id;
		this.headNode = headNode;
		this.tailNode = tailNode;
		this.directed = directed;
		this.color = color;
	}
	
	public Arc(int ID, Node headNode, Node tailNode, boolean directed) {
		new Arc(ID, headNode, tailNode, directed, 0);
	}
	
	public Arc(int ID, Node headNode, Node tailNode) {
		new Arc(ID, headNode, tailNode, false, 0);
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Node getTeadNode() {
		return headNode;
	}
	
	public void setTeadNode(Node headNode) {
		this.headNode = headNode;
	}

	public Node getTailNode() {
		return tailNode;
	}
	
	public void setTailNode(Node tailNode) {
		this.tailNode = tailNode;
	}
	
	public boolean getDirected() {
		return this.directed;
	}
	
	public void setDirected(boolean directed) {
		this.directed = directed;
	}
	
	public int getColor() {
		return this.color;
	}
	
	public void setDirected(int color) {
		this.color = color;
	}
}
