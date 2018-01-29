package sonar.bagels.common.blocks;

import mcmultipart.api.container.IPartInfo;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sonar.bagels.api.IDeskPart;
import sonar.bagels.api.IDrawerContainer;
import sonar.bagels.common.tileentity.TileDrawer;
import sonar.bagels.utils.BagelsHelper;
import sonar.core.common.block.SonarMaterials;
import sonar.core.common.block.properties.SonarProperties;
import sonar.core.helpers.InventoryHelper;
import sonar.core.integration.multipart.BlockFacingMultipart;

public abstract class BlockDrawer extends BlockFacingMultipart implements IDeskPart {

	public BlockDrawer() {
		super(SonarMaterials.droppable_wood);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileDrawer) {
			return ((TileDrawer) tile).onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
		}
		return false;
	}

	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getStateFromMeta(placer.getHorizontalFacing().getOpposite().ordinal());
	}

	@Override
	public IPartSlot getSlotForPlacement(World world, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ, EntityLivingBase placer) {
		return BagelsHelper.getNextDrawerPosition(world, pos, state, facing, hitX, hitY, hitZ, placer);
	}

	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		super.breakBlock(world, pos, state);
		InventoryHelper.dropInventory(world.getTileEntity(pos), world, pos, state);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		TileEntity tile = source.getTileEntity(pos);
		if (tile instanceof TileDrawer) {
			TileDrawer drawer = (TileDrawer) tile;
			EnumFacing face = state.getValue(SonarProperties.ORIENTATION);
			double p = 0.0625;
			double yMin = ((TileDrawer) tile).getDrawerPosition().offsetY() + p + p / 2;
			double yMax = yMin + drawer.getDrawerType().height;

			double start = drawer.isDrawerOpen() ? -0.4 : 0;
			double finish = 1 - p * 2;
			double offset = (1 - drawer.getDrawerType().width) / 2;

			switch (face) {
			case EAST:
				return new AxisAlignedBB(1 - start, yMin, finish, offset, yMax, offset);
			case NORTH:
				return new AxisAlignedBB(offset, yMin, start, 1 - offset, yMax, finish);
			case SOUTH:
				return new AxisAlignedBB(finish, yMin, 1 - start, offset, yMax, offset);
			case WEST:
				return new AxisAlignedBB(finish, yMin, finish, start, yMax, offset);
			default:
				break;
			}
		}
		return super.getBoundingBox(state, source, pos);
	}

	public boolean testIntersection(IPartInfo self, IPartInfo otherPart) {
		if (otherPart.getPart() instanceof IDrawerContainer) {
			if (self.getState().getValue(SonarProperties.ORIENTATION) == otherPart.getState().getValue(SonarProperties.ORIENTATION)) {
				return false;
			}
		}
		return super.testIntersection(self, otherPart);
	}

}
