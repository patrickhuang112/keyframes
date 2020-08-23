package keyframes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFileChooser;

import org.jcodec.api.awt.AWTSequenceEncoder;


public class Utils {

	public static Settings getSettings() {
		Settings settings;
		try {
			String file = Settings.settingsPath;
			FileInputStream fileIn = new FileInputStream(file);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        settings = (Settings)in.readObject();
	        System.out.println("Successfully loaded settings");
	        in.close();
	        fileIn.close();
		} catch (Exception e) {
			System.out.println("Settings don't preexist");
			settings = new Settings();
		}
		return settings;
		
	}
	
	public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
	
	public static void saveFile(ActionEvent e, Session session, JComponent parent) {
		try {
			System.out.println("Loading project...");
			final JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Save session");
			int returnVal = fc.showSaveDialog(parent);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				String savePath = file.getAbsolutePath() + ".ser";
				
				session.setSavePath(savePath);
				
				FileOutputStream fos = new FileOutputStream(savePath);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				// write object to file
				oos.writeObject(session);
				System.out.println("Saving Successful!");
				// closing resources
				oos.close();
				fos.close();
				
			} else {
				System.out.println("Saving aborted");
			}
			
		} catch (Exception ex) {
			System.out.println("Error saving file");
			session = new Session();
		}
	}
	
	public static void renderFile(ActionEvent e, Session session, JComponent parent, int fps) {
		try {
			System.out.println("Rendering...");
			final JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Render video");
			int returnVal = fc.showSaveDialog(parent);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				String filePath = file.getAbsolutePath() + ".mp4";
				File newFile = new File(filePath);
				AWTSequenceEncoder enc = AWTSequenceEncoder.createSequenceEncoder(newFile, fps);
				HashMap<Integer, BufferedImage> images = session.getImages();
				
				for(int i = 0; i < session.getLongestTimepoint(); i++) {
					BufferedImage img;
					if(images.containsKey(i)) {
						img = images.get(i);
					} else {
						int w = session.getDrawablePaneWidth();
						int h = session.getDrawablePaneHeight();
						
						img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
						Graphics2D g = img.createGraphics();
						g.setPaint(session.getDrawablePanelBackgroundColor());
						g.fillRect(0,0, w, h);
					}
					enc.encodeImage(img);
				}
				enc.finish();
				System.out.println("Rendering finished!");
				
			} else {
				System.out.println("Rendering aborted");
			}
			
		} catch (Exception ex) {
			System.out.println("Error rendering");
			session = new Session();
		}
	}
}
