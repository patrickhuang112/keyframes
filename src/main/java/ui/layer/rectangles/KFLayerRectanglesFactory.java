package ui.layer.rectangles;

import datatypes.Layer;
import ui.layer.KFLayer;

public class KFLayerRectanglesFactory {
	public static KFLayerRectangles createStandardKFLayerRectangles(Layer layer) {
		return new StandardKFLayerRectangles(layer);
	}
}
