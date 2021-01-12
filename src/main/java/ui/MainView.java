package ui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicSliderUI;

import com.formdev.flatlaf.FlatDarkLaf;

import datatypes.ProgressBar;
import datatypes.SessionObject;
import factories.EnumFactory;
import keyframes.MagicValues;
import keyframes.Session;
import keyframes.Utils;
import settings.Settings;
import ui.button.ButtonFactory;
import ui.dialog.CompositionLengthDialog;
import ui.dialog.DialogFactory;
import ui.dialog.FPSDialog;
import ui.dialog.SettingsDialog;
import ui.slider.SliderFactory;


public class MainView implements SessionObject, Serializable{
	
	private static final long serialVersionUID = -4390147842958702501L;

	private Session session;
	
	private JFrame window;
	private JPanel mainPanel;
	
	private JPanel topBar;
	private JMenuBar topMenuBar;
	// Contains topToolBar and progressBar
	private JPanel topToolBarContainer;
	private JToolBar topToolBar;
	
	private SplitPaneManager drawableAndTimelinePane;
	
	private JMenu fileMenu;
	private JMenu editMenu;
	
	private JMenuItem newProjectMenuItem;
	private JMenuItem openProjectMenuItem;
	private JMenuItem saveProjectMenuItem;
	private JMenuItem saveProjectAsMenuItem;
	private JMenuItem renderMP4AsMenuItem;
	private JMenuItem renderGIFAsMenuItem;
	private JMenuItem settingsMenuItem;
	
	private JMenuItem editFpsMenuItem;
	private JMenuItem editTimeMenuItem;
	private JMenuItem editBackgroundColorItem;
	
	private JProgressBar renderingProgressBar;
	private ProgressBar progressBar;
	
	private static MainView instance;
	
	public static MainView getInstance() {
		if (instance == null) {
			MainView.instance = new MainView();
		}
		return MainView.instance;
	}
	
	public static void createNewInstance() {
		MainView.instance = new MainView();
	}

	
	//Only package visible
	SplitPaneManager getSplitPaneManager() {
		return drawableAndTimelinePane;
	}
	
	private MainView() {
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("Keyframes");
		window.setVisible(true);
		window.setMinimumSize(new Dimension(MagicValues.windowMinimumWidth, MagicValues.windowMinimumHeight));
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		mainPanel = new JPanel(new BorderLayout());
		window.add(mainPanel, BorderLayout.CENTER);
		
		Settings settings = Utils.getSettings();
		session = new Session(settings);
		
		buildUI();
	}

	
	
	private void buildUI() {
		mainPanel.setBackground(Color.blue);
		buildDrawableAndTimeline();
		buildTopBar();
		setKeyMaps();
		window.revalidate();
		window.repaint();
		
	}
	
