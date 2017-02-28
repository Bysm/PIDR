package app.managers;
pdfpodisdsq dqsdqs vgfsdgdf fqsdq

//A SUPPRIMER OU DEPLACER?

import java.util.ArrayList;
import java.util.Observable;

import app.models.Arc;
import app.models.Node;

public class DrawManager extends Observable {

	private static DrawManager instance = null;
	private ArrayList<Node> sommets = new ArrayList<Node>();
	
	public DrawManager() {
		instance = this;
	}
	
	public static DrawManager getInstance() {
		if (instance == null) {
			return new DrawManager();
		}
		return instance;
	}
	
	public boolean addSommetNewArc(Node s) {
		if (sommets.size()<2) {
			
			if (sommets.contains(s)) {
				sommets.remove(s);
			}
			else {
				sommets.add(s);
				
				if (sommets.size() == 2) {
					Arc a = new Arc(sommets.get(0), sommets.get(1));
					setChanged();
					notifyObservers(a);
					sommets.clear();
				}
				
				return true;
			}
		}
		
		return false;
	}
}
