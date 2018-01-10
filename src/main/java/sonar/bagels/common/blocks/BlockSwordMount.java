package sonar.bagels.common.blocks;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sonar.bagels.common.tileentity.TileSwordMount;
import sonar.core.common.block.SonarMaterials;
import sonar.core.common.block.properties.SonarProperties;
import sonar.core.helpers.InventoryHelper;
import sonar.core.helpers.NBTHelper.SyncType;
import sonar.core.integration.multipart.BlockFacingMultipart;

public class BlockSwordMount extends BlockFacingMultipart {

	public BlockSwordMount() {
		super(SonarMaterials.droppable_wood);
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileSwordMount();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileSwordMount) {
			ItemStack heldItem = player.getHeldItem(hand);
			TileSwordMount swordMount = (TileSwordMount) tile;
			if (!player.isSneaking()) {
				if (swordMount.getStackInSlot(0).isEmpty() && !heldItem.isEmpty() && heldItem.getItem() instanceof ItemSword && heldItem.getCount() == 1) {
					if (!world.isRemote) {
						swordMount.setInventorySlotContents(0, heldItem.copy());
						heldItem.shrink(1);
						// need to update?
					} else {
						world.playSound(player, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.NEUTRAL, 0.3F, 0.1F);
					}
				}
			} else if (!swordMount.getStackInSlot(0).isEmpty()) {
				if (!world.isRemote) {
					net.minecraft.inventory.InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), swordMount.getStackInSlot(0).copy());
					swordMount.setInventorySlotContents(0, ItemStack.EMPTY);
					// need to update?
				}
				world.playSound(player, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.NEUTRAL, 0.3F, 0.1F);
			}
			if (!world.isRemote) {
				swordMount.sendSyncPacket(player, SyncType.SAVE);
			}
		}
		return false;
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing face = state.getValue(SonarProperties.ORIENTATION);
		double p = 0.0625;

		switch (face) {
		case EAST:
			return new AxisAlignedBB(p * 2 + p / 2, 1 - p / 2, p * 3, p * 1 + p / 2, 1 + p / 2, p * 13);
		case NORTH:
			return new AxisAlignedBB(p * 3, 1 - p / 2, 1 - p * 2 + p / 2, p * 13, 1 + p / 2, 1 - p * 3 + p / 2);
		case SOUTH:
			return new AxisAlignedBB(p * 3, 1 - p / 2, p * 2 + p / 2, p * 13, 1 + p / 2, p * 1 + p / 2);
		case WEST:
			return new AxisAlignedBB(1 - p * 2 + p / 2, 1 - p / 2, p * 3, 1 - p * 3 + p / 2, 1 + p / 2, p * 13);
		default:
			return super.getBoundingBox(state, source, pos);
		}

	}

	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		super.breakBlock(world, pos, state);
		InventoryHelper.dropInventory(world.getTileEntity(pos), world, pos, state);
	}
}
