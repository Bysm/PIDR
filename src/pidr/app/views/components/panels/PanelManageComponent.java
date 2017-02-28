package pidr.app.views.components.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import matlabcontrol.MatlabInvocationException;
import pidr.app.managers.ManagerGraph;
import pidr.app.managers.ManagerMatlab;
import pidr.app.models.Arc;
import pidr.app.models.Node;
import pidr.app.views.PanelGraph;
import pidr.app.views.components.buttons.ButtonEdit;

//The panel in the east to edit a node and its arcs.
public class PanelManageComponent extends Panel implements Observer {

	private PanelGraph panelGraph;
	private boolean open = false;
	private Node currentNode;
	private JPanel panelDetails;
	private JPanel panelListArc;
	private JPanel panelNode;
	private JPanel panelTop;
	private JLabel lblCoin;

	public PanelManageComponent(PanelGraph panelGraph) {
		super();
		this.panelGraph = panelGraph;
		ManagerGraph.getInstance().addObserver(this);
		setMinimumSize(new Dimension(400, 0));
	}

	public boolean isOpen() {
		return open;
	}
	
	//When we click on a node, it updates the panel
	//adds the list of connected arcs and updates if changed/deleted
	//when hovered on an arc, changes its color to red
	public void majDetail() {
		panelListArc.removeAll();
		JButton btnDelete;
		for (final Arc arc : panelGraph.getPanelSheet().getArcDraws().keySet()) {
			if (arc.getHeadNode() == currentNode
					|| arc.getTailNode() == currentNode) {

				/*JPanel panelRowListArc = new JPanel();
				panelRowListArc.setBorder(new EmptyBorder(1, 0, 1, 0));
				panelRowListArc.setPreferredSize(new Dimension(0, 1));
				panelRowListArc.setBackground(new Color(0, 125, 223));
				panelListArc.add(panelRowListArc);*/
				
				JPanel panelRowListArc = new JPanel(new GridLayout(0, 3));
				panelRowListArc.setBackground(new Color(21, 210, 255));
				panelRowListArc.setBorder(new EmptyBorder(0, 10, 0, 0));
				if (ManagerGraph.getInstance().isModeEditManageComponent()) {
					final JTextField colField = new JTextField(String.valueOf(arc.getColor()));
					final JCheckBox directed = new JCheckBox("dir", arc.isDirected());
					panelRowListArc.add(directed);
					panelRowListArc.add(colField);
					
					colField.addKeyListener(new KeyListener() {
						
						public void keyTyped(KeyEvent e) {}
						
						public void keyReleased(KeyEvent e) {}
						
						public void keyPressed(KeyEvent e) {
							if(e.getKeyCode() == KeyEvent.VK_ENTER) {
								int col = Integer.valueOf(colField.getText());
								if(col >= 0) {
									arc.setColor(col);
									if (!ManagerGraph.getColors().containsKey(col)) {
										ManagerGraph.generateColor(col);
									}
									try {
										ManagerMatlab.getInstance().removeArc(ManagerGraph.getInstance().getGraph(), arc);
										ManagerMatlab.getInstance().addArc(ManagerGraph.getInstance().getGraph(), arc);
									} catch (MatlabInvocationException e1) {}
								
									PanelGraph.getInstance().repaint();
								}
							}
							
						}
					});
					
					directed.addMouseListener(new MouseListener() {
						
						public void mouseReleased(MouseEvent e) {}
						
						public void mousePressed(MouseEvent e) {}
						
						public void mouseExited(MouseEvent e) {}
						
						public void mouseEntered(MouseEvent e) {}
						
						public void mouseClicked(MouseEvent e) {
							boolean dir = directed.isSelected();
							arc.setDirected(dir);
							try {
								ManagerMatlab.getInstance().removeArc(ManagerGraph.getInstance().getGraph(), arc);
								ManagerMatlab.getInstance().addArc(ManagerGraph.getInstance().getGraph(), arc);
							} catch (MatlabInvocationException e1) {}
							PanelGraph.getInstance().repaint();
						}
					});
					
				}
				else {
					panelRowListArc.add(new JLabel("Arc "+ arc.getId()));
					panelRowListArc.add(new JLabel("Col "+ arc.getColor()));
				}
				btnDelete = new JButton("X");
				btnDelete.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						new Thread(new Runnable() {
							public void run() {
								ManagerGraph.getInstance().deleteArc(arc);
							}
						}).start();
					}
				});
				panelRowListArc.add(btnDelete);
				
