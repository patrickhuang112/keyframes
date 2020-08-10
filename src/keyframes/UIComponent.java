package keyframes;

import javax.swing.JComponent;

public interface UIComponent {
	
	public Session getSession();
	
	public void addChild(UIComponent child);

	public JComponent getMainComponent();
}
