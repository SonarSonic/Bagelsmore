package sonar.bagels.common.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.bagels.api.DrawerType;

public class TileEnderDrawer extends TileDrawer {

	@Override
	public DrawerType getDrawerType() {
		return DrawerType.SMALL;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking() && facing == EnumFacing.UP) {
			if (!this.getWorld().isRemote) {
				player.displayGUIChest(player.getInventoryEnderChest());
			}
			return true;
		} else {
			return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
		}
	}

}
