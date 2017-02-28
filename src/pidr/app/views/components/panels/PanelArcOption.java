package pidr.app.views.components.panels;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

//The panel that pops up when you create an arc
public class PanelArcOption extends JOptionPane {
	
	JCheckBox directed;
	JTextField color;
	JComponent[] inputs;
	
	public PanelArcOption() {
		directed = new JCheckBox();
		color = new JTextField();
		inputs = new JComponent[] {
				new JLabel("Directed"),
				directed,
				new JLabel("Color (int)"),
				color
		};
		JOptionPane.showMessageDialog(getParent(), inputs, "Add arc", JOptionPane.PLAIN_MESSAGE);
	}

	public boolean getDirected() {
		return directed.isSelected();
	}

	public Integer getColor() {
		try {
			return Integer.valueOf(color.getText());
		} catch (Exception e) {
			System.out.println("Error in value of color");
		}
		return null;
	}
}
