package keyframes;

import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.BeforeClass;
import org.junit.Test;

import settings.Settings;

public class SessionToolTest {
	
	private static Settings settings;
	private static Controller c;
	
	@BeforeClass
	public static void initController() {
		Controller.createController();	
		settings = Utils.getSettings();
		c = Controller.getController();
	}
	
	@Test
	public void testBrushSize() {
		//brush size ranges (from settings)
		//min: 1
		//max: 30
		assertTrue(c.getBrushSize() == settings.getBrushSize());
		c.setBrushSize(30);
		assertTrue(c.getBrushSize() == 30);
		c.setBrushSize(2);
		assertTrue(c.getBrushSize() == 2);
		c.setBrushSize(31);
		assertTrue(c.getBrushSize() == 2);
		c.setBrushSize(-1);
		assertTrue(c.getBrushSize() == 2);
		// 0 should be reset to 1
		c.setBrushSize(0);
		assertTrue(c.getBrushSize() == 1);
	}
	
	@Test
	public void testEraserSize() {
		//eraser size ranges
		//min: 1
		//max: 30
		assertTrue(c.getEraserSize() == settings.getEraserSize());
		c.setEraserSize(30);
		assertTrue(c.getEraserSize() == 30);
		c.setEraserSize(2);
		assertTrue(c.getEraserSize() == 2);
		c.setEraserSize(31);
		assertTrue(c.getEraserSize() == 2);
		c.setEraserSize(-1);
		assertTrue(c.getEraserSize() == 2);
		// 0 should be reset to 1
		c.setEraserSize(0);
		assertTrue(c.getEraserSize() == 1);
	}
	
	@Test
	public void testLongestTimeInSeconds() {
		//endtime ranges
		//min: 0
		//max: 20
		assertTrue(c.getLongestTimeInSeconds() == settings.getCompLength());
		c.setLongestTimeInSeconds(20);
		assertTrue(c.getLongestTimeInSeconds() == 20);
		c.setLongestTimeInSeconds(2);
		assertTrue(c.getLongestTimeInSeconds() == 2);
		c.setLongestTimeInSeconds(31);
		assertTrue(c.getLongestTimeInSeconds() == 2);
		c.setLongestTimeInSeconds(-1);
		assertTrue(c.getLongestTimeInSeconds() == 2);
		// 0 should be reset to 1
		c.setLongestTimeInSeconds(0);
		assertTrue(c.getLongestTimeInSeconds() == 1);
	
	}
	
	
	@Test
	public void testFPS() {
		//FPS ranges
		//min:0
		//max:30
		
		assertTrue(c.getFramesPerSecond() == settings.getFps());
		c.setFramesPerSecond(30);
		assertTrue(c.getFramesPerSecond() == 30);
		c.setFramesPerSecond(2);
		assertTrue(c.getFramesPerSecond() == 2);
		c.setFramesPerSecond(31);
		assertTrue(c.getFramesPerSecond() == 2);
		c.setFramesPerSecond(-1);
		assertTrue(c.getFramesPerSecond() == 2);
		// 0 should be reset to 1
		c.setFramesPerSecond(0);
		assertTrue(c.getFramesPerSecond() == 1);
		
	}
	
	@Test
	public void testBrushColor() {
		//FPS ranges
		//min:0
		//max:30
		assertTrue(c.getBrushColor().equals(settings.getBrushColor()));
		c.setBrushColor(Color.red);
		assertTrue(c.getBrushColor().equals(Color.red));
		c.setBrushColor(Color.black);
		assertTrue(c.getBrushColor().equals(Color.black));
		c.setBrushColor(Color.orange);
		assertTrue(c.getBrushColor().equals(Color.orange));
		c.setBrushColor(new Color(0.03f,0.35f, 0.94f, 0.72f));
		assertTrue(c.getBrushColor().equals(new Color(0.03f,0.35f, 0.94f, 0.72f)));
	}
	
	@Test
	public void testBackgroundColor() {
		//FPS ranges
		//min:0
		//max:30
		c.setBackgroundColor(Color.red);
		assertTrue(c.getBackgroundColor().equals(Color.red));
		c.setBackgroundColor(Color.black);
		assertTrue(c.getBackgroundColor().equals(Color.black));
		c.setBackgroundColor(Color.orange);
		assertTrue(c.getBackgroundColor().equals(Color.orange));
		c.setBackgroundColor(new Color(0.03f,0.35f, 0.94f, 0.72f));
		assertTrue(c.getBackgroundColor().equals(new Color(0.03f,0.35f, 0.94f, 0.72f)));
		
	}
	
	@Test
	public void testEraserColor() {
		//FPS ranges
		//min:0
		//max:30
		
		// First test doesn't make sense right now because eraser color should be the same as the background
		// color regardless of what it was in the settings
		
		//assertTrue(c.getEraserColor().equals(settings.getEraserColor()));
		c.setEraserColor(Color.red);
		assertTrue(c.getEraserColor().equals(Color.red));
		c.setEraserColor(Color.black);
		assertTrue(c.getEraserColor().equals(Color.black));
		c.setEraserColor(Color.orange);
		assertTrue(c.getEraserColor().equals(Color.orange));
		c.setEraserColor(new Color(0.03f,0.35f, 0.94f, 0.72f));
		assertTrue(c.getEraserColor().equals(new Color(0.03f,0.35f, 0.94f, 0.72f)));
	}
	
}
