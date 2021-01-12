package ui.button;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import factories.EnumFactory;
import keyframes.Session;
import ui.DrawablePanel;
import ui.dialog.DialogFactory;

public class EraseAllButton extends JButton implements Button {

	private static int menuPopupOffset = 5;
	
	EraseAllButton(Session session) throws IOException {
		Image eraserImage = ImageIO.read(this.getClass().getResource("/eraseAllIcon.png"));
		setIcon(new ImageIcon(eraserImage));
		
		setVisible(true);
		setPreferredSize(new Dimension(Button.mvw, Button.mvh));
		addMouseListener(new MouseAdapter( ) {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					//OLD
					/*
					DrawablePanel drawPane = (DrawablePanel)drawableAndTimelinePane.getTopOrLeft().getMainComponent();
					drawPane.clearAll();
					*/
					//NEW
					//Change it to a controller/session function
				}
				else if(SwingUtilities.isRightMouseButton(e)) {
					JPopupMenu menu = new JPopupMenu();
					JMenuItem eraseAllLayersAtCurrentTimeMenuItem 
						= new JMenuItem(new AbstractAction("Erase all layer frames at current time") {
						@Override
						public void actionPerformed(ActionEvent e) {
							session.eraseAllLayersAtCurrentFrame();
						}
					});
					menu.add(eraseAllLayersAtCurrentTimeMenuItem);
					
					//Very arbitrary right now just to make it look good
					int offset = EraseAllButton.menuPopupOffset;
					menu.show(e.getComponent(), e.getX() + offset, e.getY() + offset);
				}
			}
		});
	}
	
	@Override
	public JButton getSwingComponent() {
		return this;
	}
	
}
