package pidr.app.views.components.others;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pidr.app.managers.ManagerGraph;
import pidr.app.views.PanelGraph;
import pidr.app.views.components.panels.PanelColor;
import pidr.app.views.components.panels.PanelSheet;

//The edit menu you can find in the top left corner
public class JMenuEditGraph extends JMenu {

	public JMenuEditGraph(final JPanel panel) {
		super("Edit");
		
		//Rename the graph
		JMenuItem renameGraph = new JMenuItem("Rename graph");
		renameGraph.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JOptionPane popup = new JOptionPane();
				String name = popup.showInternalInputDialog(getParent(), "New name :", "Rename", JOptionPane.INFORMATION_MESSAGE);
				
				try {
					if(name != null && !name.equals("")) {
						ManagerGraph.getInstance().getGraph().setName(name);
					} else {
						popup.showMessageDialog(getParent(), "Error : invalid name");
					}
				} catch (Exception e1) {
					popup.showMessageDialog(getParent(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		this.add(renameGraph);
		this.setVisible(true);
		
		//Edit a color
		JMenuItem changeColor = new JMenuItem("Modify a color");
		
		changeColor.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				PanelColor popup = new PanelColor();
				if(popup.getColor() < 0) {
					System.out.println("Invalid color ID");
					return;
				} else if(popup.getRed() < 0 || popup.getGreen() < 0 || popup.getBlue() < 0) {
					return;
				}
				int color = popup.getColor();
				int red = popup.getRed();
				int green = popup.getGreen();
				int blue = popup.getBlue();
				
				try {
					if(color >= 0 && red >= 0 &&  blue >= 0 && green >= 0) {
						ManagerGraph.getInstance().addColor(color, new Color(red,green,blue));
						PanelGraph.getInstance().repaint();
					} else {
						popup.showMessageDialog(getParent(), "Error : invalid color");
					}
				} catch (Exception e1) {
					popup.showMessageDialog(getParent(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		this.add(changeColor);
		this.setVisible(true);
		
		//Change zoom ratio
		JMenuItem zoom = new JMenuItem("Zoom");
		zoom.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JOptionPane popup = new JOptionPane();
				String str = popup.showInternalInputDialog(getParent(), "zoom %", "Zoom", JOptionPane.INFORMATION_MESSAGE);
				int zoom = 1;
				try {
				  zoom = Integer.parseInt(str);
				} catch (NumberFormatException e1) {
					
				}
				
				try {
					if(zoom >= 10 && zoom <= 1000 ) {
						PanelSheet.setZoom(zoom);
						PanelGraph.getInstance().repaint();
					} else {
						popup.showMessageDialog(getParent(), "Error : zoom must be between 10 and 1000");
					}
				} catch (Exception e1) {
					popup.showMessageDialog(getParent(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		this.add(zoom);
		this.setVisible(true);
	}
}
