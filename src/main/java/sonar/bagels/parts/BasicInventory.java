package sonar.bagels.parts;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.ItemStackHandler;

public class BasicInventory extends ItemStackHandler implements IInventory {
	final InventoryMultipart multipart;

	public BasicInventory(InventoryMultipart multipart, int size) {
		this.multipart = multipart;
		this.stacks = new ItemStack[size];
	}

	@Override
	public String getName() {
		return multipart.getName();
	}

	@Override
	public boolean hasCustomName() {
		return multipart.hasCustomName();
	}

	@Override
	public ITextComponent getDisplayName() {
		return multipart.getDisplayName();
	}

	@Override
	public void markDirty() {
		multipart.markPartDirty();
	}

	public int getSizeInventory() {
		return this.stacks.length;
	}

	public ItemStack getStackInSlot(int slot) {
		if (slot < stacks.length)
			return this.stacks[slot];
		else
			return null;
	}

	public ItemStack decrStackSize(int slot, int var2) {
		if (this.stacks[slot] != null) {
			if (this.stacks[slot].stackSize <= var2) {
				ItemStack itemstack = this.stacks[slot];
				this.stacks[slot] = null;
				return itemstack;
			}
			ItemStack itemstack = this.stacks[slot].splitStack(var2);

			if (this.stacks[slot].stackSize == 0) {
				this.stacks[slot] = null;
			}
			return itemstack;
		}

		return null;
	}

	public ItemStack removeStackFromSlot(int i) {
		if (this.stacks[i] != null) {
			ItemStack itemstack = this.stacks[i];
			this.stacks[i] = null;
			return itemstack;
		}
		return null;
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.stacks[i] = itemstack;

		if ((itemstack != null) && (itemstack.stackSize > getInventoryStackLimit())) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	public void openInventory(EntityPlayer player) {
	}

	public void closeInventory(EntityPlayer player) {
	}

	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}

	public int getField(int id) {
		return 0;
	}

	public void setField(int id, int value) {
	}

	public int getFieldCount() {
		return 0;
	}

	public void clear() {
		for (int i = 0; i < this.getSizeInventory(); i++)
			this.setInventorySlotContents(i, null);
	}
}
