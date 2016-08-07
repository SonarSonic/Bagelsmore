package sonar.bagels.common.containers;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import sonar.bagels.parts.Paper;

public class ContainerTodoList extends Container {
	private final Paper multipart;

	public ContainerTodoList(Paper multipart) {
		this.multipart = multipart;
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Nullable
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);
		return itemstack;
	}

}