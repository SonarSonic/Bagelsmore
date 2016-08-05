package sonar.bagels.parts;

import java.util.ArrayList;
import java.util.List;

import mcmultipart.MCMultiPartMod;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.INormallyOccludingPart;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.bagels.Bagels;

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
	public BlockPos middle;
	public boolean doHarvest = false;

	public DeskMultipart() {
		super();
	}

	public DeskMultipart(DeskPosition position, EnumFacing face) {
		super(face);
		this.position = position;
	}

	public DeskMultipart setMiddle(BlockPos pos) {
		middle = pos;
		return this;
	}

	public void breakDesk(EntityPlayer player, PartMOP hit) {
		DeskMultipart middlePart = null;
		if (middle != null) {
			middlePart = this.position == DeskPosition.MIDDLE ? this : getDeskPart(this.getWorld(), middle);
		} else {
			doHarvest = true;
			harvest(player, hit);
		}
		if (middlePart != null) {
			ArrayList<DeskMultipart> toDelete = new ArrayList();
			toDelete.add(middlePart);
			for (EnumFacing face : EnumFacing.HORIZONTALS) {
				DeskMultipart adj = getDeskPart(this.getWorld(), middlePart.getPos().offset(face));
				if (adj != null && adj.middle != null && adj.middle.equals(middlePart.getPos())) {
					toDelete.add(adj);
				}
			}
			if (!toDelete.isEmpty() && toDelete.size() == 3) {
				toDelete.forEach(deskPart -> {
					deskPart.doHarvest = true;
					deskPart.harvest(player, hit);
				});
			}
		}
	}

	@Override
	public void harvest(EntityPlayer player, PartMOP hit) {
		if (!doHarvest) {
			breakDesk(player, hit);
			return;
		}
		ArrayList<IMultipart> toDelete = new ArrayList();
		for (IMultipart part : this.getContainer().getParts()) {
			if (part != this && part instanceof IDeskPart) {
				toDelete.add(part);
			}
		}
		toDelete.forEach(part -> part.harvest(player, hit));
		super.harvest(player, hit);
	}

	public static DeskMultipart getDeskPart(World world, BlockPos pos) {
		return getDeskPart(MultipartHelper.getPartContainer(world, pos));
	}

	public static DeskMultipart getDeskPart(IMultipartContainer container) {
		if (container != null)
			for (IMultipart part : container.getParts()) {
				if (part != null && part instanceof DeskMultipart) {
					return (DeskMultipart) part;
				}
			}
		return null;
	}

	@Override
	public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
		boxes.add(new AxisAlignedBB(0, 1 - (0.0625 * 2), 0, 1, 1 - (0.0625 / 2), 1));

		if (position != DeskPosition.MIDDLE) {
			boxes.add(new AxisAlignedBB(0, 0, 0, 1, 0.0625 / 2, 1));
			double p = 0.0625;
			if (face != EnumFacing.NORTH)
				boxes.add(new AxisAlignedBB(p * 14, p / 2, p * 2, p, 1 - p, p));
			if (face != EnumFacing.WEST)
				boxes.add(new AxisAlignedBB(p, p / 2, p, p * 2, 1 - p, p * 14));
			if (face != EnumFacing.EAST)
				boxes.add(new AxisAlignedBB(1 - p, p / 2, p, 1 - p * 2, 1 - p, p * 14));
			if (face != EnumFacing.SOUTH)
				boxes.add(new AxisAlignedBB(p * 14, p / 2, 1 - p * 2, p, 1 - p, 1 - p));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("position", position.ordinal());
		if (middle != null) {
			tag.setInteger("middleX", middle.getX());
			tag.setInteger("middleY", middle.getY());
			tag.setInteger("middleZ", middle.getZ());
		}
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		position = DeskPosition.values()[tag.getInteger("position")];
		if (tag.hasKey("middleX"))
			middle = new BlockPos(tag.getInteger("middleX"), tag.getInteger("middleY"), tag.getInteger("middleZ"));
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

	@Override
	public ItemStack createItemStack() {
		return DeskPosition.MIDDLE == position ? new ItemStack(Bagels.desk, 1) : null;
	}

}
