package sonar.bagels.parts;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public abstract class InventoryMultipart extends BagelsMultipart {

	public BasicInventory inv = new BasicInventory(this, getInvSize());

	public abstract int getInvSize();

	public InventoryMultipart() {
		super();
	}

	public InventoryMultipart(EnumFacing face) {
		super(face);
	}

	public ItemStack[] getStacks() {
		return inv.getStacks();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if (!disableInv())
			tag.setTag("inventory", inv.serializeNBT());
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (!disableInv())
			inv.deserializeNBT(tag.getCompoundTag("inventory"));
	}

	@Override
	public void writeUpdatePacket(PacketBuffer buf) {
		super.writeUpdatePacket(buf);
		if (!disableInv())
			ByteBufUtils.writeTag(buf, inv.serializeNBT());
	}

	@Override
	public void readUpdatePacket(PacketBuffer buf) {
		super.readUpdatePacket(buf);
		if (!disableInv())
			inv.deserializeNBT(ByteBufUtils.readTag(buf));
	}

	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = super.getDrops();
		if (!disableInv())
			for (int i = 0; i < getInvSize(); i++) {
				if (inv.getStackInSlot(i) != null) {
					list.add(inv.getStackInSlot(i));
				}
			}
		return list;
	}

	public void markPartDirty() {
		this.markDirty();
		if (this instanceof StorageDrawer || this instanceof DeskCraftingPart || this instanceof Bookshelf) {
			this.sendUpdatePacket();
		}
	}

	public boolean disableInv() {
		return false;
	}
}
