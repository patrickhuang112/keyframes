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
import javax.swing.plaf.basic.BasicSliderUI;

import datatypes.ProgressBar;
import datatypes.SessionObject;
import factories.EnumFactory;
import factories.SliderFactory;


public class MainView implements SessionObject, Serializable{
	
	private static final long serialVersionUID = -4390147842958702501L;

	private Session session;
	
	private JFrame parent;
	private JPanel mainWindow;
	
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
	
	private JMenuItem editFpsMenuItem;
	private JMenuItem editTimeMenuItem;
	private JMenuItem editBackgroundColorItem;
	
	private JProgressBar renderingProgressBar;
	private ProgressBar progressBar;
	
	public MainView(JFrame parent, Session session) {
		this.session = session;
		this.parent = parent;
		mainWindow = new JPanel(new BorderLayout());
		this.parent.add(mainWindow, BorderLayout.CENTER);
	}

	public void buildUI() {
		mainWindow.setBackground(Color.blue);
		buildDrawableAndTimeline();
		buildTopBar();
		setKeyMaps();
		parent.revalidate();
		parent.repaint();
		
	}
	
	public void setKeyMaps() {
		// Copy frames from current layer
		mainWindow.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control C"), "copied");
		mainWindow.getActionMap().put("copied", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getSession().copyFramesFromCurrentLayerAndCurrentTime();
			}
		});
		// Paste copied frames into current layer
		mainWindow.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control V"), "pasted");
		mainWindow.getActionMap().put("pasted", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getSession().pasteFramesToCurrentLayerAndCurrentTime();
			}
		});
		
		// Play and pause movie
		mainWindow.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "movieplaypause");
		mainWindow.getActionMap().put("movieplaypause", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getSession().playOrPauseMovie();
			}
		});
		
		mainWindow.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S"), "save");
		mainWindow.getActionMap().put("save", new AbstractAction() {
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
		
		
		
	
		mainWindow.add(drawableAndTimelinePane.getMainComponent());
	}
	
	private void buildTopBar() {
		
		int tw = MagicValues.mainViewTopBarDefaultWidth;
		int th = MagicValues.mainViewTopBarDefaultHeight;
		
		topBar = new JPanel();
		topBar.setLayout(new BorderLayout());
		topBar.setPreferredSize(new Dimension(tw,th));
		topBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainWindow.add(topBar, BorderLayout.PAGE_START);
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
				Utils.newFile(getSession(), parent);
			}
		});
		openProjectMenuItem = new JMenuItem(new AbstractAction("Open Project") {
			public void actionPerformed(ActionEvent e) {
				Utils.openFile(e, getSession(), openProjectMenuItem, parent);
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
			
		fileMenu.add(newProjectMenuItem);
		fileMenu.add(openProjectMenuItem);
		fileMenu.add(saveProjectMenuItem);
		fileMenu.add(saveProjectAsMenuItem);
		fileMenu.add(renderMP4AsMenuItem);
		fileMenu.add(renderGIFAsMenuItem);
		
		topMenuBar.add(fileMenu);
		
		// EDIT MENU
		editMenu = new JMenu("Edit");
		
		editFpsMenuItem = new JMenuItem(new AbstractAction("Edit Frames Per Second") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SliderFactory.createFPSDialogSlider(mainWindow, getSession());
			}
		});
		
		editTimeMenuItem = new JMenuItem(new AbstractAction("Edit Composition Endpoint") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SliderFactory.createCompositionEndpointDialogSlider(mainWindow, getSession());
			}
		});
		
		editBackgroundColorItem = new JMenuItem(new AbstractAction("Edit Background Color") {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color current = getSession().getDrawablePanelBackgroundColor();
				Color newColor = JColorChooser.showDialog(null, "Choose a new background color", current);
				if (newColor != null) {
					getSession().setDrawablePanelBackgroundColor(newColor);
					getSession().setEraserColor(newColor);
				}
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
		
		int iW = MagicValues.mainViewIconsDefaultWidth;
		int iH = MagicValues.mainViewIconsDefaultHeight;
		
		try {
			Image eraserImage = ImageIO.read(this.getClass().getResource("/eraseIcon.png"));
			JButton eraseTool = new JButton(new ImageIcon(eraserImage));
			
			eraseTool.setVisible(true);
			eraseTool.setPreferredSize(new Dimension(iW,iH));
			eraseTool.addMouseListener(new MouseAdapter( ) {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isLeftMouseButton(e)) {
						session.setPaintSetting(EnumFactory.PaintSetting.ERASE);
					}
					else if(SwingUtilities.isRightMouseButton(e)) {
						SliderFactory.createEraserSizeDialogSlider(mainWindow, session);
					}
				}
			});
			
			topToolBar.add(eraseTool);
		
			Image drawImage = ImageIO.read(this.getClass().getResource("/drawIcon.png"));
			JButton brushTool = new JButton(new ImageIcon(drawImage));
			
			brushTool.setVisible(true);
			brushTool.setPreferredSize(new Dimension(iW,iH));
			brushTool.addMouseListener(new MouseAdapter( ) {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isLeftMouseButton(e)) {
						getSession().setPaintSetting(EnumFactory.PaintSetting.DRAW);
					}
					else if(SwingUtilities.isRightMouseButton(e)) {
						SliderFactory.createBrushSizeDialogSlider(mainWindow, getSession());
					}
				}
			});
			
			topToolBar.add(brushTool);
		
			
		
			Image playImage = ImageIO.read(this.getClass().getResource("/playIcon.png"));
			Image pauseImage = ImageIO.read(this.getClass().getResource("/pauseIcon.png"));
			
			JButton playTool = new JButton(new ImageIcon(playImage));
			JButton pauseTool = new JButton(new ImageIcon(pauseImage));
			
			playTool.setVisible(true);
			playTool.setPreferredSize(new Dimension(iW,iH));
			playTool.addMouseListener(new MouseAdapter( ) {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isLeftMouseButton(e)) {
						getSession().playMovie();
					}
				}
			});
			
			pauseTool.setVisible(true);
			pauseTool.setPreferredSize(new Dimension(iW,iH));
			pauseTool.addMouseListener(new MouseAdapter( ) {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isLeftMouseButton(e)) {
						getSession().pauseMovie();
					}
				}
			});
			topToolBar.add(playTool);
			topToolBar.add(pauseTool);
		
	
		
			Image eraseAllImage = ImageIO.read(this.getClass().getResource("/eraseAllIcon.png"));
			
			JButton eraseAllTool = new JButton(new ImageIcon(eraseAllImage));
			
			eraseAllTool.setVisible(true);
			eraseAllTool.setPreferredSize(new Dimension(iW,iH));
			eraseAllTool.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isLeftMouseButton(e)) {
						DrawablePanel drawPane = (DrawablePanel)drawableAndTimelinePane.getTopOrLeft().getMainComponent();
						drawPane.clearAll();
					}
					else if(SwingUtilities.isRightMouseButton(e)) {
						JPopupMenu menu = new JPopupMenu();
						JMenuItem eraseAllLayersAtCurrentTimeMenuItem 
							= new JMenuItem(new AbstractAction("Erase all layer frames at current time") {
							@Override
							public void actionPerformed(ActionEvent e) {
								getSession().eraseAllLayersAtCurrentFrame();
							}
						});
						menu.add(eraseAllLayersAtCurrentTimeMenuItem);
						
						//Very arbitrary right now just to make it look good
						int offset = 5;
						menu.show(e.getComponent(), e.getX() + offset, e.getY() + offset);
					}
				}
			});
			
			topToolBar.add(eraseAllTool);
		
		
	
			Image colorPickerImage = ImageIO.read(this.getClass().getResource("/colorPickerIcon.png"));
			
			JButton colorPickerTool = new JButton(new ImageIcon(colorPickerImage));
			
			colorPickerTool.setVisible(true);
			colorPickerTool.setPreferredSize(new Dimension(iW,iH));
			colorPickerTool.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isLeftMouseButton(e)) {
						Color current = getSession().getBrushColor();
						Color newColor = JColorChooser.showDialog(null, "Choose a color", current);
						if (newColor != null) {
							getSession().setBrushColor(newColor);
						}
					}
				}
			});
			
			topToolBar.add(colorPickerTool);
			
			Image fillImage = ImageIO.read(this.getClass().getResource("/fillIcon.png"));
			
			JButton fillTool = new JButton(new ImageIcon(fillImage));
			
			fillTool.setVisible(true);
			fillTool.setPreferredSize(new Dimension(iW,iH));
			fillTool.addMouseListener(new MouseAdapter( ) {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isLeftMouseButton(e)) {
						getSession().setPaintSetting(EnumFactory.PaintSetting.FILLSINGLE);
					}
				}
			});
			
			topToolBar.add(fillTool);
		} catch(Exception e) {
			System.out.println("Icon creation failed");
		}
	}
	
	@Override
	public Session getSession() {
		return session;
	}
	
}