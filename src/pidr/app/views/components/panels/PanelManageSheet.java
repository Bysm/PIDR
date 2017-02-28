package pidr.app.views.components.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pidr.app.views.PanelGraph;
import pidr.app.views.components.buttons.ButtonAddArc;
import pidr.app.views.components.buttons.ButtonAddNode;
import pidr.app.views.components.buttons.ButtonDelNode;
import pidr.app.views.components.buttons.ButtonDisplayColors;
import pidr.app.views.components.buttons.ButtonDisplayNames;

//Sheet containing the buttons to edit the graph.
public class PanelManageSheet extends JPanel {

	private PanelGraph panelGraph;
	private ButtonAddNode btnAddNode;
	private ButtonDelNode btnDelNode;
	private ButtonAddArc btnAddArc;
	private ButtonDisplayNames btnDisplayNames;
	private ButtonDisplayColors btnDisplayColors;
	//Size of the buttons
	private static int buttonSize = 32;
	
	public PanelGraph getPanelGraph() {
		return panelGraph;
	}

	//Adds all the necessary buttons to edit the graph (+ logo)
	public PanelManageSheet(PanelGraph panelGraph) {
		super();
		
		this.panelGraph = panelGraph;
		setLayout(new BorderLayout());
		JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		btnAddNode = new ButtonAddNode();
		btnDelNode = new ButtonDelNode();
		btnAddArc = new ButtonAddArc();
		btnDisplayNames = new ButtonDisplayNames();
		btnDisplayColors = new ButtonDisplayColors();
		jPanel.add(btnAddNode);
		jPanel.add(btnDelNode);
		jPanel.add(btnAddArc);
		jPanel.add(btnDisplayNames);
		jPanel.add(btnDisplayColors);
		
		add(jPanel, BorderLayout.CENTER);
		
		try {
			BufferedImage myPicture = ImageIO.read(this.getClass().getResource("/pidr/img/logo.png"));
			Image myImage = myPicture.getScaledInstance(buttonSize*5/4, buttonSize, java.awt.Image.SCALE_SMOOTH);
			JLabel lblLogo = new JLabel(new ImageIcon(myImage));
			lblLogo.setBorder(new EmptyBorder(0, 0, 0, 10));
			add(lblLogo, BorderLayout.EAST);
		} catch (IOException e) {}
	}

	public static int getButtonSize() {
		return buttonSize;
	}

	
	

}
