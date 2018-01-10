package sonar.bagels.api;

import mcmultipart.api.slot.EnumEdgeSlot;
import mcmultipart.api.slot.EnumSlotAccess;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public enum EnumCandleSlot implements IPartSlot {
	CANDLE_N(EnumFacing.NORTH), CANDLE_E(EnumFacing.EAST), CANDLE_S(EnumFacing.SOUTH), CANDLE_W(EnumFacing.WEST);

	private final ResourceLocation name;
	public final EnumFacing face;

	private EnumCandleSlot(EnumFacing face) {
		this.name = new ResourceLocation("bagelsmore", name().toLowerCase());
		this.face = face;
	}

	public static EnumCandleSlot fromFace(EnumFacing face){
		for(EnumCandleSlot slot: values()){
			if(slot.face==face){
				return slot;
			}
		}
		return CANDLE_N;
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
		return 500;
	}

	@Override
	public EnumSlotAccess getEdgeAccess(EnumEdgeSlot edge, EnumFacing face) {
		return EnumSlotAccess.MERGE;
	}

	@Override
	public int getEdgeAccessPriority(EnumEdgeSlot edge, EnumFacing face) {
		return 500;
	}

}
