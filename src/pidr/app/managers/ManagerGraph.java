package pidr.app.managers;

import java.awt.Color;
import java.util.HashMap;

import matlabcontrol.MatlabInvocationException;
import pidr.app.Application;
import pidr.app.models.Arc;
import pidr.app.models.Graph;
import pidr.app.models.Node;

public class ManagerGraph extends AManager {
	
	private static ManagerGraph instance = null;
	private Graph graph;
	private boolean displayNames = true;
	private boolean displayColors = true;
	
	private boolean modeDeleteNode = false;
	private boolean modeAddNode = false;
	private boolean modeAddArc = false;
	private Arc arc = null;
	
	private boolean modeEditManageComponent = false;
	
	//Temporary arc that follows the mouse when we are adding arcs
	public void setArc(Arc arc) {
		if (arc == null) {
			setChanged();
			notifyObservers("DEL_TEMP_ARC");
		}
		this.arc = arc;
		if (arc != null) {
			setChanged();
			notifyObservers("ADD_TEMP_ARC");
		}
	}
	
	public Arc getArc() {
		return arc;
	}

	//Different "modes" represent if one of the buttons is activated -
	//if we are adding nodes, deleting nodes, adding arcs...
	public boolean isModeAddArc () {
		return modeAddArc;
	}
	
	public void toggleModeAddArc () {
		this.modeAddNode = false;
		this.modeDeleteNode = false;
		this.modeAddArc = !this.modeAddArc;
		setArc(null);
		setChanged();
		notifyObservers("SWITCH_MODE_OFF");
	}
	
	public boolean isModeEditManageComponent () {
		return modeEditManageComponent;
	}
	
	public void toggleModeEditManageComponent () {
		this.modeEditManageComponent = !this.modeEditManageComponent;
		setChanged();
		notifyObservers("SWITCH_MODE_MANAGE_COMPONENT");
	}
	
	public boolean isModeAddNode() {
		return modeAddNode;
	}
	
	public void toggleModeAddNode() {
		this.modeDeleteNode = false;
		this.modeAddArc = false;
		this.modeAddNode = !this.modeAddNode;
		setChanged();
		notifyObservers("SWITCH_MODE_OFF");
	}
	
	public void toggleModeDeleteNode() {
		this.modeAddNode = false;
		this.modeAddArc = false;
		this.modeDeleteNode = !this.modeDeleteNode;
		setChanged();
		notifyObservers("SWITCH_MODE_OFF");
	}

	public boolean isModeDeleteNode() {
		return modeDeleteNode;
	}
	
	private static HashMap<Integer, Color> colors = new HashMap<Integer, Color>();
	
	/**
	 * Init default colors
	 */
	static {
		colors.put(0, Color.BLACK);
		colors.put(1, Color.GREEN);
		colors.put(2, new Color(125,0,125)); //Used to be red, but red is needed elsewhere
		colors.put(3, Color.BLUE);
		colors.put(4, Color.ORANGE);
		colors.put(5, Color.MAGENTA);
		colors.put(6, Color.CYAN);
		colors.put(7, Color.PINK);
	}
	
