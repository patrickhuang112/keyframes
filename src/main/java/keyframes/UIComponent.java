package keyframes;

import javax.swing.JComponent;

public interface UIComponent {
	
	// Gets the current drawing session object
	public Session getSession();
	
	// Adds a child UI Component to the existing UI Component
	public void addChild(UIComponent child);

	// Gets the main JComponent of this UI Component
	public JComponent getMainComponent();
}
