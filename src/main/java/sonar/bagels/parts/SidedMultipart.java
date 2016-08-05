package sonar.bagels.parts;

import java.util.ArrayList;
import java.util.List;

import mcmultipart.MCMultiPartMod;
import mcmultipart.multipart.INormallyOccludingPart;
import mcmultipart.multipart.Multipart;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import sonar.bagels.parts.DeskMultipart.DeskPosition;

public abstract class SidedMultipart extends Multipart implements INormallyOccludingPart {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public EnumFacing face;

	public SidedMultipart() {
	}

	public SidedMultipart(EnumFacing face) {
		this.face = face;
	}

	public void addSelectionBoxes(List<AxisAlignedBB> list) {
		this.addOcclusionBoxes(list);
	}

	@Override
	public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
		ArrayList<AxisAlignedBB> boxes = new ArrayList();
		addSelectionBoxes(boxes);
		boxes.forEach(box -> {
			if(box.intersectsWith(mask)){
				list.add(box);
			}			
		});
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("face", face.ordinal());
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		face = EnumFacing.VALUES[tag.getInteger("face")];
	}

	@Override
	public void writeUpdatePacket(PacketBuffer buf) {
		buf.writeInt(face.ordinal());
	}

	@Override
	public void readUpdatePacket(PacketBuffer buf) {
		face = EnumFacing.VALUES[buf.readInt()];
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(MCMultiPartMod.multipart, FACING);
	}

	@Override
	public IBlockState getActualState(IBlockState state) {
		return state.withProperty(FACING, face);
	}
}
