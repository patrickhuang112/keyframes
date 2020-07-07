package keyframes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

public class DrawablePanel extends JPanel implements MouseMotionListener, MouseListener{
	private float brushSize = 5.0f;
	Point point1;
	Graphics2D old;
    
	DrawablePanel() {
		super();
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	@Override
	public void paintComponent(Graphics g){
	if(old != null) super.paintComponent(old);
	else super.paintComponent(g);
	
	Graphics2D g2d = (Graphics2D) g;
		if(point1 != null){
	    	 
			float xPos = (float)point1.getX();
			float yPos = (float)point1.getY();
			float radius = brushSize;
			System.out.println("IM HERE");
			Ellipse2D.Float point = new Ellipse2D.Float(xPos, yPos, radius, radius);
	        g2d.setPaint(Color.red);
			g2d.draw(point);
			old = g2d;
		}
		
	}   


	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		point1 = e.getPoint();
		System.out.println(e.getX());
		System.out.println(e.getComponent().getHeight());
		repaint();
		//revalidate();
	}

	@Override
	public void mousePressed(MouseEvent e) {

	   }

	  @Override
	  public void mouseReleased(MouseEvent e) {

	  }

	 @Override
	 public void mouseEntered(MouseEvent e) {

	 }

	 @Override
	 public void mouseExited(MouseEvent e) {

	}
}
