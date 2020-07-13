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
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

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
			
			JButton eraseTool = new JButton(new ImageIcon(eraserImage));
			JButton drawTool = new JButton(new ImageIcon(drawImage));
			
			drawTool.setVisible(true);
			drawTool.setPreferredSize(new Dimension(20,20));
			drawTool.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					session.setPaintSetting(EnumFactory.PaintSetting.DRAW);
				}
			});
			
			eraseTool.setVisible(true);
			eraseTool.setPreferredSize(new Dimension(20,20));
			eraseTool.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					session.setPaintSetting(EnumFactory.PaintSetting.ERASE);
				}
			});
			
			
			topToolBar.add(eraseTool);
			topToolBar.add(drawTool);
		} catch (Exception e) {
			System.out.println("Icon creation failed");
		}
		
		
		
		
		
	}

	@Override
	public Session getSession() {
		return session;
	}


}