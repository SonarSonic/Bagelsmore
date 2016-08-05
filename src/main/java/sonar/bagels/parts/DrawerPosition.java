package sonar.bagels.parts;

public enum DrawerPosition {
	LARGE_BOTTOM, LARGE_TOP, SMALL_BOTTOM, SMALL_MIDDLE, SMALL_TOP, NONE;

	public static DrawerPosition[] large_drawer = new DrawerPosition[] { LARGE_BOTTOM, LARGE_TOP };
	public static DrawerPosition[] small_drawer = new DrawerPosition[] { SMALL_BOTTOM, SMALL_MIDDLE, SMALL_TOP };

	public double offsetY() {
		double p = 0.0625;
		double minY = 0;
		switch (this) {
		case LARGE_BOTTOM:
			return minY;
		case LARGE_TOP:
			return minY + p * 4;
		case SMALL_BOTTOM:
			return minY;
		case SMALL_MIDDLE:
			return minY + p * 4;
		case SMALL_TOP:
			return minY + p * 8;
		default:
			break;
		}
		return p / 2;
	}

	public boolean isValidPosition() {
		return this != NONE;
	}
}