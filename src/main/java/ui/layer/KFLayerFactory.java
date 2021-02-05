package ui.layer;

import datatypes.Layer;

public class KFLayerFactory {

	public static KFLayer createNamesLeftRectanglesRightKFLayer(Layer parent) {
		return new NamesLeftRectanglesRightKFLayer(parent);
	}
	
}
