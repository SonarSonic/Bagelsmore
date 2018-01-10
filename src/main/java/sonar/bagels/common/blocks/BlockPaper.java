package sonar.bagels.common.blocks;

import java.util.Optional;
import java.util.Random;

import mcmultipart.api.container.IMultipartContainer;
import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.multipart.MultipartHelper;
import mcmultipart.api.slot.EnumFaceSlot;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sonar.bagels.Bagels;
import sonar.bagels.api.IDeskPart;
import sonar.bagels.common.tileentity.TilePaper;
import sonar.core.common.block.SonarMaterials;
import sonar.core.common.block.properties.SonarProperties;
import sonar.core.integration.multipart.BlockFacingMultipart;
import sonar.core.network.FlexibleGuiHandler;

public class BlockPaper extends BlockFacingMultipart implements IDeskPart {

	public BlockPaper() {
		super(SonarMaterials.droppable_wood);
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.PAPER;
	}

	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ItemStack paperDrop = new ItemStack(Items.PAPER, 1, this.damageDropped(state));
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof IMultipartContainer) {
			Optional<IMultipartTile> paperTile = MultipartHelper.getPartTile(world, pos, EnumFaceSlot.UP);
			if (paperTile.isPresent() && paperTile.get() instanceof TilePaper) {
				tile = (TileEntity) paperTile.get();
			}
		}
		if (tile != null && tile instanceof TilePaper) {
			TilePaper paper = (TilePaper) tile;
			paper.list.writeListToStack(paperDrop);
			paperDrop.setStackDisplayName("Todo List");
		}
		drops.add(paperDrop);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			ItemStack heldItem = player.getHeldItem(hand);
			if (heldItem != null && heldItem.getItem() == Bagels.clipboardEmpty) {
				TileEntity tile = world.getTileEntity(pos);
				if (tile instanceof TilePaper) {
					TilePaper paper = (TilePaper) tile;
					ItemStack stack = new ItemStack(Bagels.clipboard, 1);
					paper.list.writeListToStack(stack);
					player.setHeldItem(hand, stack);
					removedByPlayer(state, world, pos, player, true);
				}
			} else {
				FlexibleGuiHandler.openMultipartGui(0, player, world, pos);
			}
		}
		return true;
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing face = state.getValue(SonarProperties.ORIENTATION);
		double p = 0.0625;
		double minY = 1 - p / 2;
		double maxY = 1 + p * 5 + p / 2;

		switch (face) {
		case EAST:
			return new AxisAlignedBB(0.0625 * 5, 1 - (0.0625 / 2), 0.2, 1 - 0.2, 1, 1 - 0.0625 * 5);
		case NORTH:
			return new AxisAlignedBB(0.2, 1 - (0.0625 / 2), 0.2, 1 - 0.0625 * 5, 1, 1 - 0.0625 * 5);
		case SOUTH:
			return new AxisAlignedBB(0.0625 * 5, 1 - (0.0625 / 2), 0.0625 * 5, 1 - 0.2, 1, 1 - 0.2);
		case WEST:
			return new AxisAlignedBB(1 - 0.0625 * 5, 1 - (0.0625 / 2), 1 - 0.2, 0.2, 1, 0.0625 * 5);
		default:
			return super.getBoundingBox(state, source, pos);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TilePaper();
	}

	@Override
	public IPartSlot getSlotForPlacement(World world, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ, EntityLivingBase placer) {
		return EnumFaceSlot.UP;
	}

}
