package sonar.bagels.api;

import net.minecraft.util.IStringSerializable;

public enum DeskPosition implements IStringSerializable {
	LEFT, MIDDLE, RIGHT;

	@Override
	public String getName() {
		return this.name().toLowerCase();
	}
}