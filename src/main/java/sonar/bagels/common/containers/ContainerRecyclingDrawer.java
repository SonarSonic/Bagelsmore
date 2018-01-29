package sonar.bagels.common.containers;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRecyclingDrawer extends Container {

	public static class RecyclingSlot extends Slot {

		public RecyclingSlot(int index, int xPosition, int yPosition) {
			super(null, index, xPosition, yPosition);
		}

		@Nullable
		public ItemStack getStack() {
			return ItemStack.EMPTY;
		}

		public boolean getHasStack() {
			return !this.getStack().isEmpty();
		}

		public void putStack(ItemStack stack) {}

		public void onSlotChanged() {}

		public int getSlotStackLimit() {
			return 64;
		}

		public ItemStack decrStackSize(int amount) {
			return ItemStack.EMPTY;
		}

		public boolean isHere(IInventory inv, int slotIn) {
			return slotIn == this.getSlotIndex();
		}
	}

	public ContainerRecyclingDrawer(EntityPlayer player) {
		addSlotToContainer(new RecyclingSlot(0, 80, 34));
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(slotID);

		if ((slot != null) && (slot.getHasStack())) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if ((slotID != 0)) {
				if (!mergeItemStack(itemstack1, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if ((slotID >= 1) && (slotID < 28)) {
				if (!mergeItemStack(itemstack1, 28, 37, false)) {
					return ItemStack.EMPTY;

				} else if ((slotID >= 28) && (slotID < 37) && (!mergeItemStack(itemstack1, 1, 28, false))) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(itemstack1, 1, 37, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0) {
				slot.putStack((ItemStack) ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}
		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
}
