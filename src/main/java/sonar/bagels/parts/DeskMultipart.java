package sonar.bagels.parts;

import java.util.ArrayList;
import java.util.List;

import mcmultipart.MCMultiPartMod;
import mcmultipart.multipart.INormallyOccludingPart;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;

public class DeskMultipart extends SidedMultipart implements INormallyOccludingPart {

	public static enum DeskPosition implements IStringSerializable {
		LEFT, MIDDLE, RIGHT;

		@Override
		public String getName() {
			return this.name().toLowerCase();
		}
	}

	public static final PropertyEnum<DeskPosition> POS = PropertyEnum.<DeskPosition>create("table", DeskPosition.class);
	public DeskPosition position;

	public DeskMultipart() {
		super();
	}

	public DeskMultipart(DeskPosition position, EnumFacing face) {
		super(face);
		this.position = position;
	}

	@Override
	public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
		boxes.add(new AxisAlignedBB(0, 1 - (0.0625*2), 0, 1, 1 - (0.0625 / 2), 1));
		if (position != DeskPosition.MIDDLE) {
			boxes.add(new AxisAlignedBB(0, 0, 0, 1, 0.0625 / 2, 1));
			double p = 0.0625;

			if (face != EnumFacing.NORTH)
				boxes.add(new AxisAlignedBB(p * 14, p / 2, p * 2, p, 1 - p, p));
			if (face != EnumFacing.SOUTH)
				boxes.add(new AxisAlignedBB(p, p / 2, p, p * 2, 1 - p, p * 14));
			if (face != EnumFacing.EAST)
				boxes.add(new AxisAlignedBB(1 - p, p / 2, p, 1 - p * 2, 1 - p, p * 14));
			if (face != EnumFacing.WEST)
				boxes.add(new AxisAlignedBB(p * 14, p / 2, 1 - p * 2, p, 1 - p, 1 - p));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("position", position.ordinal());
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		position = DeskPosition.values()[tag.getInteger("position")];
	}

	@Override
	public void writeUpdatePacket(PacketBuffer buf) {
		super.writeUpdatePacket(buf);
		buf.writeInt(position.ordinal());
	}

	@Override
	public void readUpdatePacket(PacketBuffer buf) {
		super.readUpdatePacket(buf);
		position = DeskPosition.values()[buf.readInt()];
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(MCMultiPartMod.multipart, FACING, POS);
	}

	@Override
	public IBlockState getActualState(IBlockState state) {
		return state.withProperty(FACING, face).withProperty(POS, position);
	}

}
