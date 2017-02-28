package pidr.app.managers;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import pidr.app.Application;
import pidr.app.models.Arc;
import pidr.app.models.Graph;
import pidr.app.models.Node;
import pidr.app.views.PanelGraph;
import pidr.app.views.components.panels.PanelSheet;

public class ManagerCSV extends AManager {
	
	private static ManagerCSV instance;
	
	public ManagerCSV() {
		instance = this;
	}
	
	public static synchronized ManagerCSV getInstance() {
		if (instance == null) {
			instance = new ManagerCSV();
	    }
		return instance;
	}
	
	/* 
	 * Export saves: 
	 * - the nodes and their position on the sheet
	 * - the arcs
	 * - custom colors but not modifications to the default ones (0 - 7)
	 */
	public void exportCsv() throws IOException {
		PanelSheet panel = ((PanelGraph) Application.getInstance().getjFrame().getContentPane()).getPanelSheet();
		
		String path = "./" + ManagerGraph.getInstance().getGraph().getName() + ".csv";
		File file = new File(path);
		boolean alreadyExists = file.exists();
		
		if (alreadyExists) {
			file.delete();
		}

		CsvWriter writer = new CsvWriter(new FileWriter(path), ';');
		
		{
			writer.writeRecord(new String[]{"Graph"});
			writer.writeRecord(new String[]{ManagerGraph.getInstance().getGraph().getName()});
			writer.writeRecord(new String[]{""});
			writer.writeRecord(new String[]{"Nodes"});
			writer.writeRecord(new String[]{"ID","Name","X","Y"});
			Set<Node> nodes = panel.getNodeDraws().keySet();
			for (Node node : nodes) {
				writer.writeRecord(new String[]{String.valueOf(node.getId()),node.getName(),
						String.valueOf(panel.getNodeDraws().get(node).getX()),String.valueOf(panel.getNodeDraws().get(node).getY())});
			}
			writer.writeRecord(new String[]{""});
			writer.writeRecord(new String[]{"Arcs"});
			writer.writeRecord(new String[]{"ID","headId","tailId","directed","color"});
			Set<Arc> arcs = panel.getArcDraws().keySet();
			for (Arc arc : arcs) {
				writer.writeRecord(new String[]{String.valueOf(arc.getId()),String.valueOf(arc.getHeadNode().getId()),
						String.valueOf(arc.getTailNode().getId()),String.valueOf(arc.isDirected()),String.valueOf(arc.getColor())});
			}
			HashMap<Integer, Color> colorMap = ManagerGraph.getInstance().getColors();
			if(colorMap.size() > 0) {
				writer.writeRecord(new String[]{""});
				writer.writeRecord(new String[]{"Colors"});
				writer.writeRecord(new String[]{"ID","red","green","blue"});
				for (Entry<Integer, Color> entry : colorMap.entrySet()) {
					//if(entry.getKey() > 7) {
						Color color = entry.getValue();
						writer.writeRecord(new String[]{String.valueOf(entry.getKey()), 
								String.valueOf(color.getRed()), String.valueOf(color.getGreen()), 
								String.valueOf(color.getBlue())});
					//}
				}
			}
		}
		writer.close();
		
	}
	
	//Imports a graph csv file and creates/fills in matlab the corresponding dynGraph variable
	public HashMap<Node, int[]> importCsv(String name) throws Exception {
		
		String path = "./" + name + ".csv";
		CsvReader reader = new CsvReader(path, ';');
		
		boolean end = false;
		String[] headers;
		Graph graph = null;
		HashMap<Node, int[]> nodeDraws = new HashMap<Node, int[]>();
		while(!end) {
			reader.readHeaders();
			headers = reader.getHeaders();
			if(headers[0].equals("Graph")) {
				reader.readRecord();
				graph = new Graph(reader.get("Graph"));
				ManagerMatlab.getInstance().createGraph(reader.get("Graph"));
				reader.skipRecord();
			} else if(headers[0].equals("Nodes")) {
				reader.readHeaders();
				while(reader.readRecord() && reader.getColumnCount() > 1 && !reader.get("ID").equals("")) {
					//Possible problem with the encoding, so we replace Â if we find it
					String nom = reader.get("Name").replace("Â", "");
					Node node = new Node(Integer.valueOf(reader.get("ID")),nom);
					graph.addNode(node);
					nodeDraws.put(node, new int[]{Integer.valueOf(reader.get("X")),Integer.valueOf(reader.get("Y"))});
				}
			 }else if(headers[0].equals("Arcs")) {
				reader.readHeaders();
				while(reader.readRecord() && reader.getColumnCount() > 1) {
					Arc arc = new Arc(Integer.valueOf(reader.get("ID")),
							graph.getNode(Integer.valueOf(reader.get("headId"))),graph.getNode(Integer.valueOf(reader.get("tailId"))),
							Boolean.valueOf(reader.get("directed")),Integer.valueOf(reader.get("color")));
					graph.addArc(arc);
				}
				if (reader.readRecord() == false) {
					end = true;
				} else {
					reader.readHeaders();
					while(reader.readRecord() && reader.getColumnCount() > 1) {
						int ID = Integer.valueOf(reader.get("ID"));
						Color color = new Color(Integer.valueOf(reader.get("red")),
								Integer.valueOf(reader.get("green")), 
								Integer.valueOf(reader.get("blue")));
						ManagerGraph.getInstance().addColor(ID, color);
					}
					end = true;
				}
			 }
		}
		ManagerGraph.getInstance().setGraph(graph);
		return nodeDraws;
	}
}
