package pidr.app.views.components.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pidr.app.draws.ADraw;
import pidr.app.draws.DrawArc;
import pidr.app.draws.DrawNode;
import pidr.app.managers.ManagerGraph;
import pidr.app.models.Arc;
import pidr.app.models.Node;
import pidr.app.views.PanelGraph;

//The panel where the graph is drawn. Contains the graphic representations of 
//the graph, nodes and arcs, as well as any other necessary data, like the zoom ratio. 
public class PanelSheet extends JPanel implements Observer, MouseMotionListener, MouseListener, KeyListener {
	
	private ArrayList<ADraw> aDraws = new ArrayList<ADraw>();
	private HashMap<Node, DrawNode> nodeDraws = new HashMap<Node, DrawNode>();
	private HashMap<Arc, DrawArc> arcDraws = new HashMap<Arc, DrawArc>();
	private DrawArc arcTemp = null;
	private static int zoom = 100;
	
	public static int getZoom() {
		return zoom;
	}
	
	public HashMap<Arc, DrawArc> getArcDraws() {
		return arcDraws;
	}
	
	public PanelSheet() {
		super();
		ManagerGraph.getInstance().addObserver(this);
		
		setBackground(Color.WHITE);
		
		// Absolute layout
		setLayout(null);
		
		/**
		 * We get the graphs' data
		 * - Nodes
		 * - Arcs
		 */
		ADraw aDraw;
		for (Node node : ManagerGraph.getInstance().getGraph().getNodes()) {
			aDraw = new DrawNode(node);
			aDraws.add(aDraw);
			add(aDraw);
			nodeDraws.put(node, (DrawNode) aDraw);
		}
		
		for (Arc arc : ManagerGraph.getInstance().getGraph().getArcs()) {

			int num = 0;
			for (int i = 0; i < aDraws.size(); i++) {
				if (aDraws.get(i) instanceof DrawArc) {
					DrawArc da = (DrawArc) aDraws.get(i);
					if (da.getArc().getHeadNode().equals(arc.getHeadNode()) && da.getArc().getTailNode().equals(arc.getTailNode())
							|| da.getArc().getHeadNode().equals(arc.getTailNode()) && da.getArc().getTailNode().equals(arc.getHeadNode())) {
						num++;
					}
				}
			}
			
			aDraw = new DrawArc(arc, nodeDraws.get(arc.getHeadNode()), nodeDraws.get(arc.getTailNode()), num);
			aDraws.add(aDraw);
			add(aDraw);
			arcDraws.put(arc, (DrawArc) aDraw);
		}
		
		setPreferredSize(new Dimension(4000, 4000));
		
		/**
		 * Listener of Add node
		 */
		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);
	}

	
	public HashMap<Node, DrawNode> getNodeDraws() {
		return nodeDraws;
	}
	
	public void setNodeDraws(HashMap<Node, DrawNode> nodeDraws) {
		this.nodeDraws = nodeDraws;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		
		for (Component component : getComponents()) {
			component.paint(g);
		}
	}
	
	/**
	 * Update function
	 */
	
	public void majGraphArc() {
		ADraw aDraw;

		/**
		 * If a new arc has been created
		 */
		for (Arc arc : ManagerGraph.getInstance().getGraph().getArcs()) {
			if (!arcDraws.containsKey(arc)) {
				
				int num = 0;
				for (Entry<Arc, DrawArc> entry : arcDraws.entrySet()) {
					DrawArc da = entry.getValue();
					if (da.getArc().getHeadNode().equals(arc.getHeadNode()) && da.getArc().getTailNode().equals(arc.getTailNode())
							|| da.getArc().getHeadNode().equals(arc.getTailNode()) && da.getArc().getTailNode().equals(arc.getHeadNode())) {
						num++;
					}
				}
				
				aDraw = new DrawArc(arc, nodeDraws.get(arc.getHeadNode()), nodeDraws.get(arc.getTailNode()), num);
				aDraws.add(aDraw);
				add(aDraw);
				arcDraws.put(arc, (DrawArc) aDraw);
			}
		}
		
		/**
		 * If an arc has been deleted
		 */
		Set<Entry<Arc, DrawArc>> entries = arcDraws.entrySet();
		ArrayList<Arc> removes = new ArrayList<Arc>();
		for (Entry<Arc, DrawArc> entry : entries) {
			if (!ManagerGraph.getInstance().getGraph().getArcs().contains(entry.getKey())) {
				int num = arcDraws.get(entry.getKey()).getNum();
				for (Entry<Arc, DrawArc> entry1 : entries) {
					if (((entry1.getKey().getHeadNode().equals(entry.getKey().getHeadNode()) && entry1.getKey().getTailNode().equals(entry.getKey().getTailNode()))
							|| (entry1.getKey().getHeadNode().equals(entry.getKey().getTailNode()) && entry1.getKey().getTailNode().equals(entry.getKey().getHeadNode())))
							&& num<entry1.getValue().getNum()) {
						entry1.getValue().setNum(entry1.getValue().getNum()-1);
					}
				}
				removes.add(entry.getKey());
			}
		}
		for (Arc arc : removes) {
			remove(arcDraws.get(arc));
			arcDraws.remove(arc);
		}
		
		//We redraw the panel after any modification
		repaint();
	}

	//If a node has been added/deleted
	public void majGraphNode() {
		ADraw aDraw;
		for (Node node : ManagerGraph.getInstance().getGraph().getNodes()) {
			if (!nodeDraws.containsKey(node)) {
				aDraw = new DrawNode(node);
				aDraws.add(aDraw);
				add(aDraw);
				nodeDraws.put(node, (DrawNode) aDraw);
			}
		}
		
		Set<Entry<Node, DrawNode>> entries = nodeDraws.entrySet();
		ArrayList<Node> removes = new ArrayList<Node>();
		for (Entry<Node, DrawNode> entry : entries) {
			if (!ManagerGraph.getInstance().getGraph().getNodes().contains(entry.getKey())) {
				remove(nodeDraws.get(entry.getKey()));
				removes.add(entry.getKey());
			}
		}
		
		for (Node node : removes) {
			nodeDraws.remove(node);
		}
		
		repaint();
	}

	//If we are adding an arc
	public void update(Observable o, Object arg) {
		//Start
		if (arg.equals("ADD_TEMP_ARC")) {
			ManagerGraph mg = ManagerGraph.getInstance();
			arcTemp = new DrawArc(mg.getArc(), nodeDraws.get(mg.getArc().getHeadNode()), null, 0);
			if (arcTemp != null) {
				remove(arcTemp);
			}
			add(arcTemp);
			repaint();
		}
		//End
		else if (arg.equals("DEL_TEMP_ARC")) {
			if (arcTemp != null) {
				remove(arcTemp);
				repaint();	
			}
		}
	}

	//If an arc is being selected -> repaint in red
	public void mouseOnArc(Arc arc) {
		for (Entry<Arc, DrawArc> entry : arcDraws.entrySet()) {
			if (entry.getKey() == arc) {
				entry.getValue().setHover(true);
				repaint();
			}
		}
	}

	public void mouseOutArc(Arc arc) {
		for (Entry<Arc, DrawArc> entry : arcDraws.entrySet()) {
			if (entry.getKey() == arc) {
				entry.getValue().setHover(false);
				repaint();
			}
		}
	}

	public void unselectedDrawNode(Node node) {
		for (Entry<Node, DrawNode>  entry : nodeDraws.entrySet()) {
			if (entry.getKey() != node) {
				entry.getValue().setSelected(false);
			}
		}
		repaint();
	}

	public void mouseDragged(MouseEvent e) {

	}

	//If we are creating an arc -> repaint so arc follows mouse
	public void mouseMoved(MouseEvent e) {
		if (ManagerGraph.getInstance().isModeAddArc() && ManagerGraph.getInstance().getArc() != null) {
			repaint();
		}
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		if (ManagerGraph.getInstance().isModeAddNode()) {
			JOptionPane popup = new JOptionPane();
			String nodeName = popup.showInternalInputDialog(PanelGraph.getInstance(), "Enter node name", "Add node", JOptionPane.INFORMATION_MESSAGE);
			try {
				if (nodeName.length()==0) {
					throw new Exception("node name empty");
				}
				ADraw aDraw;
				Node node = ManagerGraph.getInstance().addNodeByName(nodeName);
				node.setName(nodeName);
				aDraw = new DrawNode(node, e.getX(), e.getY());
				aDraws.add(aDraw);
				add(aDraw);
				nodeDraws.put(node, (DrawNode) aDraw);
				repaint();
			} catch (Exception e1) {
				popup.showMessageDialog(PanelGraph.getInstance(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
	
	}
	
	public static void setZoom(int zoom) {
		PanelSheet.zoom = zoom;
		PanelGraph.getInstance().repaint();
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		
	}

	public void keyReleased(KeyEvent e) {
		System.out.println(e.getKeyCode() + " " + KeyEvent.VK_A);
		if (e.getKeyCode() == KeyEvent.VK_A) {
			System.out.println("A");
			setZoom(getZoom()+1);
		}
		if (e.getKeyCode() == KeyEvent.VK_W) {
			System.out.println("W");
			if (getZoom()>1) {
				setZoom(getZoom()-1);
			}
		}
	}
	
	
	
}
