package sonar.bagels.common.containers;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryEnderDrawer extends InventoryBasic {

	public InventoryEnderDrawer() {
		super("container.enderchest", false, 27);
	}

	public void loadInventoryFromNBT(NBTTagList p_70486_1_) {
		for (int i = 0; i < this.getSizeInventory(); ++i) {
			this.setInventorySlotContents(i, (ItemStack) null);
		}

		for (int k = 0; k < p_70486_1_.tagCount(); ++k) {
			NBTTagCompound nbttagcompound = p_70486_1_.getCompoundTagAt(k);
			int j = nbttagcompound.getByte("Slot") & 255;

			if (j >= 0 && j < this.getSizeInventory()) {
				this.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound));
			}
		}
	}

	public NBTTagList saveInventoryToNBT() {
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.getSizeInventory(); ++i) {
			ItemStack itemstack = this.getStackInSlot(i);

			if (itemstack != null) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) i);
				itemstack.writeToNBT(nbttagcompound);
				nbttaglist.appendTag(nbttagcompound);
			}
		}

		return nbttaglist;
	}
}