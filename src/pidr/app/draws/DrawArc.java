package pidr.app.draws;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import pidr.app.managers.ManagerGraph;
import pidr.app.models.Arc;

/*For information concerning the gemotric properties (points, distances...) 
 *check src/pidr/img/graphical properties.png
 **/
public class DrawArc extends ADraw {

	private Arc arc;
	private DrawNode headNodeDraw;
	private DrawNode tailNodeDraw;
	private boolean pressed = false;
	private boolean hover = false;
	//If there are multiple arcs between two nodes, the arcs are nubered
	private int num = 0;
	/*Radius of the drawn nodes: if (x,y) is the java center,
	  (x+R,y+R) is the center as we see it */
	private int R = DrawNode.getR();
	
	public void setNum(int num) {
		this.num = num;
	}
	
	public int getNum() {
		return num;
	}
	
	public Arc getArc() {
		return arc;
	}

	public DrawArc(Arc arc, DrawNode headNode, DrawNode tailNode, int num) {
		super();
		this.arc = arc;
		this.headNodeDraw = headNode;
		this.tailNodeDraw = tailNode;
		this.num = num;
	}
	
	
	
	@Override
	public String toString() {
		return "DrawArc [arc=" + arc + ", headNodeDraw=" + headNodeDraw + ", tailNodeDraw=" + tailNodeDraw
				+ ", pressed=" + pressed + ", hover=" + hover + ", num=" + num + "]";
	}

	@Override
	public void paint(Graphics g) {
		
		// Color
		if (hover) {
			g.setColor(Color.RED);
		} else g.setColor(ManagerGraph.getColors().get(arc.getColor()));
		
		
		/**
		 * Take into account all possible relative positions of p1 and p2
		 */
		// Head is point 1, Tail point 2
		
		double x1 = headNodeDraw.getX();
		double y1 = headNodeDraw.getY();
		double x2, y2;
		if (tailNodeDraw != null) {
			x2 = tailNodeDraw.getX();
			y2 = tailNodeDraw.getY();
		}
		else {
			x2 = getParent().getMousePosition().getX();
			y2 = getParent().getMousePosition().getY();
		}
		
		
		double x3 = 0, y3 = 0, x4 = 0, y4 = 0;
		//the line between the p3 and p4 has for equation y = ax + b
		double a = 0, b = 0;
		
		//e is 1 or -1 : it is used to invert the position of a point in the circle
		int e = 1;
		//alpha is the angle between the two node centers, compared to the horizontal
		double alpha = 0;
		int h = 30;
		int d=(int) (h*Math.pow(-1, num)*Math.floor((num+1)/2));
		int R = DrawNode.getR();
		
		if (x1!=x2) {
			
			if (x1>=x2) e = -e;
			
			a = (y1-y2)/(x1-x2);
			
			b = y1-a*x1;
			
			x3 = e*R/Math.sqrt(1+a*a)+x1;
			y3 = a*x3+b;

			x4 = -e*R/Math.sqrt(1+a*a)+x2;
			y4 = a*x4+b;
			
			alpha = Math.atan(a);
			
		}
		else {
			
			if (y1>=y2) e = -e;
			
			x3 = x1;
			y3 = y1+e*R;
			x4 = x2;
			y4 = y2-e*R;
			
			alpha = Math.PI/2;
		}

		x3 += R;
		x4 += R;
		y3 += R;
		y4 += R;
		
		double x5 = (x3+x4)/2;
		double y5 = (y3+y4)/2;
		
		double x6 = x5+Math.sin(alpha)*d;
		double y6 = y5-Math.cos(alpha)*d;
	
		if (tailNodeDraw != null) {
			g.drawPolyline(new int[] {(int) x3, (int) x6, (int) x4}, new int[] {(int) y3, (int) y6, (int) y4}, 3);	
		}
		else {
			g.drawPolyline(new int[] {(int) x3, (int) x2}, new int[] {(int) y3, (int) y2}, 2);
		}

		if (tailNodeDraw != null) {
			if(ManagerGraph.getInstance().isDisplayColors()) {
				if(Math.abs(x3 - x4) > 10 ) {
					g.drawString(String.valueOf(arc.getColor()), (int) x6, (int) y6 + 20);
				} else {
					g.drawString(String.valueOf(arc.getColor()), (int) x6 + 5, (int) y6);
				}
			}
		}
		
		// If the arc is directed, we draw an arrow
		// e2 makes sure the arrow is in the right direction the direction of the arrow !
		if (arc.isDirected()) {
			int c = 10*R/25;
			double beta = Math.PI/6;
			double h1 = c/Math.cos(beta);
			double o = c*Math.sin(beta);
			
			double x9;
			double y9;
			double a2;
			double b2;
			int e2 = 1;
			double gamma;
			
			if (x6!=x4) {
				a2 = (y6-y4)/(x6-x4);
				b2 = y6-a2*x6;

				if(x4<x6)
					e2 = -e2;

				gamma = Math.atan(a2);

				x9 = -e2*h1/Math.sqrt(1+a2*a2)+(x4-R);
				y9 = a2*(x9+R)+b2-R;
			} else {
				
				if (y6>=y4) 
					e2 = -e2;
				
				x9 = x2;
				y9 = y4-e2*h1-R;
				
				gamma = Math.PI/2;
				
			}
			
			x9 += R;
			y9 += R;
			
			double x7 = x9+Math.sin(gamma)*o;
			double y7 = y9-Math.cos(gamma)*o;
			double x8 = x9-Math.sin(gamma)*o;
			double y8 = y9+Math.cos(gamma)*o;
			
			g.drawPolygon(new int[] {(int) x7, (int) x8, (int) x4}, new int[] {(int) y7, (int) y8, (int) y4}, 3);
			
			
			
			//To display the positions of the point 1 to 9
			/*
			g.drawString("1", (int) x1, (int) y1);
			g.drawString("2", (int) x2, (int) y2);
			g.drawString("3", (int) x3, (int) y3);
			g.drawString("4", (int) x4, (int) y4);
			g.drawString("5", (int) x5, (int) y5);
			g.drawString("6", (int) x6, (int) y6);
			g.drawString("7", (int) x7, (int) y7);
			g.drawString("8", (int) x8, (int) y8);
			g.drawString("9", (int) x9, (int) y9);
			*/
			
		}
	}

	public void mousePressed(MouseEvent e) {
		pressed = true;
		getParent().repaint();
	}

	public void mouseReleased(MouseEvent e) {
		pressed = false;
		getParent().repaint();
	}

	public void mouseEntered(MouseEvent e) {
		hover = true;
		getParent().repaint();
	}

	public void mouseExited(MouseEvent e) {
		hover  = false;
		getParent().repaint();
	}
	
	public void setHover(boolean b) {
		hover = b;
	}
	
	public void mouseDragged(MouseEvent e) {
		//Do nothing
	}

	public void mouseMoved(MouseEvent e) {
		//Do nothing
	}
	
	public void mouseClicked(MouseEvent e) {
		//Do nothing
	}


}
