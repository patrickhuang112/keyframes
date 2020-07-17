package keyframes;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicSliderUI;

public class MainView implements SessionObject{
	
	private Session session;
	
	private JFrame parent;
	private JPanel mainWindow;
	
	private JPanel topBar;
	private JMenuBar topMenuBar;
	private JToolBar topToolBar;
	
	private SplitPaneManager drawableAndTimelinePane;
	
	private JMenu fileMenu;
	private JMenu editMenu;
	
	private JMenuItem newProjectMenuItem;
	private JMenuItem openProjectMenuItem;
	private JMenuItem saveProjectMenuItem;
	private JMenuItem saveProjectAsButton;
	
	
	public MainView(JFrame parent, Session session) {
		this.session = session;
		this.parent = parent;
		mainWindow = new JPanel(new BorderLayout());
		this.parent.add(mainWindow, BorderLayout.CENTER);
	}

	public void buildUI() {
		mainWindow.setBackground(Color.blue);
		
		buildTopBar();
		buildDrawableAndTimeline();
		parent.revalidate();
		parent.repaint();
	}
	
	private void buildDrawableAndTimeline() {
		
		drawableAndTimelinePane = new SplitPaneManager(this, true);
		
		Timeline mainTimeline = new Timeline(drawableAndTimelinePane, false);
		Drawable mainDrawable = new Drawable(drawableAndTimelinePane, false);

		
		drawableAndTimelinePane.setTopOrLeft(mainDrawable);
		drawableAndTimelinePane.setBottomOrRight(mainTimeline);
		drawableAndTimelinePane.getBottomOrRight().getMainComponent().setPreferredSize(new Dimension(0,200));
		
		mainTimeline.build();
		mainDrawable.build();
	
		mainWindow.add(drawableAndTimelinePane.getMainComponent());
	}
	
	private void buildTopBar() {
		topBar = new JPanel();
		topBar.setLayout(new BoxLayout(topBar, BoxLayout.Y_AXIS));
		topBar.setPreferredSize(new Dimension(0,60));
		
		mainWindow.add(topBar, BorderLayout.PAGE_START);
		buildTopMenuBar();
		buildtopToolBar();
	}
	
	private void buildTopMenuBar() {
		topMenuBar = new JMenuBar();
		
		fileMenu = new JMenu("File");
		newProjectMenuItem = new JMenuItem("New Project");
		openProjectMenuItem = new JMenuItem("Open Project");
		saveProjectMenuItem = new JMenuItem("Save");
		saveProjectAsButton = new JMenuItem("Save As..");
		
		fileMenu.add(newProjectMenuItem);
		fileMenu.add(openProjectMenuItem);
		fileMenu.add(saveProjectMenuItem);
		fileMenu.add(saveProjectAsButton);
		
		topMenuBar.add(fileMenu);
		
		editMenu = new JMenu("Edit");
		topMenuBar.add(editMenu);
		
		topMenuBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		topBar.add(topMenuBar);
	}
	
	private void buildtopToolBar() {
		topToolBar = new JToolBar(JToolBar.HORIZONTAL);
		topToolBar.setPreferredSize(new Dimension(0,30));
		topToolBar.setFloatable(false);
		topToolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		buildtopToolBarButtons();
		topBar.add(topToolBar);
		
	}
	
