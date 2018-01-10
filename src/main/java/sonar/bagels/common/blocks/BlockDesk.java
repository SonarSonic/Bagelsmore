package sonar.bagels.common.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import mcmultipart.api.container.IPartInfo;
import mcmultipart.api.slot.EnumCenterSlot;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sonar.bagels.api.DeskMetadata;
import sonar.bagels.api.DeskPosition;
import sonar.bagels.api.DeskType;
import sonar.bagels.api.IDeskPart;
import sonar.bagels.api.IDrawerContainer;
import sonar.bagels.common.tileentity.TileDesk;
import sonar.bagels.utils.BagelsHelper;
import sonar.core.common.block.SonarMaterials;
import sonar.core.common.block.properties.SonarProperties;
import sonar.core.integration.multipart.BlockFacingMultipart;

public class BlockDesk extends BlockFacingMultipart implements IDeskPart, IDrawerContainer {

	public static final PropertyEnum<DeskPosition> POS = PropertyEnum.<DeskPosition>create("table", DeskPosition.class);

	public DeskType type;

	public BlockDesk(DeskType type) {
		super(SonarMaterials.droppable_wood);
		this.type = type;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileDesk) {
			return ((TileDesk) tile).onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
		}
		return false;
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getStateFromMeta(meta);
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		return true;// implements in DeskItem
	}

	public void destroyAdjacentDesks(TileDesk source, IBlockState state, World world, BlockPos pos) {
		if (source != null && source.shouldHarvestAdjacent) {
			EnumFacing orientation = state.getValue(SonarProperties.ORIENTATION);
			BlockPos middle = source.middle.getCoords().getBlockPos();
			List<BlockPos> positions = BagelsHelper.getDeskPositions(world, middle, orientation);
			for (BlockPos adj : positions) {
				if (!adj.equals(pos)) {
					TileDesk adjDesk = BagelsHelper.getDeskAt(world, adj);
					if (adjDesk != null) {
						adjDesk.shouldHarvestAdjacent = false;
						if (adjDesk.info == null) {
							world.setBlockState(adj, Blocks.AIR.getDefaultState(), world.isRemote ? 11 : 3);
						} else {
							adjDesk.info.getContainer().removePart(EnumCenterSlot.CENTER);
						}
					}
				}
			}
		}
	}

	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		destroyAdjacentDesks(BagelsHelper.getDeskAt(world, pos), state, world, pos);
		super.breakBlock(world, pos, state);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		DeskPosition position = state.getValue(POS);
		if (position == DeskPosition.MIDDLE) {
			return new AxisAlignedBB(0, 1 - (0.0625 * 2), 0, 1, position == DeskPosition.LEFT ? 1 - (0.0625) : 1 - (0.0625 / 2), 1);
		}
		return new AxisAlignedBB(0.01, 0, 0.01, 1-0.01, 1 - (0.0625 / 2), 1-0.01);
	}

	public List<AxisAlignedBB> getOcclusionBoxes(IPartInfo part) {
		return getAllCollisionBoxes(part.getState(), part.getPartWorld(), part.getPartPos());
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		List<AxisAlignedBB> allBoxes = getAllCollisionBoxes(state, world, pos);
		allBoxes.forEach(box -> addCollisionBoxToList(pos, entityBox, collidingBoxes, box));
	}

	public List<AxisAlignedBB> getAllCollisionBoxes(IBlockState state, World world, BlockPos pos) {
		List<AxisAlignedBB> boxes = Lists.newArrayList();
		
		DeskPosition position = state.getValue(POS);
		EnumFacing face = state.getValue(SonarProperties.ORIENTATION);
		double p = 0.0625;
		boxes.add(new AxisAlignedBB(0, 1 - (0.0625 * 2), 0, 1, position == DeskPosition.LEFT ? 1 - (0.0625) : 1 - (0.0625 / 2), 1));
		if (position != DeskPosition.MIDDLE) {
			boxes.add(new AxisAlignedBB(0, 0, 0, 1, 0.0625 / 2, 1));

			if (face != EnumFacing.NORTH)
				boxes.add(new AxisAlignedBB(p * 14, p / 2, p * 2, p, 1 - p, p));
			if (face != EnumFacing.WEST)
				boxes.add(new AxisAlignedBB(p, p / 2, p, p * 2, 1 - p, p * 14));
			if (face != EnumFacing.EAST)
				boxes.add(new AxisAlignedBB(1 - p, p / 2, p, 1 - p * 2, 1 - p, p * 14));
			if (face != EnumFacing.SOUTH)
				boxes.add(new AxisAlignedBB(p * 14, p / 2, 1 - p * 2, p, 1 - p, 1 - p));
		}
	
		return boxes;
	}

	@Override
	public IPartSlot getSlotForPlacement(World world, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ, EntityLivingBase placer) {
		return EnumCenterSlot.CENTER;
	}

	@Override
	public IPartSlot getSlotFromWorld(IBlockAccess world, BlockPos pos, IBlockState state) {
		return EnumCenterSlot.CENTER;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		DeskMetadata map = DeskMetadata.getMetaMap(meta);
		return getDefaultState().withProperty(SonarProperties.ORIENTATION, map.getFace()).withProperty(POS, map.getPosition());
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return DeskMetadata.getMeta(state.getValue(SonarProperties.ORIENTATION), state.getValue(POS));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SonarProperties.ORIENTATION, POS);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return TileDesk.createDesk(DeskMetadata.getMetaMap(meta).getPosition());
	}

	public boolean testIntersection(IPartInfo self, IPartInfo otherPart) {
		if (otherPart.getPart() instanceof BlockDrawer) {
			if (self.getState().getValue(SonarProperties.ORIENTATION) == otherPart.getState().getValue(SonarProperties.ORIENTATION)) {
				return false;
			}
		}
		return super.testIntersection(self, otherPart);
	}

}
