package sonar.bagels.common.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import mcmultipart.api.container.IPartInfo;
import mcmultipart.api.slot.EnumCenterSlot;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sonar.bagels.api.DeskPosition;
import sonar.bagels.api.DeskType;
import sonar.bagels.api.IDeskDrawer;
import sonar.bagels.api.IDeskPart;
import sonar.bagels.api.IDrawerContainer;
import sonar.bagels.common.tileentity.TileDesk;
import sonar.bagels.common.tileentity.TileCabinet;
import sonar.core.common.block.SonarMaterials;
import sonar.core.common.block.properties.SonarProperties;
import sonar.core.integration.multipart.BlockFacingMultipart;

public class BlockCabinet extends BlockFacingMultipart implements IDeskPart, IDrawerContainer {

	public DeskType type;

	public BlockCabinet(DeskType type) {
		super(SonarMaterials.droppable_wood);
		this.type = type;
	}	

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
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
		EnumFacing face = state.getValue(SonarProperties.ORIENTATION);
		double p = 0.0625;
		boxes.add(new AxisAlignedBB(0, 1 - (0.0625 * 2), 0, 1, 1 - (0.0625 / 2), 1));
		boxes.add(new AxisAlignedBB(0, 0, 0, 1, 0.0625 / 2, 1));

		if (face != EnumFacing.NORTH)
			boxes.add(new AxisAlignedBB(p * 14, p / 2, p * 2, p, 1 - p, p));
		if (face != EnumFacing.WEST)
			boxes.add(new AxisAlignedBB(p, p / 2, p, p * 2, 1 - p, p * 14));
		if (face != EnumFacing.EAST)
			boxes.add(new AxisAlignedBB(1 - p, p / 2, p, 1 - p * 2, 1 - p, p * 14));
		if (face != EnumFacing.SOUTH)
			boxes.add(new AxisAlignedBB(p * 14, p / 2, 1 - p * 2, p, 1 - p, 1 - p));

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
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		return true;// implements in DeskItem
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCabinet();
	}

	public boolean testIntersection(IPartInfo self, IPartInfo otherPart) {
		if (otherPart.getPart() instanceof IDeskDrawer) {
			if (self.getState().getValue(SonarProperties.ORIENTATION) == otherPart.getState().getValue(SonarProperties.ORIENTATION)) {
				return false;
			}
		}
		return super.testIntersection(self, otherPart);
	}

}
