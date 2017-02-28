package pidr.app.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import pidr.app.Application;
import pidr.app.managers.ManagerCSV;
import pidr.app.managers.ManagerGraph;
import pidr.app.models.Node;
import pidr.app.views.components.others.JMenuEditGraph;
import pidr.app.views.components.panels.PanelManageComponent;
import pidr.app.views.components.panels.PanelManageSheet;
import pidr.app.views.components.panels.PanelSheet;

//The main panel of the program 
public class PanelGraph extends AView {

	private PanelManageSheet panelManageSheet;
	private PanelSheet panelSheet;
	private JScrollPane panelScroll;
	private PanelManageComponent panelManageComponent;
	private static PanelGraph instance = null;
	
	public static PanelGraph getInstance() {
		if (instance == null) {
			return new PanelGraph();
		}
		return instance;
	}

	public PanelSheet getPanelSheet() {
		return panelSheet;
	}
	
	public PanelGraph() {
		super();
		instance = this;
		
		setLayout(new BorderLayout());
	
		// ManagerGraph manages the panelGraph
		ManagerGraph.getInstance().addObserver(this);
		
		//Creates the menu bar
		JMenuBar menuBar = new JMenuBar();
		final JMenuEditGraph menuEditGraph = new JMenuEditGraph(this);		
		menuBar.add(menuEditGraph);
		
		/**
		 * Export csv menu
		 */
		JMenu menuIE = new JMenu("Import/Export");
		JMenuItem menuImport = new JMenuItem("Import csv");
		menuImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane popup = new JOptionPane();
				String name = popup.showInternalInputDialog(menuEditGraph.getParent(), "Enter graph name","Name", JOptionPane.INFORMATION_MESSAGE);
				try {
					HashMap<Node, int[]> nD = ManagerCSV.getInstance().importCsv(name);
					panelSheet = new PanelSheet();
				    for (Node node : nD.keySet()) {
				    	int[] coord = nD.get(node);
						panelSheet.getNodeDraws().get(node).setLocation(coord[0], coord[1]);
					}
				    remove(panelScroll);
				    panelScroll = new JScrollPane(panelSheet, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
					add(panelScroll, BorderLayout.CENTER);
					revalidate();
					repaint();
				} catch (Exception e1) {
					popup.showMessageDialog(getParent(), e1.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		menuIE.add(menuImport);
		JMenuItem menuExport = new JMenuItem("Export csv");
		menuExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ManagerCSV.getInstance().exportCsv();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} 
		});
		
		menuIE.add(menuExport);
		menuBar.add(menuIE);
		
		Application.getInstance().getjFrame().setJMenuBar(menuBar);
		
		//EAST: panel to edit a node and its' arcs
		panelManageComponent = new PanelManageComponent(this);
		add(panelManageComponent, BorderLayout.EAST);
		setBorder(BorderFactory.createEmptyBorder());

		//NORTH: buttons to switch between edit modes
		panelManageSheet = new PanelManageSheet(this);
		add(panelManageSheet, BorderLayout.NORTH);
		
		//CENTER: panelSheet (in a scroll panel) containing the draws for the graph
		panelSheet = new PanelSheet();
	    panelScroll = new JScrollPane(panelSheet, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(panelScroll, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
	
	//Shows or hide the east panel depending on whether we are editing a node/arcs
	public void showManageComponent(Node node) {
		panelSheet.unselectedDrawNode(node);
		panelManageComponent.showDetail(node);
	}
	
	public void hideManageComponent() {
		panelManageComponent.hideDetail();
	}

	//Update the draw of the graph. MAJ stands for update in French.
	public void update(Observable o, Object arg) {
		if (arg.equals("MAJ_GRAPH_ADD_NODE")) {
			panelSheet.majGraphNode();
		}
		else if (arg.equals("MAJ_GRAPH_ADD_ARC")) {
			panelManageComponent.hideDetail();
			panelSheet.majGraphArc();
		}
		else if (arg.equals("ERROR_ADD_NODE")) {
			System.out.println("erreur lors de l'ajout d'un node");
		}
		else if (arg.equals("MAJ_GRAPH_REMOVE_NODE")) {
			panelSheet.majGraphNode();
			panelSheet.majGraphArc();
		}
		else if (arg.equals("MAJ_GRAPH_REMOVE_ARC")) {
			panelSheet.majGraphArc();
			panelManageComponent.majDetail();
		}
	}
}
