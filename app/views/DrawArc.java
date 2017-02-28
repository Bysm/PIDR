package app.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import app.models.Arc;

public class DrawArc extends JComponent {

	private Arc arc;

	public DrawArc(Arc arc) {
		this.arc = arc;
	}
	
	@Override
	public void paint(Graphics g) {
		setSize(50, 50);
		g.setColor(Color.BLACK);
		DrawSommet db = null, df = null;
		for (Component componant : getParent().getComponents()) {
			if (componant.getClass().getSimpleName().equals("DrawSommet")) {
				DrawSommet ds = (DrawSommet) componant;
				if (ds.getSommet() == arc.getSommetDebut()) {
					db = ds;
				}
				else if (ds.getSommet() == arc.getSommetFin()) {
					df = ds;
				}
			}
		}
		g.drawLine(db.getX(), db.getY(), df.getX(), df.getY());
	}
	

}
