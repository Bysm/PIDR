package pidr.app.models;

import java.util.ArrayList;

import matlabcontrol.MatlabInvocationException;
import pidr.app.managers.ManagerMatlab;

public class Graph {
	
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private ArrayList<Arc> arcs = new ArrayList<Arc>();
	private String name = null;
	
	public Graph(ArrayList<Node> nodes, ArrayList<Arc> arcs, String name) {
		try {
			ManagerMatlab.getInstance().addArcs(this, arcs);
			ManagerMatlab.getInstance().addNodes(this, nodes);
		} catch (MatlabInvocationException e) {
			
		}
		this.nodes = nodes;
		this.arcs = arcs;
		this.name = name;
	}

	public Graph(String name) {
		this.name = name;
	}

	public void addArc(Arc arc) {
		try {
			ManagerMatlab.getInstance().addArc(this, arc);
			arcs.add(arc);
		} catch (MatlabInvocationException e) {
			 
		}
	}
	
	public void removeArc(Arc arc) {
		try {
			ManagerMatlab.getInstance().removeArc(this, arc);
			arcs.remove(arc);
		} catch (MatlabInvocationException e) {
			 
		}
	}
	
	public void addNode(Node node) throws Exception {
		if (isExistNode(node)) {
			throw new Exception("Ce noeud existe déjà !");
		}
		try {
			ManagerMatlab.getInstance().addNode(this, node);
			nodes.add(node);
		} catch (MatlabInvocationException e) {
			 
		}
	}
	
	private boolean isExistNode(Node leNode) {
		for (Node node : nodes) {
			if (node.getId() == leNode.getId()) {
				return true;
			}
		}
		return false;
	}

	public void removeNode(Node node) {
		ArrayList<Arc> arcsCopy = (ArrayList<Arc>) arcs.clone();
		for (Arc arc : arcsCopy) {
			if(arc.getHeadNode() == node || arc.getTailNode() == node) {
				removeArc(arc);
			}
		}
		try {
			ManagerMatlab.getInstance().removeNode(this, node);
			nodes.remove(node);
		} catch (MatlabInvocationException e) {
			 
		}
	}
	
	public void clearNodes() {
		try {
			ManagerMatlab.getInstance().clearNodes(this);
			this.nodes = new ArrayList<Node>();
		} catch (MatlabInvocationException e) {
			 
		}
	}
	
	public void clearArcs() {
		try {
			ManagerMatlab.getInstance().clearArcs(this);
			this.arcs = new ArrayList<Arc>();
		} catch (MatlabInvocationException e) {
			 
		}
	}
	
	public void clearGraph() {
		clearArcs();
		clearNodes();
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	public Node getNode(int Id) {
		ArrayList<Node> nodes = getNodes();
		Node node = null;
		for (int i = 0; i < nodes.size(); i++) {
			if(nodes.get(i).getId()==Id) {
				node = nodes.get(i);
			}
		}
		return node;
	}
	
	//Counts the number of arcs having node as head or tail (if both are true, an arc that has node as head and tail will count as 2)
	//Used to determine how many lines are connected to a node
	//NOTE: if the graph isn't directed it will count every arc connected to the node
	public int degree (Node node, boolean out, boolean in) {
		if(!out && !in)
			return 0;
		int count = 0;
		for (Arc arc : arcs) {
			if(out && arc.getHeadNode() == node || arc.getHeadNode() == node && arc.isDirected() == false)
				count++;
			if(in && arc.getTailNode() == node || arc.getTailNode() == node && arc.isDirected() == false)
				count++;
		}
		return count;
	}
	
	//Counts the number of arcs (node1, node2) between two nodes
	//If directed, the order matters.
	public int countArcs(Node node1, Node node2, boolean directed) {
		int count = 0;
		for (Arc arc : arcs) {
			if(directed) {
				if(arc.isDirected()  && arc.getHeadNode() == node1 && arc.getTailNode() == node2)
					count++;
				if(!arc.isDirected() && (arc.getHeadNode() == node1 && arc.getTailNode() == node2 
						|| arc.getHeadNode() == node2 && arc.getTailNode() == node1))
					count++;
			} else {
				if(arc.getHeadNode() == node1 && arc.getTailNode() == node2 
						|| arc.getHeadNode() == node2 && arc.getTailNode() == node1)
					count++;
			}
		}
		return count;
	}
	
	public int degree(Node node) {
		return degree(node, true, true);
	}

	public void setNodes(ArrayList<Node> nodes) {
		try {
			ManagerMatlab.getInstance().addNodes(this, nodes);
			this.nodes = nodes;
		} catch (MatlabInvocationException e) {
			 
		}
	}

	public ArrayList<Arc> getArcs() {
		return arcs;
	}

	public void setArcs(ArrayList<Arc> arcs) {
		try {
			ManagerMatlab.getInstance().addArcs(this, arcs);
			this.arcs = arcs;
		} catch (MatlabInvocationException e) {
			 
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		String oldName = this.name;
		try {
			ManagerMatlab.getInstance().renameGraph(name, oldName);
			this.name = name;
		} catch (MatlabInvocationException e) {
			 
		}
	}

	//Generate an ID for a node by looking for a "hole" in the list of ids.
	public int generateId() {
		ArrayList<Integer> nodeIds = new ArrayList<Integer>();
		int max = 1;
		for (Node node : nodes) {
			int id = node.getId();
			nodeIds.add(id);
			if(id > max) {
				max = id;
			}
		}
		for (int i = nodes.size()+1; i > 0; i--) {
			if(!nodeIds.contains(i)) {
				return i;
			}
		}
		return max+1;
	}
	
}
