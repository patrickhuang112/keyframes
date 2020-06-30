package keyframes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

public class MainView {
	
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
	
	public MainView(JFrame parent) {
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
		
		Timeline mainTimeline = new Timeline(drawableAndTimelinePane, false);
		Drawable mainDrawable = new Drawable(drawableAndTimelinePane, false);

		drawableAndTimelinePane = new SplitPaneManager(mainDrawable, mainTimeline, true);
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
		JButton first = new JButton("first");
		JButton second = new JButton("second");
		first.setEnabled(true);
		topToolBar.add(first);
		topToolBar.add(second);
	}
}