package keyframes;

public class Session {
	public Session() {
		super();
	}
	
	private EnumFactory.PaintSetting paintSetting = EnumFactory.PaintSetting.DRAW;
	
	public void setPaintSetting(EnumFactory.PaintSetting paintSetting) {
		this.paintSetting = paintSetting;
	}
	
	public EnumFactory.PaintSetting getPaintSetting() {
		return this.paintSetting;
	}
}
