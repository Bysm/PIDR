package app.views;

import java.awt.Dialog;
import java.awt.Event;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import matlabcontrol.MatlabInvocationException;
import app.Application;
import app.managers.ManagerGraph;
import app.managers.ManagerMatlab;
import app.models.Node;

public class ButtonAddSommet extends JButton implements MouseListener {
	public ButtonAddSommet() {
		super("Ajouter un sommet");
	}

	public void mouseClicked(MouseEvent e) {
		try {
			ManagerMatlab.getInstance().getProxy().eval("g.Symbol="+10);
			Object symbol = ManagerMatlab.getInstance().getProxy().getVariable("g.Symbol");
			double actual = ((double[]) symbol)[0];
			System.out.println("Symbol: "+ actual);
		} catch (MatlabInvocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*
		JOptionPane popup = new JOptionPane();
		String nom = popup.showInternalInputDialog(Application.getInstance().getAppFrame().getContentPane(), "Entrer un nom :", "Définition du nom du sommet", JOptionPane.INFORMATION_MESSAGE);
		GraphManager.getInstance().addSommet(new Sommet(nom));*/
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}
