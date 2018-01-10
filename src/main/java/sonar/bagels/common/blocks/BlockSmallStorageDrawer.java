package sonar.bagels.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import sonar.bagels.common.tileentity.TileStorageDrawer;

public class BlockSmallStorageDrawer extends BlockDrawer {

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileStorageDrawer.Small();
	}

}
