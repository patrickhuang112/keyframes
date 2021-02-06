package ui.button;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;


abstract class AbstractKFButton extends JButton implements KFButton{

	protected static final int mvw = 30;
	protected static final int mvh = 30;
	
	AbstractKFButton(String iconPath, MouseListener action) throws IOException  {
		super();
		Image drawImage = ImageIO.read(this.getClass().getResource(iconPath));
		setIcon(new ImageIcon(drawImage));
		setVisible(true);
		setPreferredSize(new Dimension(AbstractKFButton.mvw, AbstractKFButton.mvh));
		addMouseListener(action);
	}
	
	@Override
	public JButton getSwingComponent() {
		return this;
	}
}
