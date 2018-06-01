package sonar.bagels.common.tileentity;

import net.minecraft.item.ItemStack;
import sonar.core.handlers.inventories.SonarInventoryTile;
import sonar.core.helpers.NBTHelper.SyncType;
import sonar.core.integration.multipart.TileInventoryMultipart;

public class TileSwordMount extends TileInventoryMultipart {

	public TileSwordMount() {
		inv = new SonarInventoryTile(this, 1);
		syncList.addParts(inv);
	}

	public ItemStack getSword() {
		return getStackInSlot(0);
	}

	@Override
	public SyncType getUpdateTagType() {
		return SyncType.SAVE;
	}
}
