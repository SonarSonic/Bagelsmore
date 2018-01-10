package sonar.bagels.common.blocks;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sonar.bagels.common.tileentity.TileSmeltingDrawer;
import sonar.core.common.block.properties.SonarProperties;

public class BlockSmeltingDrawer extends BlockDrawer {

	public static final PropertyBool active = PropertyBool.create("on");

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SonarProperties.ORIENTATION, active);
	}

	@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos){
		TileEntity tile = world.getTileEntity(pos);
		boolean isBurning = false;
		if(tile instanceof TileSmeltingDrawer){
			isBurning = ((TileSmeltingDrawer) tile).isBurning();
		}
		return state.withProperty(active, isBurning);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileSmeltingDrawer();
	}

}
