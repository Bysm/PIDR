package pidr.app.views.components.buttons;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import pidr.app.managers.ManagerGraph;
import pidr.app.views.components.panels.PanelManageSheet;

public class ButtonDelNode extends JButton implements ActionListener, Observer {

	private Image myImage;
	private Image myImageHover;
	private int buttonSize = PanelManageSheet.getButtonSize();
	
	public ButtonDelNode() {
		super();
		ManagerGraph.getInstance().addObserver(this);
		
		// Del node
		try {
			// I get the image
			BufferedImage myPicture = ImageIO.read(this.getClass().getResource("/pidr/img/node_del.png"));
			// I resize it
			myImage = myPicture.getScaledInstance(buttonSize, buttonSize, java.awt.Image.SCALE_SMOOTH);
			
			myPicture = ImageIO.read(this.getClass().getResource("/pidr/img/node_del_hover.png"));
			myImageHover = myPicture.getScaledInstance(buttonSize, buttonSize, java.awt.Image.SCALE_SMOOTH);
			
			setIcon(new ImageIcon(myImage));
		} catch (IOException e) {}
		
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		ManagerGraph.getInstance().toggleModeDeleteNode();
	}
	
	//Changes the image (background color) depending on whether it is clicked.
	private void majButton() {
		if (ManagerGraph.getInstance().isModeDeleteNode()) {
			setIcon(new ImageIcon(myImageHover));
		}
		else {
			setIcon(new ImageIcon(myImage));
		}
	}
	
	
	
	public void update(Observable o, Object arg) {
		if (arg.equals("SWITCH_MODE_OFF")) {
			majButton();
		}
	}
	
}
