package pidr.app.views.components.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import pidr.app.managers.ManagerGraph;
import pidr.app.models.Node;

//This button is for the east panel, to switch between view mode and edit mode
public class ButtonEdit extends JButton implements ActionListener {
	
	Node node;
	
	public ButtonEdit(Node node) {
		super("Stop edition mode");
		this.node = node;
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		ManagerGraph.getInstance().toggleModeEditManageComponent();
	}
}
