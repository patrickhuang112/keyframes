package ui.layer;

import datatypes.Layer;
import keyframes.MagicValues;

public class KFLayerFactory {

	public static KFLayer createStandardKFLayer(Layer parent) {
		return new StandardKFLayer(parent);
	}
	
}
