package sonar.bagels.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import sonar.bagels.client.gui.GuiBookshelf;
import sonar.bagels.common.containers.ContainerBookshelf;
import sonar.bagels.utils.BagelsHelper;
import sonar.core.api.IFlexibleGui;
import sonar.core.integration.multipart.SonarMultipartHelper;
import sonar.core.integration.multipart.TileInventoryMultipart;
import sonar.core.inventory.SonarInventoryTile;
import sonar.core.network.sync.SyncTagType;

public class TileBookshelf extends TileInventoryMultipart implements IFlexibleGui {

	public SyncTagType.INT books = new SyncTagType.INT("books");

	public TileBookshelf() {
		inv = new SonarInventoryTile(this, 7);
		syncList.addParts(books, inv);
	}
	
	public void markDirty() {
		if (this.isServer()) {
			int newBookCount = 0;
			for (int i = 0; i < getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (BagelsHelper.isBook(inv.getStackInSlot(i))) {
					newBookCount++;
				}
			}
			books.setObject(newBookCount);
			SonarMultipartHelper.sendMultipartUpdateSyncAround(this, 64);
		}
		super.markDirty();
	}

	@Override
	public Object getServerElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
		return new ContainerBookshelf(player.inventory, this);
	}

	@Override
	public Object getClientElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
		return new GuiBookshelf(this);
	}

}
