package pidr.app.views.components.panels;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

//The panel to edit/create a color
public class PanelColor extends JOptionPane {

	JTextField color;
	JTextField red;
	JTextField blue;
	JTextField green;
	JComponent[] inputs;
	
	public PanelColor() {
		color = new JTextField();
		red = new JTextField();
		blue = new JTextField();
		green = new JTextField();
		inputs = new JComponent[] {
				new JLabel("Red and White are not recommended"),
				new JLabel(""),
				new JLabel("Color number"),
				color,
				new JLabel("Red value (0-255)"),
				red,
				new JLabel("Green value (0-255)"),
				green,
				new JLabel("Blue value (0-255)"),
				blue
		};
		JOptionPane.showMessageDialog(getParent(), inputs, "Edit color", JOptionPane.PLAIN_MESSAGE);
	}

	public int getRed() {
		try {
			return Integer.valueOf(red.getText())%256;
		} catch (Exception e) {
			System.out.println("Error on selection of red");
		}
		return -1;
	}

	public int getBlue() {
		try {
			return Integer.valueOf(blue.getText())%256;
		} catch (Exception e) {
			System.out.println("Error on selection of blue");
		}
		return -1;
	}
	
	public int getGreen() {
		try {
			return Integer.valueOf(green.getText())%256;
		} catch (Exception e) {
			System.out.println("Error on selection of green");
		}
		return -1;
	}
	
	public int getColor() {
		try {
			return Integer.valueOf(color.getText());
		} catch (Exception e) {
			System.out.println("Error on selection of color");
		}
		return -1;
	}
}