	private void setKeyMaps() {
		// Copy frames from current layer
		mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control C"), "copied");
		mainPanel.getActionMap().put("copied", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getSession().copyFramesFromCurrentLayerAndCurrentTime();
			}
		});
		// Paste copied frames into current layer
		mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control V"), "pasted");
		mainPanel.getActionMap().put("pasted", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getSession().pasteFramesToCurrentLayerAndCurrentTime();
			}
		});
		
		// Play and pause movie
		mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "movieplaypause");
		mainPanel.getActionMap().put("movieplaypause", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getSession().playOrPauseMovie();
			}
		});
		
		mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S"), "save");
		mainPanel.getActionMap().put("save", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Utils.saveFile(e, session, saveProjectMenuItem);
			}
		});
		
	}
	
	private void buildDrawableAndTimeline() {
		
		drawableAndTimelinePane = new SplitPaneManager(this, true);
		
		Timeline mainTimeline = new Timeline(drawableAndTimelinePane, false);
		mainTimeline.buildUI();
		
		Drawable mainDrawable = new Drawable(drawableAndTimelinePane, false);
		mainDrawable.buildUI();
		
		drawableAndTimelinePane.setTopOrLeft(mainDrawable);
		drawableAndTimelinePane.setBottomOrRight(mainTimeline);
		
		
		
	
		mainPanel.add(drawableAndTimelinePane.getMainComponent());
	}
	
	private void buildTopBar() {
		
		int tw = MagicValues.mainViewTopBarDefaultWidth;
		int th = MagicValues.mainViewTopBarDefaultHeight;
		
		topBar = new JPanel();
		topBar.setLayout(new BorderLayout());
		topBar.setPreferredSize(new Dimension(tw,th));
		topBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.add(topBar, BorderLayout.PAGE_START);
		buildTopMenuBar();
		buildTopToolBarContainer();
	}
	
	private void buildTopMenuBar() {
		
		int w = MagicValues.mainViewTopMenuBarDefaultWidth;
		int h = MagicValues.mainViewTopMenuBarDefaultHeight;
		
		topMenuBar = new JMenuBar();
		topMenuBar.setPreferredSize(new Dimension(w,h));
		
		// FILE MENU
		fileMenu = new JMenu("File");
		newProjectMenuItem = new JMenuItem(new AbstractAction("New Project") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Utils.newFile(getSession(), window);
			}
		});
		openProjectMenuItem = new JMenuItem(new AbstractAction("Open Project") {
			public void actionPerformed(ActionEvent e) {
				Utils.openFile(e, getSession(), openProjectMenuItem, window);
			}
		});
		
		saveProjectMenuItem = new JMenuItem(new AbstractAction("Save") {

			@Override
			public void actionPerformed(ActionEvent e) {
				Utils.saveFile(e, getSession(), saveProjectMenuItem);
			}
		});
		
		saveProjectAsMenuItem = new JMenuItem(new AbstractAction("Save As..") {
			@Override
			public void actionPerformed(ActionEvent e) {
				Utils.saveAsFile(e, getSession(), saveProjectAsMenuItem);
			}
		});
		
		renderMP4AsMenuItem = new JMenuItem(new AbstractAction("Render MP4 As..") {
			@Override
			public void actionPerformed(ActionEvent e) {
				Utils.renderFile(e, getSession(), renderMP4AsMenuItem, getSession().getFramesPerSecond());
			}
		});
		
		renderGIFAsMenuItem = new JMenuItem(new AbstractAction("Render GIF As..") {
			@Override
			public void actionPerformed(ActionEvent e) {
				Utils.renderGif(e, getSession(), renderGIFAsMenuItem, getSession().getFramesPerSecond());
			}
		});
		
		settingsMenuItem = new JMenuItem(new AbstractAction("Settings") {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogFactory.createSettingsDialog(window, "Settings", Utils.getSettings());
			}
		});
		
			
		fileMenu.add(newProjectMenuItem);
		fileMenu.add(openProjectMenuItem);
		fileMenu.add(saveProjectMenuItem);
		fileMenu.add(saveProjectAsMenuItem);
		fileMenu.add(renderMP4AsMenuItem);
		fileMenu.add(renderGIFAsMenuItem);
		fileMenu.add(settingsMenuItem);
		
		topMenuBar.add(fileMenu);
		
		// EDIT MENU
		editMenu = new JMenu("Edit");
		editFpsMenuItem = new JMenuItem(new AbstractAction("Edit Frames Per Second") {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogFactory.createFPSDialog(mainPanel, getSession());
			}
		});
		
		editTimeMenuItem = new JMenuItem(new AbstractAction("Edit Composition Endpoint") {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogFactory.createCompositionLengthDialog(mainPanel, getSession());
			}
		});
		
		editBackgroundColorItem = new JMenuItem(new AbstractAction("Edit Background Color") {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogFactory.createBackgroundColorDialog(getSession());
			}
		});
		
		editMenu.add(editFpsMenuItem);
		editMenu.add(editTimeMenuItem);
		editMenu.add(editBackgroundColorItem);
		
		topMenuBar.add(editMenu);
		topBar.add(topMenuBar, BorderLayout.PAGE_START);
	}
	
	private void buildTopToolBarContainer() {
		topToolBarContainer = new JPanel();
		topToolBarContainer.setLayout(new BoxLayout(topToolBarContainer, BoxLayout.X_AXIS));
		
		buildTopToolBar();
		buildHorizontalGlue();
		buildProgressBar();
		
		topBar.add(topToolBarContainer, BorderLayout.PAGE_END);
	}
	
	private void buildHorizontalGlue() {
		topToolBarContainer.add(Box.createHorizontalGlue());
	}
	
	private void buildTopToolBar() {
		
		int tbw = MagicValues.mainViewTopToolBarDefaultWidth;
		int tbh = MagicValues.mainViewTopToolBarDefaultHeight;
		
		topToolBar = new JToolBar(JToolBar.HORIZONTAL);
		topToolBar.setPreferredSize(new Dimension(tbw,tbh));
		topToolBar.setFloatable(false);
		
		buildTopToolBarButtons();
		topToolBarContainer.add(topToolBar);
		
	}
	
	private void buildProgressBar() {
		
		renderingProgressBar = new JProgressBar(getSession().minTimepoint, getSession().getLongestTimepoint());
		String label = "Rendering Progress: ";
		progressBar = new ProgressBar(renderingProgressBar, label);
		getSession().setProgressBar(progressBar);
		
		topToolBarContainer.add(progressBar);
	}
	

	private void buildTopToolBarButtons() {

		try {
			topToolBar.add(ButtonFactory.createBrushButton(mainPanel, session).getSwingComponent());
			topToolBar.add(ButtonFactory.createEraseButton(mainPanel, session).getSwingComponent());
			topToolBar.add(ButtonFactory.createEraseAllButton(session).getSwingComponent());
			topToolBar.add(ButtonFactory.createPlayButton(session).getSwingComponent());
			topToolBar.add(ButtonFactory.createPauseButton(session).getSwingComponent());
			topToolBar.add(ButtonFactory.createColorPickerButton(session).getSwingComponent());
			topToolBar.add(ButtonFactory.createFillButton(session).getSwingComponent());
		} catch(Exception e) {
			System.out.println("Button icon creation failed");
		}
	}
	
	@Override
	public Session getSession() {
		return session;
	}
	
}