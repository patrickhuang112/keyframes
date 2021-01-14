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

import ui.progressbar.ProgressBar;
import datatypes.SessionObject;
import factories.EnumFactory;
import keyframes.Controller;
import keyframes.MagicValues;
import keyframes.Session;
import keyframes.Utils;
import settings.Settings;
import ui.button.ButtonFactory;
import ui.canvas.KFCanvas;
import ui.canvas.KFCanvasFactory;
import ui.dialog.CompositionLengthDialog;
import ui.dialog.DialogFactory;
import ui.dialog.FPSDialog;
import ui.dialog.SettingsDialog;
import ui.progressbar.ProgressBarFactory;
import ui.slider.SliderFactory;
import ui.splitpane.SplitPane;
import ui.splitpane.SplitPaneFactory;
import ui.timeline.Timeline;
import ui.timeline.TimelineFactory;


public class MainView extends JPanel implements Serializable{
	
	private static final long serialVersionUID = -4390147842958702501L;

	private JFrame window;
	private JPanel mainPanel;
	
	private JPanel topBar;
	private JMenuBar topMenuBar;
	// Contains topToolBar and progressBar
	private JPanel topToolBarContainer;
	private JToolBar topToolBar;
	
	private SplitPane splitPane;
	
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

	// UI Element Getters
	public SplitPane getSplitPaneManager() {
		return splitPane;
	}
	
	public ProgressBar getProgressBar() {
		return progressBar;
	}
	
	private MainView() {
		super();
		setLayout(new BorderLayout());
		
		
		mainPanel = new JPanel(new BorderLayout());
		window.add(mainPanel, BorderLayout.CENTER);
		
		Settings settings = Utils.getSettings();
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
				Controller.getController().copyFramesFromCurrentLayerAndCurrentTime();
			}
		});
		// Paste copied frames into current layer
		mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control V"), "pasted");
		mainPanel.getActionMap().put("pasted", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getController().pasteFramesToCurrentLayerAndCurrentTime();
			}
		});
		
		// Play and pause movie
		mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "movieplaypause");
		mainPanel.getActionMap().put("movieplaypause", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getController().playOrPauseMovie();
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
		KFCanvas canvas = KFCanvasFactory.createStandardKFCanvas();
		Timeline timeline = TimelineFactory.createStandardTimeline();

		splitPane = SplitPaneFactory.createHorizontalSplitPane(canvas, timeline);
		mainPanel.add(splitPane.getSwingComponent());
	}
	
	private void buildTopBar() {
		
		
		mainPanel.add(topBar, BorderLayout.PAGE_START);
		buildTopMenuBar();
		buildTopToolBarContainer();
	}
	
	private void buildTopMenuBar() {
		
		topBar.add(topMenuBar, BorderLayout.PAGE_START);
	}
	
	private void buildTopToolBarContainer() {
		
		
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
		
		ProgressBar pb = ProgressBarFactory.createRenderingProgressBar();
		topToolBarContainer.add(pb.getSwingComponent());
	}
	

	private void buildTopToolBarButtons() {

		
	}
	
}