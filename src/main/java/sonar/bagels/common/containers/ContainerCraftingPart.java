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
import sonar.bagels.parts.DeskCraftingPart;

public class ContainerCraftingPart extends Container {
	private final DeskCraftingPart craftingPart;

	public CustomInventoryCrafting craftMatrix;
	public CustomCraftResult craftResult;
	public EntityPlayer player;

	public static class CustomCraftResult extends InventoryCraftResult {
		public DeskCraftingPart part;
		public int slot;

		public CustomCraftResult(DeskCraftingPart part, int slot) {
			this.part = part;
			this.slot = slot;
		}

		@Nullable
		public ItemStack getStackInSlot(int index) {
			return part.inv.getStackInSlot(slot);
		}

		public ItemStack decrStackSize(int index, int count) {
			return ItemStackHelper.getAndRemove(part.inv.getStacks(), slot);
		}

		public ItemStack removeStackFromSlot(int index) {
			return ItemStackHelper.getAndRemove(part.inv.getStacks(), slot);
		}

		public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
			part.inv.setInventorySlotContents(slot, stack);
			part.markPartDirty();
		}

		public void clear() {
			part.inv.setInventorySlotContents(slot, null);
		}
	}

	public static class CustomInventoryCrafting extends InventoryCrafting {
		public DeskCraftingPart part;
		public Container container;

		public CustomInventoryCrafting(DeskCraftingPart part, Container container, int width, int height) {
			super(container, width, height);
			this.part = part;
			this.container = container;
		}

		public int getSizeInventory() {
			return 9;
		}

		@Nullable
		public ItemStack getStackInSlot(int index) {
			return index >= this.getSizeInventory() ? null : this.part.inv.getStackInSlot(index);
		}

		@Nullable
		public ItemStack removeStackFromSlot(int index) {
			return ItemStackHelper.getAndRemove(part.inv.getStacks(), index);
		}

		@Nullable
		public ItemStack decrStackSize(int index, int count) {
			ItemStack itemstack = ItemStackHelper.getAndSplit(part.inv.getStacks(), index, count);
			if (itemstack != null) {
				this.container.onCraftMatrixChanged(this);
			}
			return itemstack;
		}

		public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
			this.part.inv.setStackInSlot(index, stack);
			this.container.onCraftMatrixChanged(this);
		}

		public void clear() {
			for (int i = 0; i < this.getSizeInventory(); ++i) {
				part.inv.setInventorySlotContents(i, null);
			}
		}
	}

	public ContainerCraftingPart(EntityPlayer player, DeskCraftingPart craftingPart) {
		this.craftingPart = craftingPart;
		this.craftMatrix = new CustomInventoryCrafting(craftingPart, this, 3, 3);
		this.craftResult = new CustomCraftResult(craftingPart, 9);
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
		this.craftResult.setInventorySlotContents(9, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, player.worldObj));
		this.craftingPart.markPartDirty();
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 9) {
				if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 10 && index < 37) {
				if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
					return null;
				}
			} else if (index >= 37 && index < 46) {
				if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(playerIn, itemstack1);
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

}
