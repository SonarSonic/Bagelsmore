package sonar.bagels.common.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.bagels.api.DrawerType;
import sonar.bagels.client.gui.GuiRecyclingDrawer;
import sonar.bagels.common.containers.ContainerRecyclingDrawer;
import sonar.core.api.IFlexibleGui;

public class TileRecyclingDrawer extends TileDrawer implements IFlexibleGui {

	@Override
	public DrawerType getDrawerType() {
		return DrawerType.SMALL;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking() && facing == EnumFacing.UP) {
			if (!this.getWorld().isRemote) {
				openFlexibleGui(player, 0);
			}
			return true;
		} else {
			return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
		}
	}

	@Override
	public Object getServerElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
		return new ContainerRecyclingDrawer(player);
	}

	@Override
	public Object getClientElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
		return new GuiRecyclingDrawer(player);
	}

}
