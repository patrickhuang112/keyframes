package ui.canvas;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import commands.CommandFactory;
import datatypes.DrawFrame;
import datatypes.DrawPoint;
import datatypes.Enums;
import datatypes.Layer;
import datatypes.SessionObject;
import keyframes.Controller;
import keyframes.Session;
import ui.UIComponent;

public class StandardKFCanvas extends AbstractKFCanvas {
	
	StandardKFCanvas(int w, int h) {
		super(w,h);
	}
}
