package sonar.bagels.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import sonar.bagels.common.tileentity.TileRecyclingDrawer;

public class BlockRecyclingDrawer extends BlockDrawer {

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileRecyclingDrawer();
	}

}
