package app.models;

import java.util.ArrayList;

public class Graph {
	
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private ArrayList<Arc> arcs = new ArrayList<Arc>();
	private String name;
	
	public void addArc(Arc arc) {
		arcs.add(arc);
	}
	
	public void removeArc(Arc arc) {
		arcs.remove(arc);
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	public void removeNode(Node node) {
		nodes.remove(node);
	}
	
	public String getname() {
		return name;
	}
	
	public void setname(String name) {
		this.name = name;
	}
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
	
	public ArrayList<Arc> getArcs() {
		return arcs;
	}
	
	public void setArcs(ArrayList<Arc> arcs) {
		this.arcs = arcs;
	}
	
	public void clearNodes() {
		this.nodes = new ArrayList<Node>();
	}
	
	public void clearArcs() {
		this.arcs = new ArrayList<Arc>();
	}
	
	public void clearGraph() {
		clearArcs();
		clearNodes();
	}
	
	

}
