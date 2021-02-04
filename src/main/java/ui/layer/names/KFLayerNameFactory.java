package ui.layer.names;

import datatypes.Layer;

public class KFLayerNameFactory {
	
	public static KFLayerName createStandardKFLayerNames(Layer layer) {
		return new StandardKFLayerName(layer);
	}
	
}