				panelRowListArc.addMouseListener(new MouseListener() {
					
					public void mouseReleased(MouseEvent e) {}
					
					public void mousePressed(MouseEvent e) {}
					
					public void mouseExited(MouseEvent e) {
						panelGraph.getPanelSheet().mouseOutArc(arc);
					}
					
					public void mouseEntered(MouseEvent e) {
						panelGraph.getPanelSheet().mouseOnArc(arc);
					}
					
					public void mouseClicked(MouseEvent e) {}
				});
				panelListArc.add(panelRowListArc);
			}
		}
		panelDetails.add(panelListArc, BorderLayout.CENTER);
		add(panelDetails);
		repaint();
		revalidate();
	}
	
	//Adds the selected node and updates the panels if it is edited
	public void majNode() {
		panelNode.removeAll();
		panelTop.removeAll();
		if(!ManagerGraph.getInstance().isModeEditManageComponent()) {
			JLabel lblName = new JLabel(currentNode.getName());
			lblName.setBorder(new EmptyBorder(5, 5, 5, 5));
			lblName.addMouseListener(new MouseListener() {
				
				public void mouseReleased(MouseEvent e) {}
				
				public void mousePressed(MouseEvent e) {}
				
				public void mouseExited(MouseEvent e) {}
				
				public void mouseEntered(MouseEvent e) {}
				
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						ManagerGraph.getInstance().toggleModeEditManageComponent();
					}
				}
			});
			panelNode.add(lblName);
		} else {
			final JTextField textField = new JTextField(currentNode.getName());
			panelNode.add(textField);
			textField.addKeyListener(new KeyListener() {
				
				public void keyTyped(KeyEvent e) {}
				
				public void keyReleased(KeyEvent e) {}
				
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						String name = textField.getText();
						if(name != null && !name.equals("")) {
							currentNode.setName(name);
							PanelGraph.getInstance().repaint();
						}
					}
					
				}
			});
			panelNode.add(new ButtonEdit(currentNode));
		}
		panelTop.add(lblCoin, BorderLayout.WEST);
		panelTop.add(panelNode, BorderLayout.CENTER);
		panelDetails.add(panelTop, BorderLayout.NORTH);
		add(panelDetails);
		repaint();
		revalidate();
	}
	
	//shows and hide the panel depending on whether a node is selected
	public void showDetail(Node node) {
		currentNode = node;
		removeAll();
		
		panelDetails = new JPanel(new BorderLayout());
		panelDetails.setAlignmentY(TOP_ALIGNMENT);
		panelNode = new JPanel(new GridLayout(0, 1));
		panelTop = new JPanel(new BorderLayout());

		try {
			BufferedImage myPicture = ImageIO.read(this.getClass().getResource("/pidr/img/coin_gauche.png"));
			Image myImage = myPicture.getScaledInstance(41, 40, java.awt.Image.SCALE_SMOOTH);
			lblCoin = new JLabel(new ImageIcon(myImage));
			lblCoin.setBorder(new EmptyBorder(5, 0, 5, 0));
		} catch (IOException e) {}

		panelNode.setBackground(new Color(21, 210, 255));
		panelTop.setBorder(new EmptyBorder(0, 10, 0, 0));
		majNode();
		
		panelListArc = new JPanel(new GridLayout(0, 1));
		panelListArc.setBorder(new EmptyBorder(0, 51, 0, 0));
		majDetail();
		
		add(panelDetails);
		
		open = true;
	}

	public void hideDetail() {
		removeAll();
		revalidate();
		open = false;
	}
	public void update(Observable o, Object arg) {
		if (arg.equals("SWITCH_MODE_MANAGE_COMPONENT")) {
			majDetail();
			majNode();
		}
	}
}
