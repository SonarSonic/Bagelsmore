package sonar.bagels.common.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.bagels.api.DrawerType;
import sonar.bagels.client.gui.GuiStorageDrawer;
import sonar.bagels.common.containers.ContainerStorageDrawer;
import sonar.core.api.IFlexibleGui;
import sonar.core.helpers.NBTHelper.SyncType;
import sonar.core.api.inventories.ISonarInventory;
import sonar.core.api.inventories.ISonarInventoryTile;
import sonar.core.inventory.SonarInventoryTile;

public abstract class TileStorageDrawer extends TileDrawer implements IFlexibleGui, ISonarInventoryTile {

	public static class Large extends TileStorageDrawer{

		public Large() {
			super();
			inv = new SonarInventoryTile(this, 32);
			syncList.addParts(inv);
		}

		@Override
		public DrawerType getDrawerType() {
			return DrawerType.LARGE;
		}
		
	}

	public static class Small extends TileStorageDrawer{

		public Small() {
			super();
			inv = new SonarInventoryTile(this, 16);
			syncList.addParts(inv);
		}

		@Override
		public DrawerType getDrawerType() {
			return DrawerType.SMALL;
		}
		
	}

	protected ISonarInventory inv;
	
	public ISonarInventory inv() {
		return inv; 
	}

	@Override
	public SyncType getUpdateTagType() {
		return SyncType.SAVE;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking() && facing == EnumFacing.UP) {
			if (!this.getWorld().isRemote){
				openFlexibleGui(player, 0);
			}
			return true;
		} else {
			return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
		}
	}

	@Override
	public Object getServerElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
		return new ContainerStorageDrawer(this, player);
	}

	@Override
	public Object getClientElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
		return new GuiStorageDrawer(this);
	}

}
