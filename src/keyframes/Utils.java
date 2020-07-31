package keyframes;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.JComponent;
import javax.swing.JFileChooser;

public class Utils {

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
				System.out.println("Loading aborted");
			}
			
		} catch (Exception ex) {
			System.out.println("Error loading file");
			session = new Session();
		}
	}
}
