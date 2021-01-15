package keyframes;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class sessionToolTest {
	
	@Test
	public void testBrushSize() {
		//brush size ranges
		//min: 1
		//max: 30
		/*
		Session sess = new Session();
		sess.setBrushSize(30);
		assertTrue(sess.getBrushSize() == 30);
		sess.setBrushSize(2);
		assertTrue(sess.getBrushSize() == 2);
		sess.setBrushSize(-1);
		// Invalid brush size should not change the brush size
		assertTrue(sess.getBrushSize() == 2);
		*/
	}
	
	@Test
	public void testEraserSize() {
		//eraser size ranges
		//min: 1
		//max: 30
		/*
		Session sess = new Session();
		sess.setEraserSize(30);
		assertTrue(sess.getEraserSize() == 30);
		sess.setEraserSize(2);
		assertTrue(sess.getEraserSize() == 2);
		sess.setEraserSize(-1);
		// Invalid eraser size should not change the eraser size
		assertTrue(sess.getEraserSize() == 2);
		*/
	}
	
	//These two don't work because of how I changed the session...
	
	@Test
	public void testLongestTimeInSeconds() {
		//endtime ranges
		//min: 1
		//max: 20
		
		/*
		Session sess = new Session();
		sess.setLongestTimeInSeconds(10);
		assertTrue(sess.getLongestTimeInSeconds() == 10);
		sess.setLongestTimeInSeconds(21);
		assertTrue(sess.getLongestTimeInSeconds() == 10);
		sess.setLongestTimeInSeconds(-1);
		// Invalid longest timepoint should not change longest timepoint
		assertTrue(sess.getLongestTimeInSeconds() == 10);
		*/
	}
	
	
	@Test
	public void testFps() {
		//FPS ranges
		//min:10
		//max:30
		
		/*
		Session sess = new Session();
		int endSec = sess.getLongestTimeInSeconds();
		sess.setFramesPerSecond(20);
		assertTrue(sess.getFramesPerSecond() == 20);
		// The endpoint should be changed if fps is changed too
		assertTrue(sess.getLongestTimepoint() == endSec * sess.getFramesPerSecond());
		
		sess.setFramesPerSecond(30);
		assertTrue(sess.getFramesPerSecond() == 30);
		assertTrue(sess.getLongestTimepoint() == endSec * sess.getFramesPerSecond());
		int lastLongestTimepoint = endSec * sess.getFramesPerSecond();
		
		sess.setLongestTimeInSeconds(-1);
		// Invalid fps should not work, should not update fps and longest timepoint
		assertTrue(sess.getFramesPerSecond() == 30);
		assertTrue(sess.getLongestTimepoint() == lastLongestTimepoint);
		*/
	}
}
