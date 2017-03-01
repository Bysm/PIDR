package pidr.app.managers;
import java.util.ArrayList;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;
import pidr.app.models.Arc;
import pidr.app.models.Graph;
import pidr.app.models.Node;

/*
 * Uses the matlabcontrol package
 * Note that every single function editing the graph in the class dynGraph.m
 * has a "mirror" function here with the same name. Its role is to call the
 * matlab function to modify the java graph and matlab graph at the same time
 * and keep them perfectly synchronised.
 * See the commentaries on the dynGraph.m file for more details on each function
 */
public class ManagerMatlab extends AManager implements IManagerConnector {
	
	private static ManagerMatlab instance = null;
	private static MatlabTypeConverter processor = null;
	
	MatlabProxy proxy;
	
	private ManagerMatlab() {
		try {
			proxy = (new MatlabProxyFactory()).getProxy();
			//makes sure the link between matlab and program works
			proxy.setVariable("DynGraphGraphicLoaded", 1);
			processor = new MatlabTypeConverter(proxy);
			//PIDR_MODIF : changer la valeur d'instance
		} catch (MatlabConnectionException e1) {
			e1.printStackTrace();
		} catch (MatlabInvocationException e) {
			e.printStackTrace();
		}
	}
	
	public static ManagerMatlab getInstance() {
		if (ManagerMatlab.instance == null) {
			return new ManagerMatlab();
		}
		return instance;
	}
	
	
	public static void close() {
		ManagerMatlab.getInstance().getProxy().disconnect();
	}

	public MatlabProxy getProxy() {
		return proxy;
	}
	
	
	/* 
	 * Functions that try to modify Matlab 
	 * if fails, throws an exception. But shouldn't.
	*/
	
	public void createGraph(String name) throws MatlabInvocationException {
		proxy.eval(name + "= dynGraph()");
	}
	
	public void renameGraph(String newName, String oldName) throws MatlabInvocationException {
		proxy.eval(newName + "= " + oldName);
	}
	
	private void nodeCommon(Graph graph, Node node, String function) throws MatlabInvocationException {
		Object name = proxy.getVariable(graph.getName());
		proxy.feval(function, name, node.getId());
	}
	
	public void addNode(Graph graph, Node node) throws MatlabInvocationException {
		nodeCommon(graph, node, "addNode");
	}
	
	public void removeNode(Graph graph, Node node) throws MatlabInvocationException {
		nodeCommon(graph, node, "removeNode");
	}
	
	private void nodesCommon(Graph graph, ArrayList<Node> nodes, String function) throws MatlabInvocationException {
		Object name = proxy.getVariable(graph.getName());
		proxy.eval("tmp = [];");
		for (int i = 0; i < nodes.size(); i++) {
			proxy.eval("tmp = [tmp, " + nodes.get(i).getId() + "];");
		}
		Object tmp = proxy.getVariable("tmp");
		proxy.feval(function, name, tmp);
	}
	
	public void addNodes(Graph graph, ArrayList<Node> nodes) throws MatlabInvocationException {
		nodesCommon(graph, nodes, "addNode");
	}
	
	public void removeNodes(Graph graph, ArrayList<Node> nodes) throws MatlabInvocationException {
		nodesCommon(graph, nodes, "removeNode");
	}

	private void arcCommon(Graph graph, Arc arc, String function) throws MatlabInvocationException {
		Object name = proxy.getVariable(graph.getName());
		int directed = 0;
		if(arc.isDirected() == true) { directed = 1; }
		proxy.feval(function, name, arc.getId(), arc.getHeadNode().getId(), arc.getTailNode().getId(), directed, arc.getColor());
	}
	
	public void addArc(Graph graph, Arc arc) throws MatlabInvocationException {
		arcCommon(graph, arc, "addArc");
	}
	
	public void removeArc(Graph graph, Arc arc) throws MatlabInvocationException {
		Object name = proxy.getVariable(graph.getName());
		proxy.feval("removeArc", name, arc.getId());
	}
	
