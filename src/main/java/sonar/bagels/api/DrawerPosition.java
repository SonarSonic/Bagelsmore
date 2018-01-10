package sonar.bagels.api;

import mcmultipart.api.slot.EnumEdgeSlot;
import mcmultipart.api.slot.EnumSlotAccess;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public enum DrawerPosition implements IPartSlot {
	LARGE_BOTTOM, LARGE_TOP, SMALL_BOTTOM, SMALL_MIDDLE, SMALL_TOP, NONE;

	public static DrawerPosition[] large_drawer = new DrawerPosition[] { LARGE_BOTTOM, LARGE_TOP };
	public static DrawerPosition[] small_drawer = new DrawerPosition[] { SMALL_BOTTOM, SMALL_MIDDLE, SMALL_TOP };

	private final ResourceLocation name;

	private DrawerPosition() {
		this.name = new ResourceLocation("bagelsmore", name().toLowerCase());
	}

	@Override
	public ResourceLocation getRegistryName() {
		return name;
	}

	@Override
	public EnumSlotAccess getFaceAccess(EnumFacing face) {
		return EnumSlotAccess.MERGE;
	}

	@Override
	public int getFaceAccessPriority(EnumFacing face) {
		return 100000;
	}

	@Override
	public EnumSlotAccess getEdgeAccess(EnumEdgeSlot edge, EnumFacing face) {
		return EnumSlotAccess.MERGE;
	}

	@Override
	public int getEdgeAccessPriority(EnumEdgeSlot edge, EnumFacing face) {
		return 100000;
	}
	
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

	public DrawerPosition getAboveSlot() {
		switch (this) {
		case SMALL_BOTTOM:
			return SMALL_MIDDLE;
		case SMALL_MIDDLE:
		case LARGE_BOTTOM:
			return SMALL_TOP;
		default:
			return NONE;
		}
	}

	public boolean isValidPosition() {
		return this != NONE;
	}
}