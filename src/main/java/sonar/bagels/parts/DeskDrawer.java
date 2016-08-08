package sonar.bagels.parts;

import java.util.List;

import mcmultipart.multipart.MultipartHelper;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.bagels.parts.DeskMultipart.DeskPosition;
import sonar.bagels.utils.DrawerPosition;
import sonar.bagels.utils.DrawerType;
import sonar.bagels.utils.IDeskDrawer;

public abstract class DeskDrawer extends InventoryMultipart implements IDeskDrawer, ITickable {

	private DrawerPosition position;
	private boolean isOpen;
	private DeskMultipart host;
	public boolean shouldRenderSpecials = true;
	public boolean wasLoaded = false;

	public DeskDrawer() {
		super();
	}

	public void update() {
		if (!this.getWorld().isRemote && !this.wasLoaded) {
			this.onAdded();
			wasLoaded = true;
		}
	}

	public DeskDrawer(DrawerPosition position, EnumFacing face) {
		super(face);
		this.position = position;
	}

	public DeskDrawer setHost(DeskMultipart host) {
		host.addSlave(this);
		return this;
	}

	public void alertHost() {
		if (!this.getWorld().isRemote && this.host != null) {
			host.onSlaveChanged();
		}
	}

	public void onSlaveChanged() {
		boolean oldValue = shouldRenderSpecials;
		if (this.isDrawerOpen()) {
			shouldRenderSpecials = true;
		} else {
			DrawerPosition aboveSlot = this.getDrawerPosition().getAboveSlot();
			if (aboveSlot != DrawerPosition.NONE) {
				IDeskDrawer drawer = DeskMultipart.getDrawerInPosition(host, aboveSlot);
				if (drawer != null ) {
					shouldRenderSpecials = false;
				}else{
					shouldRenderSpecials = true;
				}
			} else {
				shouldRenderSpecials = true;
			}
		}
		//System.out.println(shouldRenderSpecials);
		if (oldValue != this.shouldRenderSpecials) {
			this.sendUpdatePacket();
		}
	}

	public void onAdded() {
		if (this.host == null) {
			DeskMultipart host = DeskMultipart.getDeskPart(getWorld(), getPos());
			if (host != null) {
				this.host = host;
				host.addSlave(this);
			}
		}
	}

	public void onRemoved() {
		super.onRemoved();
		DeskMultipart host = this.host == null ? DeskMultipart.getDeskPart(getWorld(), getPos()) : this.host;
		if (host != null) {
			host.removeSlave(this);
		}
	}

	@Override
	public boolean isDrawerOpen() {
		return isOpen;
	}

	@Override
	public void setDrawerOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	@Override
	public DrawerPosition getDrawerPosition() {
		return position;
	}

	@Override
	public void setDrawerPosition(DrawerPosition position) {
		this.position = position;
	}

	@Override
	public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
		double p = 0.0625;
		double yMin = getDrawerPosition().offsetY() + p + p / 2;
		double yMax = yMin + getDrawerType().height;

		double start = isDrawerOpen() ? -0.4 : 0;
		double finish = 1 - p * 2;
		double offset = (1 - getDrawerType().width) / 2;

		switch (face) {
		case EAST:
			boxes.add(new AxisAlignedBB(1 - start, yMin, finish, offset, yMax, offset));
			break;
		case NORTH:
			boxes.add(new AxisAlignedBB(offset, yMin, start, 1 - offset, yMax, finish));
			break;
		case SOUTH:
			boxes.add(new AxisAlignedBB(finish, yMin, 1 - start, offset, yMax, offset));
			break;
		case WEST:
			boxes.add(new AxisAlignedBB(finish, yMin, finish, start, yMax, offset));
			break;
		default:
			break;

		}
	}

	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
		if (hit.sideHit == face || player.isSneaking()) {
			if (!this.getWorld().isRemote) {
				this.isOpen = !isOpen;
				this.alertHost();
				this.sendUpdatePacket();
			} else {
				this.getWorld().playSound(player, this.getPos(), SoundEvents.BLOCK_FENCE_GATE_CLOSE, SoundCategory.NEUTRAL, 0.3F, 0.1F);
			}
		}
		return true;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("position", position.ordinal());
		tag.setBoolean("isOpen", isOpen);
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		position = DrawerPosition.values()[tag.getInteger("position")];
		isOpen = tag.getBoolean("isOpen");
	}

	@Override
	public void writeUpdatePacket(PacketBuffer buf) {
		super.writeUpdatePacket(buf);
		buf.writeBoolean(this.shouldRenderSpecials);
		buf.writeInt(position.ordinal());
		buf.writeBoolean(isOpen);
	}

	@Override
	public void readUpdatePacket(PacketBuffer buf) {
		super.readUpdatePacket(buf);
		shouldRenderSpecials = buf.readBoolean();
		position = DrawerPosition.values()[buf.readInt()];
		isOpen = buf.readBoolean();
	}

	@Override
	public boolean canRenderInLayer(BlockRenderLayer layer) {
		return false;
	}

	public static DrawerPosition getValidDrawerPosition(World world, BlockPos pos, IDeskDrawer drawer, EnumFacing front) {
		DeskMultipart deskPart = null;
		if ((deskPart = DeskMultipart.getDeskPart(world, pos)) != null && deskPart.position != DeskPosition.MIDDLE) {
			FakeDrawer fakeDrawer = new FakeDrawer(drawer.getDrawerType(), DrawerPosition.NONE, front);
			DrawerPosition[] positions = fakeDrawer.getDrawerType().getValidPositions();
			for (int i = 0; i < positions.length; i++) {
				fakeDrawer.setDrawerPosition(positions[i]);
				if (MultipartHelper.canAddPart(world, pos, fakeDrawer)) {
					return positions[i];
				}
			}
		}
		return DrawerPosition.NONE;

	}

	public static class FakeDrawer extends DeskDrawer {
		public DrawerType type;

		public FakeDrawer(DrawerType type, DrawerPosition position, EnumFacing face) {
			super(position, face);
			this.type = type;
		}

		@Override
		public DrawerType getDrawerType() {
			return type;
		}

		@Override
		public int getInvSize() {
			return 1;
		}

		@Override
		public ItemStack createItemStack() {
			return null;
		}

	}

}
