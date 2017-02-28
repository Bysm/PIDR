package app.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import matlabcontrol.MatlabInvocationException;
import app.managers.DrawManager;
import app.managers.ManagerMatlab;
import app.models.Node;

public class DrawSommet extends JComponent implements MouseListener, MouseMotionListener {
	
	private Node sommet;
	private boolean selected = false;

	public DrawSommet(Node sommet) {
		this.sommet = sommet;
	}

	@Override
	public void paint(Graphics g) {
		setSize(50, 50);
		g.setColor(Color.BLACK);
		//g.drawOval(x, y, width, height);
		if (selected) {
			g.drawRect(getX(), getY(), getWidth(), getHeight());
		} else g.drawOval(getX(), getY(), getWidth(), getHeight());
		
		g.setColor(Color.BLACK);
		g.drawString(sommet.getNom(), getX(), getY()+getHeight() + 20);
	}

	public void mouseClicked(MouseEvent e) {
		System.out.println("J'ai cliqué sur un sommet");
		if (DrawManager.getInstance().addSommetNewArc(sommet)) {
			selected = !selected;
		}
		
		DrawPanel.getInstance().repaint();
		/*try {
			MatlabManager.getInstance().putValueInt("sommet_" + sommet.getId(), sommet.getId());
		} catch (MatlabInvocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
	}
	
	public Node getSommet() {
		return sommet;
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		setLocation(getX() + e.getX() - getHeight()/2, getY() + e.getY() - getWidth()/2);
		DrawPanel.getInstance().repaint();
	}

	public void mouseMoved(MouseEvent e) {
	}
	
}
