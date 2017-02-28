package app.views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class HomePanel extends JPanel {
	
	private DrawPanel paint;
	private JPanel barre;
	
	private ButtonAddSommet bAddSommet;

	public HomePanel() {
		super(new BorderLayout());
		
		barre = new JPanel();
		bAddSommet = new ButtonAddSommet();
		bAddSommet.addMouseListener(bAddSommet);
		
		barre.add(bAddSommet);
		
		paint = new DrawPanel();

		add(barre, BorderLayout.NORTH);
		add(paint, BorderLayout.CENTER);
	}
}
