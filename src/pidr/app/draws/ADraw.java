package pidr.app.draws;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

public abstract class ADraw extends JComponent implements MouseListener, MouseMotionListener {
	
	public ADraw() {
		addMouseListener(this);
		addMouseMotionListener(this);
	}
}
