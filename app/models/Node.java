package app.models;

public class Node {
	private int id;
	private String name;
	
	public Node(int id) {
		new Node(id, "Node n°" + id);
	}

	public Node(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getname() {
		return name;
	}
	
	public void setname(String name) {
		this.name = name;
	}
	
	
}
