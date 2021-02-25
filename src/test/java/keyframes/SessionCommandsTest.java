package keyframes;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import commands.CommandFactory;
import datatypes.Layer;
import settings.Settings;

public class SessionCommandsTest {

	private static Controller c;
	
	@Before
	public void initController() {
		Controller.createController();	
		c = Controller.getController();
	}

	@Test 
	public void testAddLayerCommand() {
		assertTrue(c.getLayers().size() == 1);
		c.addAndExecuteCommand(CommandFactory.createAddLayerCommand());
		assertTrue(c.getLayers().size() == 2);
		Layer newLayer = c.getLayers().get(1);
		assertTrue(newLayer.getName().equals("Layer 2"));
		assertTrue(newLayer.isSelected());
		assertTrue(newLayer.isVisible());
		assertTrue(!newLayer.isGhost());
		int testNumOfLayers = 10;
		
		// If a layer has layer num 1, (its index in the array list of layers), its user visible number should be 2
		for (Integer i = 3; i < testNumOfLayers; i++) {
			c.addAndExecuteCommand(CommandFactory.createAddLayerCommand());
			assertTrue(c.getCurrentLayerNum() == i-1);
			Layer curLayer = c.getCurrentLayer();
			assertTrue(curLayer.getLayerNum() == i-1);
			assertTrue(curLayer.getName().equals("Layer " + i.toString()));
			assertTrue(curLayer.isSelected());
		}
	}
	
	@Test 
	public void testDeleteLayersCommand() {
		assertTrue(c.getLayers().size() == 1);
		int testNumOfLayers = 10;
		for (int i = 0; i < testNumOfLayers; i++) {
			c.addAndExecuteCommand(CommandFactory.createAddLayerCommand());
		}
		assertTrue(c.getLayers().size() == 11);
		ArrayList<Integer> layersToDelete = new ArrayList<>();
		layersToDelete.add(10);
		Layer deletedLayer = c.getLayers().get(10);
		assertTrue(deletedLayer == c.getLayers().get(10));
		c.addAndExecuteCommand(CommandFactory.createDeleteLayersCommand(layersToDelete));
		assertTrue(c.getLayers().size() == 10);
		for (int i = 0; i < 10; i++) {
			Layer curLayer = c.getLayers().get(i);
			// All layer layernums should be reset to now be back in numerical order
			assertTrue(curLayer.getLayerNum() == i);
			// Deleted layer should not be still in the layers
			assertTrue(curLayer != deletedLayer);
		}
		layersToDelete.remove(0);
		// remove the second layer (index 1)
		layersToDelete.add(1);
		deletedLayer = c.getLayers().get(1);
		c.addAndExecuteCommand(CommandFactory.createDeleteLayersCommand(layersToDelete));
		for (int i = 0; i < 9; i++) {
			Layer curLayer = c.getLayers().get(i);
			// All layer layernums should be reset to now be back in numerical order
			assertTrue(curLayer.getLayerNum() == i);
			// Deleted layer should not be still in the layers
			assertTrue(curLayer != deletedLayer);
			// Further more layer names should be unchanged!
			assertTrue(!curLayer.getName().equals("Layer 2"));
		}
	}
	
	
	@Test
	public void testChangeLayerColorCommand() {
		assertTrue(c.getLayers().size() == 1);
		c.addAndExecuteCommand(CommandFactory.createLayerColorSelectedCommand(Color.cyan));
		assertTrue(c.getCurrentLayer().getColor().equals(Color.cyan));
		c.addAndExecuteCommand(CommandFactory.createLayerColorSelectedCommand(new Color(0.03f,0.35f, 0.94f, 0.72f)));
		assertTrue(c.getCurrentLayer().getColor().equals(new Color(0.03f,0.35f, 0.94f, 0.72f)));
	}
	
	@Test
	public void testRenameLayerCommand() {
		assertTrue(c.getLayers().size() == 1);
		assertTrue(c.getCurrentLayer().getName().equals("Layer 1"));
		c.addAndExecuteCommand(CommandFactory.createRenameCurrentLayerCommand("test1"));
		assertTrue(c.getCurrentLayer().getName().equals("test1"));
		c.addAndExecuteCommand(CommandFactory.createAddLayerCommand());
		c.addAndExecuteCommand(CommandFactory.createAddLayerCommand());
		c.addAndExecuteCommand(CommandFactory.createRenameCurrentLayerCommand("test2"));
		assertTrue(c.getCurrentLayer().getName().equals("test2"));
	}
	
	@Test 
	public void testUndoCommand() {
		assertTrue(c.getLayers().size() == 1);
		assertTrue(c.getCurrentLayer().getName().equals("Layer 1"));
		c.addAndExecuteCommand(CommandFactory.createRenameCurrentLayerCommand("test1"));
		assertTrue(c.getCurrentLayer().getName().equals("test1"));
		c.undoCommand();
		assertTrue(c.getCurrentLayer().getName().equals("Layer 1"));
		
		//Add three new layers, undo one, should have 3 layers now
		c.addAndExecuteCommand(CommandFactory.createAddLayerCommand());
		c.addAndExecuteCommand(CommandFactory.createAddLayerCommand());
		c.addAndExecuteCommand(CommandFactory.createAddLayerCommand());
		
		c.undoCommand();
		assertTrue(c.getLayers().size() == 3);
		c.undoCommand();
		assertTrue(c.getLayers().size() == 2);
	}
	
	@Test
	public void testRedoCommand() {
		c.addAndExecuteCommand(CommandFactory.createAddLayerCommand());
		c.addAndExecuteCommand(CommandFactory.createAddLayerCommand());
		c.addAndExecuteCommand(CommandFactory.createAddLayerCommand());
		// Now there should be 4 layers
		c.undoCommand();
		c.undoCommand();
		// Now there should be 2 layers
		c.redoCommand();
		// Now there should be 3 layers
		assertTrue(c.getLayers().size() == 3);
		c.redoCommand();
		assertTrue(c.getLayers().size() == 4);
	}
	
}
