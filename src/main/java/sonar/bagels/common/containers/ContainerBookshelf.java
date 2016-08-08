package sonar.bagels.common.containers;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import sonar.bagels.parts.Bookshelf;

public class ContainerBookshelf extends Container {
	private Bookshelf entity;

	public static class BookSlot extends Slot {

		public BookSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		public boolean isItemValid(@Nullable ItemStack stack) {
			return Bookshelf.isBook(stack);
		}
	}

	public ContainerBookshelf(InventoryPlayer inventory, Bookshelf entity) {
		this.entity = entity;
		for (int j = 0; j < entity.getInvSize(); j++) {
			addSlotToContainer(new BookSlot(entity.inv, j, 8 + 18 + j * 18, 20 + 16));
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
		}
	}

	@Nullable
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < entity.getInvSize()) {
				if (!this.mergeItemStack(itemstack1, entity.getInvSize(), this.inventorySlots.size(), true)) {
					return null;
				}
			} else if (Bookshelf.isBook(itemstack1)) {
				if (!this.mergeItemStack(itemstack1, 0, entity.getInvSize() + 1, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 37, this.inventorySlots.size(), true)) {
				return null;
			}
			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
}
