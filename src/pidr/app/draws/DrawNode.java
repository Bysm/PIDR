package pidr.app.draws;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import pidr.app.managers.ManagerGraph;
import pidr.app.models.Arc;
import pidr.app.models.Node;
import pidr.app.views.PanelGraph;
import pidr.app.views.components.panels.PanelArcOption;
import pidr.app.views.components.panels.PanelSheet;

public class DrawNode extends ADraw {
	//Radius of nodes
	private static int R = 25;
	
	private Node node;
	private boolean pressed = false;
	private boolean hover = false;
	private boolean selected = false;
	private boolean dragged = false;
	
	private static DrawNode nodeSelected = null;
	
	public Node getNode() {
		return node;
	}

	public DrawNode(Node node) {
		super();
		this.node = node;
		
	}
	
	public DrawNode(Node node, int x, int y) {
		this(node);
		//To make sure nodes can't leave the panel
		setBounds(x, y, getWidth(), getHeight());
	}
	
	public void mouseClicked(MouseEvent e) {
		//Do nothing, replaced by mousePressed and mouseReleased
	}

	public void mousePressed(MouseEvent e) {
		pressed = true;
		getParent().repaint();
	}

	public void mouseReleased(MouseEvent e) {
		pressed = false;
		if (hover && !dragged) {
			ManagerGraph mg = ManagerGraph.getInstance();
			if (mg.isModeDeleteNode()) {
				mg.deleteNode(DrawNode.this.node);
				return;
			}
			else if (mg.isModeAddArc()){
				if (mg.getArc() == null) {
					mg.setArc(new Arc(0, node, null));
				}
				else if (e.getButton() == MouseEvent.BUTTON1 && mg.getArc().getHeadNode() == node) {
					mg.setArc(null);
				}
				else if (e.getButton() == MouseEvent.BUTTON3) {
					PanelArcOption popup = new PanelArcOption();
					boolean directed = popup.getDirected();
					if(popup.getColor() == null) {
						return;
					}
					int color = popup.getColor();
					
					if (!ManagerGraph.getColors().containsKey(color)) {
						ManagerGraph.generateColor(color);
					}
					mg.addArcAndNotify(mg.getArc().getHeadNode(), node, directed, color);
					mg.setArc(null);
				}
			}
			else if (!selected) {
				nodeSelected = DrawNode.this;
				selected = true;
				PanelGraph.getInstance().showManageComponent(this.node);
			}
			else if (selected) {
				nodeSelected = null;
				selected = false;
				PanelGraph.getInstance().hideManageComponent();
			}
		}
		dragged = false;
		getParent().repaint();
	}

	public void mouseEntered(MouseEvent e) {
		hover = true;
		getParent().repaint();
	}

	public void mouseExited(MouseEvent e) {
		hover = false;
		getParent().repaint();
	}

	public void mouseDragged(MouseEvent e) {
		if (pressed) {
			dragged = true;
			if(getX() + e.getX()>0 && getY() + e.getY()>0) {
				setLocation(getX() + e.getX() - getHeight()/2, getY() + e.getY() - getWidth()/2);
				getParent().repaint();
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (ManagerGraph.getInstance().isModeAddArc() && ManagerGraph.getInstance().getArc() != null) {
			getParent().repaint();
		}
	}

	@Override
	public void paint(Graphics g) {
		setSize(getR()*2, getR()*2);
		
		// When selected we set node color to red
		if (selected) {
			g.setColor(Color.RED);
		}
		//If it is being dragged
		else if (pressed) {
			g.setColor(Color.BLUE);
		} else g.setColor(Color.BLACK);

		// Node hover
		if (hover && !ManagerGraph.getInstance().isDisplayNames()) {
			g.drawString(node.getName(), getX(), getY()+getHeight() + 20);
		}
		if (ManagerGraph.getInstance().isDisplayNames()) {
			String name = node.getName().replace("Node n°", "");
			g.drawString(name, (int) (getX() + (getR()-name.length()*2.5)), getY()+getHeight() - (getR()-5));
		}
		
		// We draw the node
		g.drawOval(getX(), getY(), getWidth(), getHeight());
	}

	public static int getR() {
		return R*PanelSheet.getZoom()/100;
	}

	public void setSelected(boolean selected) {
		this.nodeSelected = null;
		this.selected = selected;
	}

}
