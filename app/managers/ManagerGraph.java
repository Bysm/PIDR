package app.managers;

import app.models.Graph;

public class ManagerGraph extends AManager implements IManagerConnector {
	
	private static ManagerGraph instance = null;
	private static Graph graph;
	
	private ManagerGraph() {}
	
	public static synchronized ManagerGraph getInstance() {
		if (instance == null) {
			instance = new ManagerGraph();
	    }
	    return instance;
	}
	//
	public void setGraph(Graph graph) {
		ManagerGraph.graph = graph;
	}
	
	public Graph getGraph() {
		return graph;
	}

}
