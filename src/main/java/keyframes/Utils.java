package keyframes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.jcodec.api.awt.AWTSequenceEncoder;

import external.GifSequenceWriter;
import settings.Settings;


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
	
	public static void newFile(Session oldSession, JFrame frame) {
		// DO STUFF WITH OLD SESSION (NOT IMPLEMENTED YET)
		
		frame.getContentPane().removeAll();
		Session newSession = new Session();
		MainView mv = new MainView(frame, newSession);
		mv.buildUI();
		
		System.out.println("New project created");
	}
	
	public static void openFile(ActionEvent e, Session oldSession, JComponent parent, JFrame frame) {
		// DO STUFF WITH OLD SESSION (NOT IMPLEMENTED YET)
		
		Session newSession;
		try {
			System.out.println("Loading project...");
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(parent);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				if(Utils.getExtension(file).equals("ser")) {
					FileInputStream fileIn = new FileInputStream(file);
			        ObjectInputStream in = new ObjectInputStream(fileIn);
			        newSession = (Session) in.readObject();
			        in.close();
			        fileIn.close();
			        frame.getContentPane().removeAll();
					MainView mv = new MainView(frame, newSession);
					mv.buildUI();
				} else {
					System.out.println("Incompatible file");
				}
			} else {
				System.out.println("Loading aborted");
			}
			
		} catch (Exception ex) {
			System.out.println("Error loading file");
		}
		System.out.println("Loaded project");
	}
	
	public static void saveAsFile(ActionEvent e, Session session, JComponent parent) {
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
	
	public static void saveFile(ActionEvent ae, Session session, JComponent parent) {
		if(session.getSavePath() == null) {
			Utils.saveAsFile(ae, session, parent);
		} else {
			try {
				FileOutputStream fos = new FileOutputStream(session.getSavePath());
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				// write object to file
				oos.writeObject(session);
				System.out.println("Saving Successful!");
				// closing resources
				oos.close();
				fos.close();
			} catch (Exception e) {
				System.out.println("SAVING FAILED");
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static void renderFile(ActionEvent e, Session session, JComponent parent, int fps) {
		try {
			System.out.println("Rendering video...");
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
				System.out.println("Rendering video finished!");
				
			} else {
				System.out.println("Rendering video aborted");
			}
			
		} catch (Exception ex) {
			System.out.println("Error rendering video");
			session = new Session();
		}
	}
	
	public static void renderGif(ActionEvent e, Session session, JComponent parent, int fps) {
		try {
			System.out.println("Rendering gif...");
			final JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Render gif");
			int returnVal = fc.showSaveDialog(parent);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				String filePath = file.getAbsolutePath() + ".gif";
				File newFile = new File(filePath);
				
				HashMap<Integer, BufferedImage> images = session.getImages();
				ImageOutputStream output = new FileImageOutputStream(newFile);
				
				ArrayList<BufferedImage> finalImages = new ArrayList<>();
				
				
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
					finalImages.add(img);
				}
				
				int timeBetweenFrames = 1000 / fps;
				
				GifSequenceWriter writer = 
						new GifSequenceWriter(output, finalImages.get(0).getType(), timeBetweenFrames, true);
			      
			    // create a gif sequence with the type of the first image, 1 second
			    // between frames, which loops continuously
			    
			      
			    // write out the first image to our sequence...
			    writer.writeToSequence(finalImages.get(0));
			    for (int i=1; i < finalImages.size(); i++) {
			        BufferedImage nextImage = finalImages.get(i);
			        writer.writeToSequence(nextImage);
			    }
			      
			    writer.close();
			    output.close();
			    System.out.println("Rendering gif finished");
				
			} else {
				System.out.println("Rendering gif aborted");
			}
			
		} catch (Exception ex) {
			System.out.println("Error rendering");
			session = new Session();
		}
	}
}
