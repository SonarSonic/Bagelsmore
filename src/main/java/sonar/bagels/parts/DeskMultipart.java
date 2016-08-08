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
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.bagels.Bagels;
import sonar.bagels.utils.DeskType;
import sonar.bagels.utils.DrawerPosition;
import sonar.bagels.utils.IDeskDrawer;
import sonar.bagels.utils.IDeskPart;
import sonar.bagels.utils.TodoList;

public abstract class DeskMultipart extends BagelsMultipart implements INormallyOccludingPart {

	public ArrayList<IDeskDrawer> drawers = new ArrayList();

	public static enum DeskPosition implements IStringSerializable {
		LEFT, MIDDLE, RIGHT;

		@Override
		public String getName() {
			return this.name().toLowerCase();
		}
	}

	public static final PropertyEnum<DeskPosition> POS = PropertyEnum.<DeskPosition>create("table", DeskPosition.class);
	public DeskType type;
	public DeskPosition position;
	public BlockPos middle;
	public boolean doHarvest = false;

	public static class Fancy extends DeskMultipart {

		public Fancy() {
			super();
		}

		public Fancy(DeskPosition position, EnumFacing face) {
			super(DeskType.FANCY, position, face);
		}

		@Override
		public ItemStack createItemStack() {
			return new ItemStack(Bagels.deskFancy);
		}

	}

	public static class Stone extends DeskMultipart {

		public Stone() {
			super();
		}

		public Stone(DeskPosition position, EnumFacing face) {
			super(DeskType.STONE, position, face);
		}

		@Override
		public ItemStack createItemStack() {
			return new ItemStack(Bagels.deskStone);
		}

	}

	public static class Treated extends DeskMultipart {

		public Treated() {
			super();
		}

		public Treated(DeskPosition position, EnumFacing face) {
			super(DeskType.TREATED, position, face);
		}

		@Override
		public ItemStack createItemStack() {
			return new ItemStack(Bagels.deskTreated);
		}

	}

	public DeskMultipart() {
		super();
	}

	public DeskMultipart(DeskType type, DeskPosition position, EnumFacing face) {
		super(face);
		this.type = type;
		this.position = position;
	}

	public DeskMultipart setMiddle(BlockPos pos) {
		middle = pos;
		return this;
	}

	public void addSlave(IDeskDrawer drawer) {
		if (!drawers.contains(drawer)) {
			drawers.add(drawer);
			this.onSlaveChanged();
		}
	}

	public void removeSlave(IDeskDrawer drawer) {
		drawers.remove(drawer);
		this.onSlaveChanged();
	}

	public static DeskMultipart createDeskMultipart(DeskType type, DeskPosition position, EnumFacing face) {
		switch (type) {
		case FANCY:
			return new DeskMultipart.Fancy(position, face);
		case STONE:
			return new DeskMultipart.Stone(position, face);
		case TREATED:
			return new DeskMultipart.Treated(position, face);
		default:
			return null;

		}
	}

	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
		if (!this.getWorld().isRemote) {
			if (heldItem != null) {
				if (position == DeskPosition.MIDDLE && hit.sideHit == EnumFacing.UP) {
					if (heldItem.getItem() == Items.PAPER) {
						Paper multipart = new Paper(this.face);
						if (MultipartHelper.canAddPart(this.getWorld(), this.getPos(), multipart)) {
							if (heldItem.hasTagCompound()) {
								multipart.list = TodoList.getListFromStack(heldItem);
							}
							MultipartHelper.addPart(this.getWorld(), this.getPos(), multipart);
							heldItem.stackSize--;
						}
					} else if (heldItem.getItem() == Bagels.clipboard) {
						Paper multipart = new Paper(this.face);
						if (MultipartHelper.canAddPart(this.getWorld(), this.getPos(), multipart)) {
							multipart.list = TodoList.getListFromStack(heldItem);
							MultipartHelper.addPart(this.getWorld(), this.getPos(), multipart);
							player.setHeldItem(hand, new ItemStack(Bagels.clipboardEmpty, 1));
						}
					}
					
				}
			}
		}
		return true;
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
		ArrayList<BagelsMultipart> toDelete = new ArrayList();
		for (IMultipart part : this.getContainer().getParts()) {
			if (part != this && part instanceof BagelsMultipart) {
				toDelete.add((BagelsMultipart) part);
			}
		}
		toDelete.forEach(part -> part.defaultHarvest(player, hit));
		super.harvest(player, hit);
	}

	public static IDeskDrawer getDrawerInPosition(DeskMultipart desk, DrawerPosition position) {
		for (IDeskDrawer drawer : desk.drawers) {
			if (!drawer.wasRemoved() && drawer.getDrawerPosition() == position) {
				return drawer;
			}
		}
		return null;
	}

	public void onSlaveChanged() {
		drawers.forEach(drawer -> drawer.onSlaveChanged());
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
		boxes.add(new AxisAlignedBB(0, 1 - (0.0625 * 2), 0, 1, position == DeskPosition.LEFT ? 1 - (0.0625) : 1 - (0.0625 / 2), 1));

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
	public List<ItemStack> getDrops() {
		ArrayList<ItemStack> drops = new ArrayList();
		if (position == DeskPosition.MIDDLE) {
			ItemStack stack = this.createItemStack();
			if (stack != null) {
				drops.add(stack);
			}
		}
		return drops;
	}
}
