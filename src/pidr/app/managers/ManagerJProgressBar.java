package pidr.app.managers;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JProgressBar;

//Simply the progress bar on the loading screen
public class ManagerJProgressBar extends JProgressBar implements Observer {

	AManager am;
	
	public ManagerJProgressBar(AManager am) {
		super(0, 100);
		this.am = am;
		this.am.addObserver(this);
	    setVisible(true);       
	    setStringPainted(true);
	}
	
	public void update(Observable o, Object arg) {
		if (arg.toString().equals("MAJ_PROGRESS_LOAD")) {
			this.setValue(am.getProgress());
			this.repaint();
		}
	}
	
}
