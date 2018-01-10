package sonar.bagels.common.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import sonar.bagels.api.DrawerType;
import sonar.core.helpers.NBTHelper.SyncType;

public class TileFluidDrawer extends TileDrawer {

	public int size = Fluid.BUCKET_VOLUME * 4;
	public FluidTank[] tanks = new FluidTank[] { new FluidTank(size), new FluidTank(size) };

	public void onSlaveChanged() {
		shouldRenderSpecials = true;
	}

	@Override
	public SyncType getUpdateTagType() {
		return SyncType.SAVE;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (facing == EnumFacing.UP) {
			if (!this.getWorld().isRemote) {
				int tankID = -1;
				EnumFacing face = EnumFacing.VALUES[getBlockMetadata()];
				if (face.getAxis() == Axis.X) {
					tankID = face == EnumFacing.WEST ? hitZ < 0.5 ? 0 : 1 : hitZ < 0.5 ? 1 : 0;
				} else {
					tankID = face == EnumFacing.SOUTH ? hitX < 0.5 ? 0 : 1 : hitX < 0.5 ? 1 : 0;
				}
				if (tankID != -1) {
					FluidTank targetTank = tanks[tankID];
					FluidUtil.interactWithFluidHandler(player, hand, targetTank);
					sendSyncPacket(player, SyncType.SAVE);
					player.sendMessage(new TextComponentTranslation((targetTank.getFluid() != null ? targetTank.getFluid().getLocalizedName() : "Empty") + ": " + targetTank.getFluidAmount() + " mB"));
				}
			}
			return true;
		} else {
			super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
			return true;
		}
	}

	@Override
	public void readData(NBTTagCompound tag, SyncType type) {
		super.readData(tag, type);
		if (type.isType(SyncType.SAVE)) {
			for (int i = 0; i < tanks.length; i++) {
				tanks[i].readFromNBT(tag.getCompoundTag("tank" + i));
			}
		}
	}

	@Override
	public NBTTagCompound writeData(NBTTagCompound tag, SyncType type) {
		tag = super.writeData(tag, type);
		if (type.isType(SyncType.SAVE)) {
			for (int i = 0; i < tanks.length; i++) {
				NBTTagCompound tankTag = tanks[i].writeToNBT(new NBTTagCompound());
				tag.setTag("tank" + i, tankTag);
			}
		}
		return tag;
	}

	@Override
	public DrawerType getDrawerType() {
		return DrawerType.LARGE;
	}
}
