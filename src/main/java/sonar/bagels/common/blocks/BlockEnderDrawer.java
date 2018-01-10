package sonar.bagels.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import sonar.bagels.common.tileentity.TileEnderDrawer;

public class BlockEnderDrawer extends BlockDrawer {

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEnderDrawer();
	}

}
