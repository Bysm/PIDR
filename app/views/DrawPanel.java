package app.views;

import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import app.managers.DrawManager;
import app.managers.ManagerGraph;
import app.models.Arc;
import app.models.Node;

public class DrawPanel extends JPanel implements Observer {
	
	private static DrawPanel instance;
	private ArrayList<DrawSommet> drawSommets = new ArrayList<DrawSommet>();
	private DrawSommet currentSommet = null;
	
	public DrawPanel() {
		super();
		
		DrawPanel.instance = this;
		setLayout(null);

		ManagerGraph.getInstance().addObserver(this);
		DrawManager.getInstance().addObserver(this);
		
		DrawSommet drawSommet;
		for (Node sommet : ManagerGraph.getInstance().getSommets()) {
			drawSommet = new DrawSommet(sommet);
			drawSommet.addMouseMotionListener(drawSommet);
			drawSommet.addMouseListener(drawSommet);
			drawSommets.add(drawSommet);
			add(drawSommet);
		}
		
		DrawArc drawArc;
		for (Arc arc : ManagerGraph.getInstance().getArcs()) {
			drawArc = new DrawArc(arc);
			add(drawArc);
		}
	}
	
	@Override
	public void paint(Graphics g) {
		for (Component component : getComponents()) {
			component.paint(g);
		}
	}
	
	public ArrayList<DrawSommet> getDrawSommets() {
		return drawSommets;
	}

	public static DrawPanel getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

	public void update(Observable o, Object arg) {
		
		System.out.println(arg);
		
		if (arg.getClass().getSimpleName().equals("Sommet")) {
			DrawSommet drawSommet = new DrawSommet((Node) arg);
			drawSommet.addMouseMotionListener(drawSommet);
			drawSommet.addMouseListener(drawSommet);
			drawSommets.add(drawSommet);
			add(drawSommet);
			repaint();
		}
		
		else if (arg.getClass().getSimpleName().equals("Arc")) {
			DrawArc drawArc = new DrawArc((Arc) arg);
			add(drawArc);
			repaint();
		}
		
		else if (arg.toString().equals("MAJ_GRAPH")) {
			repaint();
		}
	}
}
