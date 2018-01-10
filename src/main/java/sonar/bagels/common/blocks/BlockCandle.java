package sonar.bagels.common.blocks;

import java.util.Random;

import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sonar.bagels.api.EnumCandleSlot;
import sonar.bagels.common.tileentity.TileCandle;
import sonar.core.SonarCore;
import sonar.core.common.block.SonarMaterials;
import sonar.core.common.block.properties.SonarProperties;
import sonar.core.integration.multipart.BlockFacingMultipart;

public class BlockCandle extends BlockFacingMultipart {

	public BlockCandle() {
		super(SonarMaterials.droppable_wood);
		this.hasTileEntity = false;
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing face = state.getValue(SonarProperties.ORIENTATION);
		double p = 0.0625;
		switch (face) {
		case EAST:
			return new AxisAlignedBB(p * 6.5, 1 - p / 2, p * 11, p * 2.5, 1 + p * 3, p * 16);
		case NORTH:
			return new AxisAlignedBB(p * 11, 1 - p / 2, 1 - p * 6.5, p * 16, 1 + p * 3, 1 - p * 2.5);
		case SOUTH:
			return new AxisAlignedBB(1 - p * 11, 1 - p / 2, p * 6.5, 1 - p * 16, 1 + p * 3, p * 2.5);
		case WEST:
			return new AxisAlignedBB(1 - p * 6.5, 1 - p / 2, 1 - p * 11, 1 - p * 2.5, 1 + p * 3, 1 - p * 16);
		default:
			return getBoundingBox(state, source, pos);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileCandle) {
			TileCandle candle = (TileCandle) tile;
			candle.isActive.invert();
		}
		if (world.isRemote)
			world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, SonarCore.rand.nextFloat() * 0.4F + 0.8F);

		return true;
	}

	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getLightValue();
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		super.randomDisplayTick(state, world, pos, rand);
		boolean isBurning = false;
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileCandle) {
			TileCandle candle = (TileCandle) tile;
			isBurning = candle.isBurning();
		}
		if (isBurning) {
			EnumFacing face = state.getValue(SonarProperties.ORIENTATION);
			double d0 = pos.getX() + 0.5D;
			double d1 = pos.getY() + 1.08D;
			double d2 = pos.getZ() + 0.5D;
			double d3 = 0.22D;
			double d4 = 0.22D;

			switch (face) {
			case EAST:
				d2 += 0.34;
				break;
			case NORTH:
				d0 += 0.34;
				break;
			case SOUTH:
				d0 -= 0.34;
				break;
			case WEST:
				d2 -= 0.34;
				break;
			default:
				break;
			}

			EnumFacing opposite = face.getOpposite();
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4 * (double) opposite.getFrontOffsetX(), d1 + d3, d2 + d4 * (double) opposite.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
			world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4 * (double) opposite.getFrontOffsetX(), d1 + d3, d2 + d4 * (double) opposite.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
		}

	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getStateFromMeta(placer.getHorizontalFacing().getOpposite().ordinal());
	}

	@Override
	public IPartSlot getSlotForPlacement(World world, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ, EntityLivingBase placer) {
		return EnumCandleSlot.fromFace(placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public IPartSlot getSlotFromWorld(IBlockAccess world, BlockPos pos, IBlockState state) {
		return EnumCandleSlot.fromFace(getFaceFromState(state));
	}

	public EnumFacing getFaceFromState(IBlockState state) {
		return state.getValue(SonarProperties.ORIENTATION);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(SonarProperties.ORIENTATION, EnumFacing.VALUES[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return getFaceFromState(state).ordinal();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SonarProperties.ORIENTATION);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCandle();
	}

}
