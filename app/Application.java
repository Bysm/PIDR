package app;

import java.awt.Dimension;

import javax.swing.JFrame;

import matlabcontrol.MatlabInvocationException;
import app.managers.ManagerMatlab;
import app.views.HomePanel;


public class Application {

	private JFrame appFrame;
	private int height = 800;
	private int width = 1024;
	private static Application instance;
	
	public Application() {
		instance = this;
		
		/**
		 * Init dimension
		 */
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int heightScreen = (int)dimension.getHeight();
		int widthScreen  = (int)dimension.getWidth();
		
		/** 
		 * Démarrage de la frame du logiciel
		 */
		appFrame = new JFrame("EditorGraph");
		appFrame.setBounds((widthScreen/2)-(width/2), (heightScreen/2)-(height/2), width, height);
		//appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		appFrame.setVisible(true);
		appFrame.setResizable(false);
		
		/**
		 * Init du panel
		 */
		appFrame.setContentPane(new HomePanel());
		appFrame.revalidate();
		
		// Init matlab Manager
		//MatlabManager.getInstance();
		
	}
	
	public static Application getInstance() {
		return instance;
	}
	
	public JFrame getAppFrame() {
		return appFrame;
	}
	
	public static void main(String[] args) {
		new Application();
	}

}
