package sonar.bagels.common.tileentity;

import java.util.Optional;

import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.multipart.MultipartHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.bagels.api.DrawerPosition;
import sonar.bagels.api.IDeskDrawer;
import sonar.core.integration.multipart.TileSonarMultipart;
import sonar.core.network.sync.SyncTagType;

public abstract class TileDrawer extends TileSonarMultipart implements IDeskDrawer {

	private SyncTagType.BOOLEAN isOpen = new SyncTagType.BOOLEAN(0);
	public boolean shouldRenderSpecials = true;
	public boolean wasLoaded = false;

	public TileDrawer() {
		super();
		syncList.addPart(isOpen);
	}

	public DrawerPosition getDrawerPosition() {
		if (this.info != null && info.getSlot() instanceof DrawerPosition) {
			return (DrawerPosition) info.getSlot();
		}
		return DrawerPosition.NONE;
	}

	public void onNeighbouringDrawerChanged() {
		boolean oldValue = shouldRenderSpecials;
		if (this.isDrawerOpen()) {
			shouldRenderSpecials = true;
		} else {
			DrawerPosition aboveSlot = getDrawerPosition().getAboveSlot();
			if (aboveSlot != DrawerPosition.NONE) {
				Optional<IMultipartTile> drawer = MultipartHelper.getPartTile(world, pos, aboveSlot);
				if (!drawer.isPresent()) {
					shouldRenderSpecials = false;
				} else {
					shouldRenderSpecials = true;
				}
			} else {
				shouldRenderSpecials = true;
			}
		}
		if (oldValue != this.shouldRenderSpecials) {
			markBlockForUpdate();
		}
	}

	@Override
	public boolean isDrawerOpen() {
		return isOpen.getObject();
	}

	@Override
	public void setDrawerOpen(boolean isOpen) {
		this.isOpen.setObject(isOpen);
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!isOpen.getObject() || player.isSneaking()) {
			if (!this.getWorld().isRemote) {
				this.isOpen.invert();
				this.sendSyncPacket(player);
			} else {
				this.getWorld().playSound(player, this.getPos(), SoundEvents.BLOCK_FENCE_GATE_CLOSE, SoundCategory.NEUTRAL, 0.3F, 0.1F);
			}
			if(!player.isSneaking()){
				return state.getBlock().onBlockActivated(world, pos, state, player, hand, EnumFacing.UP, hitX, hitY, hitZ);
			}
		}
		return true;
	}

}