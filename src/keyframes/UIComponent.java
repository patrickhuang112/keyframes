package keyframes;

import javax.swing.JComponent;

public interface UIComponent {
	
	public void addToParent(UIComponent parent);
	
	public void addChild(UIComponent child);

	public JComponent getMainComponent();
}
