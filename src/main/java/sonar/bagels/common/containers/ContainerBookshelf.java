package sonar.bagels.common.containers;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import sonar.bagels.common.tileentity.TileBookshelf;
import sonar.bagels.utils.BagelsHelper;
import sonar.core.inventory.ContainerSonar;
import sonar.core.inventory.TransferSlotsManager;

public class ContainerBookshelf extends ContainerSonar {
	private TileBookshelf entity;

	public static TransferSlotsManager<TileBookshelf> transfer = new TransferSlotsManager<TileBookshelf>() {
		{
			addTransferSlot(new TransferSlots<TileBookshelf>(TransferType.TILE_INV, 7) {
				public boolean canInsert(EntityPlayer player, TileBookshelf inv, Slot slot, int pos, int slotID, ItemStack stack) {
					return BagelsHelper.isBook(stack);
				}
			});
			addPlayerInventory();
		}
	};

	public ContainerBookshelf(InventoryPlayer inventory, TileBookshelf entity) {
		this.entity = entity;
		for (int j = 0; j < entity.getSizeInventory(); j++) {
			addSlotToContainer(new Slot(entity.inv(), j, 8 + 18 + j * 18, 20 + 16));
		}
		addInventory(inventory, 8, 84);
	}

	@Nullable
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
		return transfer.transferStackInSlot(this, entity, player, slotID);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
}