	private void buildtopToolBarButtons() {
		
		try {
			Image eraserImage = ImageIO.read(this.getClass().getResource("../resources/eraseIcon.bmp"));
			Image drawImage = ImageIO.read(this.getClass().getResource("../resources/drawIcon.bmp"));
			Image eraseAllImage = ImageIO.read(this.getClass().getResource("../resources/eraseAllIcon.bmp"));
			
			JButton eraseTool = new JButton(new ImageIcon(eraserImage));
			JButton brushTool = new JButton(new ImageIcon(drawImage));
			JButton eraseAllTool = new JButton(new ImageIcon(eraseAllImage));
			
			brushTool.setVisible(true);
			brushTool.setPreferredSize(new Dimension(20,20));
			brushTool.addMouseListener(new MouseAdapter( ) {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isLeftMouseButton(e)) {
						session.setPaintSetting(EnumFactory.PaintSetting.DRAW);
					}
					else if(SwingUtilities.isRightMouseButton(e)) {
						JOptionPane brushSizeSelector = new JOptionPane();
						
						JSlider brushSizeSlider = new JSlider(JSlider.HORIZONTAL, 0,30, session.getBrushSize());
						brushSizeSlider.setMajorTickSpacing(10);
						brushSizeSlider.setMinorTickSpacing(1);
						brushSizeSlider.setPaintTicks(true);
						brushSizeSlider.setPaintLabels(true);
						brushSizeSlider.addMouseListener(MouseAdapterFactory.clickToMouseAdapter);
						
						
						
						brushSizeSelector.setMessage(new Object[] { "Select a Brush Size: ", brushSizeSlider});
						brushSizeSelector.setMessageType(JOptionPane.QUESTION_MESSAGE);
						brushSizeSelector.setOptionType(JOptionPane.OK_CANCEL_OPTION);
						//Centered on the mainWindow
						JDialog dialog = brushSizeSelector.createDialog(mainWindow, "Brush Size");
					    dialog.setVisible(true);
					    
					    
					    if(brushSizeSelector.getValue() != null) {
					    	int res = (int)(brushSizeSelector.getValue());
					    	if(res == JOptionPane.YES_OPTION) {
					    		session.setBrushSize(brushSizeSlider.getValue());
					    	} else {
					    		System.out.println("no?");
					    	}	
					    }
					}
				}
			});
			
			eraseTool.setVisible(true);
			eraseTool.setPreferredSize(new Dimension(20,20));
			eraseTool.addMouseListener(new MouseAdapter( ) {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isLeftMouseButton(e)) {
						session.setPaintSetting(EnumFactory.PaintSetting.ERASE);
					}
					else if(SwingUtilities.isRightMouseButton(e)) {
						JOptionPane eraserSizeSelector = new JOptionPane();
						
						JSlider eraserSizeSlider = new JSlider(JSlider.HORIZONTAL, 0,30, session.getEraserSize());
						eraserSizeSlider.setMajorTickSpacing(10);
						eraserSizeSlider.setMinorTickSpacing(1);
						eraserSizeSlider.setPaintTicks(true);
						eraserSizeSlider.setPaintLabels(true);
						eraserSizeSlider.addMouseListener(MouseAdapterFactory.clickToMouseAdapter);
						
						eraserSizeSelector.setMessage(new Object[] { "Select an Eraser Size: ", eraserSizeSlider});
						eraserSizeSelector.setMessageType(JOptionPane.QUESTION_MESSAGE);
						eraserSizeSelector.setOptionType(JOptionPane.OK_CANCEL_OPTION);
						//Centered on the mainWindow
						JDialog dialog = eraserSizeSelector.createDialog(mainWindow, "Eraser Size");
					    dialog.setVisible(true);
					    
					    
					    if(eraserSizeSelector.getValue() != null) {
					    	int res = (int)(eraserSizeSelector.getValue());
					    	if(res == JOptionPane.YES_OPTION) {
					    		session.setEraserSize(eraserSizeSlider.getValue());
					    	} else {
					    		System.out.println("no?");
					    	}	
					    }
					}
				}
			});
			
			eraseAllTool.setVisible(true);
			eraseAllTool.setPreferredSize(new Dimension(20,20));
			eraseAllTool.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					DrawablePanel drawPane = (DrawablePanel)drawableAndTimelinePane.getTopOrLeft().getMainComponent();
					drawPane.clearAll();
				}
			});
			
			
			topToolBar.add(eraseTool);
			topToolBar.add(brushTool);
			topToolBar.add(eraseAllTool);
		} catch (Exception e) {
			System.out.println("Icon creation failed");
		}
	}
	

	@Override
	public Session getSession() {
		return session;
	}


}