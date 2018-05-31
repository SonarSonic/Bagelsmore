package sonar.bagels.common.containers;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import sonar.core.inventory.SonarInventory;

public class ContainerDeskCrafting extends Container {
	private final SonarInventory inv;

	public CustomInventoryCrafting craftMatrix;
	public CustomCraftResult craftResult;
	public EntityPlayer player;

	public ContainerDeskCrafting(EntityPlayer player, SonarInventory inv) {
		this.inv = inv;
		this.craftMatrix = new CustomInventoryCrafting(inv, this, 3, 3);
		this.craftResult = new CustomCraftResult(inv, 9);
		this.player = player;

		for (int i = 0; i < 3; i++) {
			for (int k = 0; k < 3; k++) {
				addSlotToContainer(new Slot(this.craftMatrix, k + i * 3, 30 + k * 18, 17 + i * 18));
			}
		}

		addSlotToContainer(new SlotCrafting(player, this.craftMatrix, this.craftResult, 10, 124, 35));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
		}

		this.onCraftMatrixChanged(null);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inv) {
		this.craftResult.setInventorySlotContents(9, CraftingManager.findMatchingResult(this.craftMatrix, player.getEntityWorld()));
		this.inv.markChanged();
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 9) {
				if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 10 && index < 37) {
				if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 37 && index < 46) {
				if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
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

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	@Override
	public ItemStack slotClick(int slot, int drag, ClickType click, EntityPlayer player) {
		//if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItemMainhand()) {
		//	return null;
		//}
		return super.slotClick(slot, drag, click, player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}



	public static class CustomCraftResult extends InventoryCraftResult {
		public SonarInventory inv;
		public int slot;

		public CustomCraftResult(SonarInventory tileDeskCraftingPart, int slot) {
			this.inv = tileDeskCraftingPart;
			this.slot = slot;
		}

		@Nullable
		public ItemStack getStackInSlot(int index) {
			return inv.getStackInSlot(slot);
		}

		public ItemStack decrStackSize(int index, int count) {
			return ItemStackHelper.getAndRemove(inv.slots(), slot);
		}

		public ItemStack removeStackFromSlot(int index) {
			return ItemStackHelper.getAndRemove(inv.slots(), slot);
		}

		public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
			inv.getWrapperInventory().setInventorySlotContents(slot, stack);
			inv.getWrapperInventory().markDirty();
		}

		public void clear() {
			inv.getWrapperInventory().setInventorySlotContents(slot, null);
		}
	}

	public static class CustomInventoryCrafting extends InventoryCrafting {
		public SonarInventory inv;
		public Container container;

		public CustomInventoryCrafting(SonarInventory inv, Container container, int width, int height) {
			super(container, width, height);
			this.inv = inv;
			this.container = container;
		}

		public int getSizeInventory() {
			return 9;
		}

		@Nullable
		public ItemStack getStackInSlot(int index) {
			return index >= this.getSizeInventory() ? null : this.inv.getStackInSlot(index);
		}

		@Nullable
		public ItemStack removeStackFromSlot(int index) {
			return ItemStackHelper.getAndRemove(inv.slots(), index);
		}

		@Nullable
		public ItemStack decrStackSize(int index, int count) {
			ItemStack itemstack = ItemStackHelper.getAndSplit(inv.slots(), index, count);
			if (itemstack != null) {
				this.container.onCraftMatrixChanged(this);
			}
			return itemstack;
		}

		public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
			this.inv.getWrapperInventory().setInventorySlotContents(index, stack);
			this.container.onCraftMatrixChanged(this);
		}

		public void clear() {
			for (int i = 0; i < this.getSizeInventory(); ++i) {
				inv.getWrapperInventory().setInventorySlotContents(i, ItemStack.EMPTY);
			}
		}
	}

}
