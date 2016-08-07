package sonar.bagels.parts;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import sonar.bagels.Bagels;
import sonar.bagels.client.gui.GuiStorageDrawer;
import sonar.bagels.common.containers.ContainerStorageDrawer;

public class DrawerLarge extends StorageDrawer implements IGuiPart {

	public DrawerLarge() {
		super();
	}

	public DrawerLarge(DrawerPosition position, EnumFacing face) {
		super(position, face);
	}

	@Override
	public DrawerType getDrawerType() {
		return DrawerType.LARGE;
	}

	@Override
	public int getInvSize() {
		return 32;
	}

	@Override
	public ItemStack createItemStack() {
		return new ItemStack(Bagels.largeDrawer, 1);
	}

}
