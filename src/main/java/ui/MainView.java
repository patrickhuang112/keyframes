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
import datatypes.Enums;
import datatypes.SessionObject;
import keyframes.Controller;
import keyframes.Session;
import keyframes.Utils;
import settings.Settings;
import ui.button.KFButtonFactory;
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
import ui.timeline.KFTimeline;
import ui.timeline.KFTimelineFactory;


public class MainView extends JPanel {
	
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
	
	private MainView() {
		super();
		setLayout(new BorderLayout());
		setKeyMaps();
	}
	
	private void setKeyMaps() {
		// Copy frames from current layer
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control C"), "copied");
		getActionMap().put("copied", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getController().copyFramesFromCurrentLayerAndCurrentTime();
			}
		});
		// Paste copied frames into current layer
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control V"), "pasted");
		getActionMap().put("pasted", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getController().pasteFramesToCurrentLayerAndCurrentTime();
			}
		});
		
		// Play and pause movie
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "movieplaypause");
		getActionMap().put("movieplaypause", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getController().playOrPauseMovie();
			}
		});
		
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S"), "save");
		getActionMap().put("save", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getController().saveProject();
			}
		});
		
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Z"), "undo");
		getActionMap().put("undo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getController().undoCommand();
			}
		});
		
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Y"), "redo");
		getActionMap().put("redo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getController().redoCommand();
			}
		});
		
		
	}
}