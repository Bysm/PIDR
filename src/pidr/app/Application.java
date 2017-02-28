package pidr.app;

import java.awt.Dimension;

import javax.swing.JFrame;

import matlabcontrol.MatlabInvocationException;
import pidr.app.managers.ManagerGraph;
import pidr.app.managers.ManagerMatlab;
import pidr.app.models.Graph;
import pidr.app.views.PanelGraph;
import pidr.app.views.PanelHome;


public class Application {

	private int height = 800;
	private int width = 1024;
	private static Application instance;
	private JFrame jFrame;
	String startGraph = null;
	
	
	public Application() {
		new Application("Test");
	}
	
	
	public Application(String string) {
		System.out.println("DynGraph application Launched");
		System.out.println("Graph is :");
		System.out.println(string);
		startGraph = string;
		instance = this;
		
		/**
		 * Init dimension
		 */
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int heightScreen = (int)dimension.getHeight();
		int widthScreen  = (int)dimension.getWidth();
		
		/** 
		 * Turns on the frame of the program
		 */
		this.jFrame = new JFrame("Graph Editor - Initialisation");
		height = 600;
		width = 800;
		this.jFrame.setBounds((widthScreen/2)-(width/2), (heightScreen/2)-(height/2), width, height);
		//appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.jFrame.setVisible(true);
		this.jFrame.setResizable(true);
		
		/**
		 * Init of the panel
		 */
		this.jFrame.setContentPane(new PanelHome());
		this.jFrame.revalidate();
		
		ManagerGraph.getInstance().loadData();
		
		// Init matlab
		ManagerMatlab.getInstance();
		if(!startGraph.equals("Test")) {
			try {
				Graph graph = ManagerMatlab.getInstance().matlabToGraph(startGraph);
				ManagerGraph.getInstance().setGraph(graph);
			} catch (MatlabInvocationException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//Initialisation of the panels' content
		this.jFrame.setTitle("Graph Editor - Edition");
		height = 800;
		width = 1024;
		this.jFrame.setBounds((widthScreen/2)-(width/2), (heightScreen/2)-(height/2), width, height);
		
		this.jFrame.setContentPane(new PanelGraph());
		this.jFrame.revalidate();
		
	}
	
	public JFrame getjFrame() {
		return jFrame;
	}
	
	public static synchronized Application getInstance() {
		if (instance == null) {
			instance = new Application();
	    }
		return instance;
	}
	
	public static synchronized Application getInstance(String string) {
		if (instance == null) {
			instance = new Application(string);
	    }
		return instance;
	}
	
	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("No arguments");
			Application.getInstance();
		} else {
			System.out.println(args[0]);
			Application.getInstance(args[0]);
		}
	}


	public String getStartGraph() {
		return startGraph;
	}

}
