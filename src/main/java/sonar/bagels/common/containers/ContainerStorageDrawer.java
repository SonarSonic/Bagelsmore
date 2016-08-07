package sonar.bagels.common.containers;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import sonar.bagels.parts.InventoryMultipart;

//edited Container chest hehe
public class ContainerStorageDrawer extends Container {
	private final InventoryMultipart multipart;

	public ContainerStorageDrawer(InventoryMultipart multipart, EntityPlayer player) {
		this.multipart = multipart;
		int numLayers = multipart.getInvSize() / 16;
		
		for (int layer = 0; layer < numLayers; layer++) {
			for (int j = 0; j < 4; ++j) {
				for (int k = 0; k < 4; ++k) {
					this.addSlotToContainer(new Slot(multipart.inv, (k + j * 4) + (layer*16), (8 + k * 18) + (multipart.getInvSize()==32 ? (layer*90): 45), 18 + j * 18));
				}
			}
		}

		for (int l = 0; l < 3; ++l) {
			for (int j1 = 0; j1 < 9; ++j1) {
				this.addSlotToContainer(new Slot(player.inventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + (l * 18)+1));
			}
		}

		for (int i1 = 0; i1 < 9; ++i1) {
			this.addSlotToContainer(new Slot(player.inventory, i1, 8 + i1 * 18, 161+1));
		}
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Nullable
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < multipart.getInvSize()) {
				if (!this.mergeItemStack(itemstack1, multipart.getInvSize(), this.inventorySlots.size(), true)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, multipart.getInvSize(), false)) {
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

}