package sonar.bagels.utils;

public enum DrawerType {
	LARGE(8 * 0.0625, 12 * 0.062), SMALL(4 * 0.0625, 12 * 0.0625);
	
	public double height, width;	
	
	DrawerType(double height, double width){
		this.height=height;
		this.width=width;
	}
	
	public DrawerPosition[] getValidPositions() {
		switch (this) {
		case LARGE:
			return DrawerPosition.large_drawer;
		case SMALL:
			return DrawerPosition.small_drawer;
		default:
			return new DrawerPosition[0];
		}
	}
}
