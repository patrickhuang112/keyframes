package ui.layer;

import datatypes.Layer;
import keyframes.MagicValues;

public class KFLayerFactory {

	public static KFLayer createNamesLeftRectanglesRightKFLayer(Layer parent) {
		return new NamesLeftRectanglesRightKFLayer(parent);
	}
	
}
