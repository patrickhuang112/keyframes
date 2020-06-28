package keyframes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class MainView {
	
	private JFrame parent;
	private JPanel mainWindow;
	
	private JPanel topBar;
	private JToolBar topToolbar;
	
	public MainView(JFrame parent) {
		this.parent = parent;
		mainWindow = new JPanel(new BorderLayout());
		this.parent.add(mainWindow, BorderLayout.CENTER);
	}

	public void buildUI() {
		mainWindow.setBackground(Color.blue);
		
		buildTopBar();
		parent.revalidate();
		parent.repaint();
	}
	
	
	private void buildTopBar() {
		topBar = new JPanel();
		topBar.setLayout(new BoxLayout(topBar, BoxLayout.Y_AXIS));
		topBar.setPreferredSize(new Dimension(0,60));
		
		mainWindow.add(topBar, BorderLayout.PAGE_START);
		buildTopMenuBar();
		buildTopToolbar();
	}
	
	private void buildTopMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu firstMenu = new JMenu("TEST");
		menuBar.add(firstMenu);
		menuBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		topBar.add(menuBar);
	}
	
	private void buildTopToolbar() {
		topToolbar = new JToolBar(JToolBar.HORIZONTAL);
		topToolbar.setPreferredSize(new Dimension(0,30));
		topToolbar.setFloatable(false);
		topToolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
		buildTopToolbarButtons();
		topBar.add(topToolbar);
		
	}
	
	private void buildTopToolbarButtons() {
		JButton first = new JButton("first");
		JButton second = new JButton("second");
		first.setEnabled(true);
		topToolbar.add(first);
		topToolbar.add(second);
	}
}