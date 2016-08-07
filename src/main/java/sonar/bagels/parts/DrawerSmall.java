package sonar.bagels.parts;

import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import sonar.bagels.Bagels;

public class DrawerSmall extends StorageDrawer {

	public DrawerSmall() {
		super();
	}

	public DrawerSmall(DrawerPosition position, EnumFacing face) {
		super(position, face);
	}

	@Override
	public boolean canRenderInLayer(BlockRenderLayer layer) {
		return false;
	}

	@Override
	public DrawerType getDrawerType() {
		return DrawerType.SMALL;
	}

	@Override
	public int getInvSize() {
		return 16;
	}

	@Override
	public ItemStack createItemStack() {
		return new ItemStack(Bagels.smallDrawer, 1);
	}
}
