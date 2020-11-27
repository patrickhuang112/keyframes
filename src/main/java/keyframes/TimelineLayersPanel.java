package keyframes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import javax.swing.JScrollPane;

public class TimelineLayersPanel extends JScrollPane {
	
	// Set this manually cause I couldn't get other things to work
	private double sliderBarx = 5;
	
	public TimelineLayersPanel() {
		super();
	}
	
	public void setSliderBarx(double x) {
		sliderBarx = x;
	}
	
	public double getSliderBarx() {
		return sliderBarx;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		// Draw the slider line in the timeline layers panel
		Graphics2D g2d = (Graphics2D) g;
		GeneralPath gp =  new GeneralPath();
		BasicStroke s = null;
		
		// Big values just to make sure it draws over the whole panel, look for a cleaner solution some other time
		gp.moveTo(sliderBarx, this.getY()-1000);
		
		g2d.setPaint(Color.black);
		s = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2d.setStroke(s);
		
		// Big values just to make sure it draws over the whole panel, look for a cleaner solution some other time
		gp.lineTo(sliderBarx, this.getY()+1000);
		
		g2d.draw(gp);
			
	}
	
}
