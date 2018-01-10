package sonar.bagels.api;

import net.minecraft.util.EnumFacing;

public enum DeskMetadata {
	LEFT_N(DeskPosition.LEFT, EnumFacing.NORTH),

	LEFT_S(DeskPosition.LEFT, EnumFacing.SOUTH),

	LEFT_W(DeskPosition.LEFT, EnumFacing.WEST),

	LEFT_E(DeskPosition.LEFT, EnumFacing.EAST),

	MIDDLE_N(DeskPosition.MIDDLE, EnumFacing.NORTH),

	MIDDLE_S(DeskPosition.MIDDLE, EnumFacing.SOUTH),

	MIDDLE_W(DeskPosition.MIDDLE, EnumFacing.WEST),

	MIDDLE_E(DeskPosition.MIDDLE, EnumFacing.EAST),

	RIGHT_N(DeskPosition.RIGHT, EnumFacing.NORTH),

	RIGHT_S(DeskPosition.RIGHT, EnumFacing.SOUTH),

	RIGHT_W(DeskPosition.RIGHT, EnumFacing.WEST),

	RIGHT_E(DeskPosition.RIGHT, EnumFacing.EAST);

	private DeskPosition pos;
	private EnumFacing face;

	DeskMetadata(DeskPosition pos, EnumFacing facing) {
		this.pos = pos;
		this.face = facing;
	}

	public int getMeta() {
		DeskMetadata[] values = values();
		for (int i = 0; i < values.length; i++) {
			DeskMetadata map = values[i];
			if (map == this) {
				return i;
			}
		}
		return 0;
	}
	
	public DeskPosition getPosition(){
		return pos;
	}
	
	public EnumFacing getFace(){
		return face;
	}

	public static DeskMetadata getMetaMap(int meta) {
		return values()[meta];
	}

	public static int getMeta(EnumFacing facing, DeskPosition pos) {
		DeskMetadata[] values = values();
		for (int i = 0; i < values.length; i++) {
			DeskMetadata map = values[i];
			if (map.face == facing && map.pos == pos) {
				return i;
			}
		}
		return MIDDLE_N.getMeta();
	}

}
