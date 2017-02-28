package pidr.app.views.components.buttons;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import pidr.app.managers.ManagerGraph;
import pidr.app.views.PanelGraph;
import pidr.app.views.components.panels.PanelManageSheet;

public class ButtonDisplayColors extends JButton implements ActionListener {
	
	private Image myImage;
	private Image myImageHover;
	private int buttonSize = PanelManageSheet.getButtonSize();
	
	public ButtonDisplayColors() {
		super();
		
		// Display color numbers under arcs
		try {
			// I get the image
			BufferedImage myPicture = ImageIO.read(this.getClass().getResource("/pidr/img/arc_color.png"));
			// I resize it
			myImage = myPicture.getScaledInstance(buttonSize, buttonSize, java.awt.Image.SCALE_SMOOTH);

			myPicture = ImageIO.read(this.getClass().getResource("/pidr/img/arc_color_hide.png"));
			myImageHover = myPicture.getScaledInstance(buttonSize, buttonSize, java.awt.Image.SCALE_SMOOTH);
			
			majButton();
		} catch (IOException e) {}
		
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		ManagerGraph.getInstance().switchDisplayColors();
		majButton();
		PanelGraph.getInstance().repaint();
	}
	
	//Changes the image (background color) depending on whether it is clicked.
	private void majButton() {
		if (ManagerGraph.getInstance().isDisplayColors()) {
			setIcon(new ImageIcon(myImageHover));
		}
		else {
			setIcon(new ImageIcon(myImage));
		}
	}

}
