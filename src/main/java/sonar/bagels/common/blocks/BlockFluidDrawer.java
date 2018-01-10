package sonar.bagels.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import sonar.bagels.common.tileentity.TileFluidDrawer;

public class BlockFluidDrawer extends BlockDrawer {

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileFluidDrawer();
	}

}