	public void addArcs(Graph graph, ArrayList<Arc> arcs) throws MatlabInvocationException {
		Object name = proxy.getVariable(graph.getName());
		proxy.eval("tmp = [];");
		Arc arc;
		int directed;
		for (int i = 0; i < arcs.size(); i++) {
			arc = arcs.get(i);
			directed = 0;
			if(arc.isDirected() == true) { directed = 1; }
			proxy.eval("tmp = [tmp; " + arc.getId() + "," + arc.getHeadNode().getId() + "," + arc.getTailNode().getId() + "," + directed + "," + arc.getColor() + "];");
		}
		Object tmp = proxy.getVariable("tmp");
		proxy.feval("addArcs", name, tmp, arcs.size());
	}
	
	public void removeArcs(Graph graph, ArrayList<Arc> arcs) throws MatlabInvocationException {
		Object name = proxy.getVariable(graph.getName());
		proxy.eval("tmp = [];");
		for (int i = 0; i < arcs.size(); i++) {
			proxy.eval("tmp = [tmp; " + arcs.get(i).getId() + "];");
		}
		Object tmp = proxy.getVariable("tmp");
		proxy.feval("removeArc", name, tmp, arcs.size());
	}
	
	public void clearNodes(Graph graph) throws MatlabInvocationException {
		Object name = proxy.getVariable(graph.getName());
		proxy.feval("clearNodes", name);
	}
	
	public void clearArcs(Graph graph) throws MatlabInvocationException {
		Object name = proxy.getVariable(graph.getName());
		proxy.feval("clearArcs", name);
	}
	
	public void clearGraph(Graph graph) throws MatlabInvocationException {
		clearArcs(graph);
		clearNodes(graph);
	}

	public int[] getNodesID(Graph graph) throws MatlabInvocationException {
		proxy.eval("tmp =" + graph.getName() +".nodes;");
		MatlabNumericArray matlabArray = processor.getNumericArray("tmp");
		double[][] nodesArray = matlabArray.getRealArray2D();
		if(nodesArray.length == 0) {
			return new int[0];
		}
		int[] nodes = new int[nodesArray[0].length];
		for (int i = 0; i < nodes.length; i++) {
			int j = (int) nodesArray[0][i];
			nodes[i] = j;
		}
		return nodes;
	}

	public void setNodes(Graph graph, ArrayList<Node> nodes) throws MatlabInvocationException {
		clearGraph(graph);
		addNodes(graph, nodes);
	}

	public int[][] getArcs(Graph graph) throws MatlabInvocationException {
		proxy.eval("tmp =" + graph.getName() +".arcs;");
		MatlabNumericArray matlabArray = processor.getNumericArray("tmp");
		double[][] arcsArray = matlabArray.getRealArray2D();
		if(arcsArray.length == 0) {
			return new int[0][0];
		}
		int[][] arcs = new int[arcsArray.length][arcsArray[0].length];
		for (int i = 0; i < arcs.length; i++) {
			for (int j = 0; j < arcs[0].length; j++) {
				int k = (int) arcsArray[i][j];
				arcs[i][j] = k;
			}
		}
		return arcs;
	}

	public void setArcs(Graph graph, ArrayList<Arc> arcs) throws MatlabInvocationException {
		clearArcs(graph);
		addArcs(graph, arcs);
	}
	
	//Creates a Java Graph from a Matlab dynGraph whose name is the parameter.
	public Graph matlabToGraph(String name) throws MatlabInvocationException,Exception {
		Graph graphJ = new Graph(name);
		int[] nodes = getNodesID(graphJ);
		clearNodes(graphJ);
		for (int i = 0; i < nodes.length; i++) {
			graphJ.addNode(new Node(nodes[i]));
		}
		int[][] arcs = getArcs(graphJ);
		int[] arc;
		clearArcs(graphJ);
		for (int i = 0; i < arcs.length; i++) {
			arc = arcs[i];
			if (!ManagerGraph.getInstance().getColors().containsKey(arc[4])) {
				ManagerGraph.generateColor(arc[4]);
			}
			graphJ.addArc(new Arc(arc[0], graphJ.getNode(arc[1]), graphJ.getNode(arc[2]), arc[3]==1, arc[4]));
		}
		return graphJ;
	}
	
	/*
	 * Creates a Matlab Graph from a Java Graph
	 * This function is actually never used since once you start importing
	 * a java graph it automatically creates the dynGraph variable in matlab
	 * and it fills it dynamically.
	 */
	
	public void graphToMatlab(Graph graph) throws MatlabInvocationException {
		createGraph(graph.getName());
		addNodes(graph, graph.getNodes());
		addArcs(graph, graph.getArcs());
	}
}
