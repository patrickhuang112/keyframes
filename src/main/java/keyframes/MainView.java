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
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.plaf.basic.BasicSliderUI;

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
	
	
	public MainView(JFrame parent, Session session) {
		this.session = session;
		this.parent = parent;
		mainWindow = new JPanel(new BorderLayout());
		this.parent.add(mainWindow, BorderLayout.CENTER);
	}

	public void buildUI() {
		mainWindow.setBackground(Color.blue);
		setKeyMaps();
		buildTopBar();
		buildDrawableAndTimeline();
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
		topBar.setLayout(new BorderLayout());
		topBar.setPreferredSize(new Dimension(0,60));
		topBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainWindow.add(topBar, BorderLayout.PAGE_START);
		buildTopMenuBar();
		buildTopToolBar();
	}
	
	private void buildTopMenuBar() {
		
		topMenuBar = new JMenuBar();
		topMenuBar.setPreferredSize(new Dimension(0,30));
		
		// FILE MENU
		fileMenu = new JMenu("File");
		newProjectMenuItem = new JMenuItem(new AbstractAction("New Project") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Utils.newFile(session, drawableAndTimelinePane);
			}
		});
		openProjectMenuItem = new JMenuItem(new AbstractAction("Open Project") {
			public void actionPerformed(ActionEvent e) {
				Utils.openFile(e, session, openProjectMenuItem, parent);
			}
		});
		
		saveProjectMenuItem = new JMenuItem(new AbstractAction("Save") {

			@Override
			public void actionPerformed(ActionEvent e) {
				Utils.saveFile(e, session, saveProjectMenuItem);
			}
		});
		
		saveProjectAsMenuItem = new JMenuItem(new AbstractAction("Save As..") {
			@Override
			public void actionPerformed(ActionEvent e) {
				Utils.saveAsFile(e, session, saveProjectAsMenuItem);
			}
		});
		
		renderMP4AsMenuItem = new JMenuItem(new AbstractAction("Render MP4 As..") {
			@Override
			public void actionPerformed(ActionEvent e) {
				Utils.renderFile(e, session, renderMP4AsMenuItem, session.getFramesPerSecond());
			}
		});
		
		renderGIFAsMenuItem = new JMenuItem(new AbstractAction("Render GIF As..") {
			@Override
			public void actionPerformed(ActionEvent e) {
				Utils.renderGif(e, session, renderGIFAsMenuItem, session.getFramesPerSecond());
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
				SliderFactory.createFPSDialogSlider(mainWindow, session);
			}
		});
		
		editTimeMenuItem = new JMenuItem(new AbstractAction("Edit Composition Endpoint") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SliderFactory.createCompositionEndpointDialogSlider(mainWindow, session);
			}
		});
		
		editMenu.add(editFpsMenuItem);
		editMenu.add(editTimeMenuItem);
		
		topMenuBar.add(editMenu);
		topBar.add(topMenuBar, BorderLayout.PAGE_START);
	}
	
	private void buildTopToolBar() {
		topToolBar = new JToolBar(JToolBar.HORIZONTAL);
		topToolBar.setPreferredSize(new Dimension(0,30));
		topToolBar.setFloatable(false);
		
		buildTopToolBarButtons();		
		topBar.add(topToolBar, BorderLayout.PAGE_END);
		
	}
	
	private void buildTopToolBarEraserButton() {
		try {
			Image eraserImage = ImageIO.read(this.getClass().getResource("/eraseIcon.bmp"));
			JButton eraseTool = new JButton(new ImageIcon(eraserImage));
			
			eraseTool.setVisible(true);
			eraseTool.setPreferredSize(new Dimension(20,20));
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
		} catch (Exception e) {
			System.out.println("Erase Icon creation failed");
		}
		
	}
	
	private void buildTopToolBarBrushButton() {
		try {
			Image drawImage = ImageIO.read(this.getClass().getResource("/brushIcon.bmp"));
			JButton brushTool = new JButton(new ImageIcon(drawImage));
			
			brushTool.setVisible(true);
			brushTool.setPreferredSize(new Dimension(30,30));
			brushTool.addMouseListener(new MouseAdapter( ) {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isLeftMouseButton(e)) {
						session.setPaintSetting(EnumFactory.PaintSetting.DRAW);
					}
					else if(SwingUtilities.isRightMouseButton(e)) {
						SliderFactory.createBrushSizeDialogSlider(mainWindow, session);
					}
				}
			});
			
			topToolBar.add(brushTool);
		} catch(Exception e) {
			System.out.println("Brush Icon creation failed");
		}
	}
	
	private void buildTopToolBarPlayAndPauseButtons() {
		try {
			Image playImage = ImageIO.read(this.getClass().getResource("/playIcon.bmp"));
			Image pauseImage = ImageIO.read(this.getClass().getResource("/pauseIcon.bmp"));
			
			JButton playTool = new JButton(new ImageIcon(playImage));
			JButton pauseTool = new JButton(new ImageIcon(pauseImage));
			
			playTool.setVisible(true);
			playTool.setPreferredSize(new Dimension(30,30));
			playTool.addMouseListener(new MouseAdapter( ) {
				@Override
				public void mouseClicked(MouseEvent e) {
					getSession().playMovie();
				}
			});
			
			pauseTool.setVisible(true);
			pauseTool.setPreferredSize(new Dimension(30,30));
			pauseTool.addMouseListener(new MouseAdapter( ) {
				@Override
				public void mouseClicked(MouseEvent e) {
					getSession().pauseMovie();
				}
			});
			topToolBar.add(playTool);
			topToolBar.add(pauseTool);
		} catch(Exception e) {
			System.out.println("Play/Pause Icon creation failed");
		}
	}
	
	private void buildTopToolBarEraseAllButton() {
		try {
			Image eraseAllImage = ImageIO.read(this.getClass().getResource("/eraseAllIcon.bmp"));
			
			JButton eraseAllTool = new JButton(new ImageIcon(eraseAllImage));
			
			eraseAllTool.setVisible(true);
			eraseAllTool.setPreferredSize(new Dimension(30,30));
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
						menu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			});
			
			topToolBar.add(eraseAllTool);
		} catch(Exception e) {
			System.out.println("EraseAll Icon creation failed");
		}
	}
	
	private void buildTopToolBarColorPickerButton() {
		try {
			Image colorPickerImage = ImageIO.read(this.getClass().getResource("/colorPickerIcon.bmp"));
			
			JButton colorPickerTool = new JButton(new ImageIcon(colorPickerImage));
			
			colorPickerTool.setVisible(true);
			colorPickerTool.setPreferredSize(new Dimension(30,30));
			colorPickerTool.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Color current = getSession().getBrushColor();
					Color newColor = JColorChooser.showDialog(null, "Choose a color", current);
					getSession().setBrushColor(newColor);
				}
			});
			
			topToolBar.add(colorPickerTool);
		} catch(Exception e) {
			System.out.println("ColorPicker Icon creation failed");
		}
	}
	
	private void buildTopToolBarButtons() {
		try {
			buildTopToolBarBrushButton();
			buildTopToolBarEraserButton();
			buildTopToolBarEraseAllButton();
			buildTopToolBarPlayAndPauseButtons();
			buildTopToolBarColorPickerButton();
		} catch (Exception e) {
			System.out.println("Icon creation failed");
		}
	}
	
	@Override
	public Session getSession() {
		return session;
	}


}