package pidr.app.views;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import pidr.app.Application;
import pidr.app.managers.ManagerGraph;
import pidr.app.managers.ManagerJProgressBar;


public class PanelHome extends AView {

	private JLabel msgLabel;

	public PanelHome() {
		
		setLayout(null);
		
		/**
		 * Error/success message
		 */
		msgLabel = new JLabel("Initialisation of the application...");
		msgLabel.setBounds((Application.getInstance().getjFrame().getWidth()/2)-(500/2), (Application.getInstance().getjFrame().getHeight()/2)-(29/2) + 80, 334, 29);
		add(msgLabel);
		
		try {
			BufferedImage myPicture = ImageIO.read(this.getClass().getResource("/pidr/img/logo.png"));
			JLabel image = new JLabel(new ImageIcon(myPicture));
			image.setBounds((Application.getInstance().getjFrame().getWidth()/2)-(500/2), (Application.getInstance().getjFrame().getHeight()/2)-(29/2) - 300, 500, 500);
			add(image);
		} catch (IOException e) {}
		
		/**
		 * Progress bar loading info
		 */
		ManagerJProgressBar progressBar = new ManagerJProgressBar(ManagerGraph.getInstance());
		progressBar.setBounds((Application.getInstance().getjFrame().getWidth()/2)-(500/2), (Application.getInstance().getjFrame().getHeight()/2)-(29/2) + 100, 500, 29);
		add(progressBar);
		
		revalidate();
	}

	public void update(Observable o, Object arg) {
		
	}
}