	protected ManagerGraph() {
		
			//If we had no graph selected, it generates a default simple one
			if(Application.getInstance().getStartGraph().equals("Test")) {
			this.graph = new Graph("Test");
			try {
				ManagerMatlab.getInstance().createGraph("Test");
				ManagerMatlab.getInstance().clearGraph(this.graph);
			} catch (MatlabInvocationException e1) {
				
			}
			Node n1 = new Node(1);
			Node n2 = new Node(2);
			Node n3 = new Node(3);
			try {
				this.graph.addNode(n1);
				this.graph.addNode(n2);
				this.graph.addNode(n3);
				this.graph.addArc(new Arc(1, n1, n2, true, 1));
				this.graph.addArc(new Arc(2, n1, n3, false, 2));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static synchronized ManagerGraph getInstance() {
		if (instance == null) {
			instance = new ManagerGraph();
	    }
	    return instance;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	
	public void loadData() {
		int i = 0;
		while (true) {
			
			// Update Progressbar
			setProgress(++i);
			setChanged();
            notifyObservers("MAJ_PROGRESS_LOAD");
            
            /**
             *	Thread is blocked during data loading
             */
            try {
				Thread.currentThread().sleep((long) (Math.random()*30));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
            if (i == 100) {
				break;
			}
		}
	}

	//To add nodes
	public Node addNodeById(int nodeId) throws Exception {
		Node n = new Node(nodeId);
		this.graph.addNode(n);
		setChanged();
        notifyObservers("MAJ_BTN_ADD_NODE_DESACTIVATE");
		return n;
	}
	
	public Node addNodeByName(String nodeName) throws Exception {
		int id = this.graph.generateId();
		Node n = new Node(id, nodeName);
		this.graph.addNode(n);
		setChanged();
        notifyObservers("MAJ_BTN_ADD_NODE_DESACTIVATE");
		return n;
	}
	
	public void addArcAndNotify (Node n1, Node n2, boolean oriented, int color) {
		this.graph.addArc(new Arc(getIdArcAvailable(), n1, n2, oriented, color));
		setChanged();
        notifyObservers("MAJ_GRAPH_ADD_ARC");
	}
	
	public static HashMap<Integer, Color> getColors() {
		return colors;
	}

	public void deleteNode(Node node) {
		graph.removeNode(node);
		setChanged();
		notifyObservers("MAJ_GRAPH_REMOVE_NODE");
	}

	/*
	 * The color algorithm:
	 * - generates a color
	 * - checks if it is different enough from the other colors (minDist)
	 * - restarts if not.
	 * - after a certain number of iterations (iter), to avoid the program 
	 * 		crashing, it restarts with a minDist divided by 2 and iter at 0.
	 * It has been tested to generate at least 70 colors without memory overflow.
	 * Upper limit remains unknown.
	 */
	private static int minDist = 10000;
	private static int iter = 0;
	public static void generateColor(int color) {
		Color col = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
		boolean good = true;
		for (Color col2 : colors.values()) {
			 double distance = Math.pow((col.getRed() - col2.getRed()),2) + 
					 Math.pow((col.getGreen() - col2.getGreen()),2) + 
					 Math.pow((col.getBlue() - col2.getBlue()),2);
			    if(distance < minDist){
			        good = false;
			    }
			    if(Math.pow((col.getRed() - 255),2) + 
					 Math.pow((col.getGreen()),2) + Math.pow((col.getBlue()),2) < 1000) {
						 good = false;
				}
			    if(col.getRed() >= 200 && col.getGreen() >= 200 && col.getBlue() >= 200) {
							 good = false;
					}
		}
		if(good == true) {
			colors.put(color,col);
			System.out.println("color " + color + " : " + col.getRed() + " " +  col.getGreen() + " " + col.getBlue());
			iter = 0;
			minDist = 10000;
		} else {
			//iter is used to avoid endless looping
			if(iter < 50) {
				iter++;
				generateColor(color);
			} else {
				minDist /= 2;
				iter = 0;
				generateColor(color);
			}
		}
	}
	
	public static void addColor(Integer colId, Color color) {
		colors.put(colId, color);
	}
	
	//Generates an ID for a new arc
	public int getIdArcAvailable() {
		int id = 0;
		boolean failed;
	
		while (true) {
			failed = false;
			for (Arc arc : graph.getArcs()) {
				if (id == arc.getId()) {
					id++;
					failed = true;
					break;
				}
			}
			if (!failed) {
				return id;
			}
		}
	}

	public void deleteArc(Arc arc) {
		graph.removeArc(arc);
		setChanged();
		notifyObservers("MAJ_GRAPH_REMOVE_ARC");
	}
		
	public boolean isDisplayNames() {
		return displayNames;
	}
	
	public void setDisplayNames(boolean bool) {
		displayNames = bool;
	}
	
	public void switchDisplayNames() {
		displayNames = !displayNames;
	}
	
	public boolean isDisplayColors() {
		return displayColors;
	}
	
	public void setDisplayColors(boolean bool) {
		displayColors = bool;
	}
	
	public void switchDisplayColors() {
		displayColors = !displayColors;
	}


}
