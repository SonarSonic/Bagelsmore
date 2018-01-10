package sonar.bagels.items;

import java.util.List;

import mcmultipart.api.item.ItemBlockMultipart;
import mcmultipart.api.multipart.IMultipart;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.bagels.api.DeskMetadata;
import sonar.bagels.api.DeskPosition;
import sonar.bagels.common.blocks.BlockDesk;
import sonar.bagels.common.tileentity.TileDesk;
import sonar.bagels.utils.BagelsHelper;
import sonar.core.registries.SonarRegistryBlock;

public class DeskItem extends ItemBlockMultipart {

	
	public static class SonarRegistryDesk<T extends BlockDesk & IMultipart> extends SonarRegistryBlock<T>{
		
		public SonarRegistryDesk(T block, String name) {
			super(block, name);
		}
		
		public SonarRegistryDesk(T block, String name, Class<? extends TileEntity> tile) {
			super(block, name, tile);
		}

		@Override
		public Item getItemBlock() {
			return new DeskItem(value);
		}
	}
	
	public DeskItem(BlockDesk multipartBlock) {
		super(multipartBlock);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();
		EnumFacing orientation = player.getHorizontalFacing().getOpposite();
		if (!BagelsHelper.canPlaceDeskAt(world, pos, orientation)) {
			if (BagelsHelper.canPlaceDeskAt(world, pos.offset(facing), orientation)) {
				pos = pos.offset(facing);
			} else {
				return EnumActionResult.FAIL;
			}
		}
		EnumActionResult result = DeskItem.place(player, world, pos, hand, facing, hitX, hitY, hitZ, this, this.block::getStateForPlacement, multipartBlock, this::placeBlockAtTested, ItemBlockMultipart::placePartAt);
		if (result == EnumActionResult.SUCCESS) {
			EnumFacing hoz = EnumFacing.getHorizontal(orientation.getHorizontalIndex() + 1);
			//ItemBlockMultipart.placePartAt(new ItemStack(Bagels.swordMount), player, hand, world, pos, facing, hitX, hitY, hitZ, (IMultipart) Bagels.swordMount, Bagels.swordMount.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, 0, player));
			//ItemBlockMultipart.placePartAt(new ItemStack(Bagels.candle), player, hand, world, pos, facing, hitX, hitY, hitZ, (IMultipart) Bagels.candle, Bagels.candle.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, 0, player));
			//ItemBlockMultipart.placePartAt(new ItemStack(Bagels.desk_crafting), player, hand, world, pos.offset(hoz.getOpposite()), facing, hitX, hitY, hitZ, (IMultipart) Bagels.desk_crafting, Bagels.desk_crafting.getStateForPlacement(world, pos.offset(hoz.getOpposite()), facing, hitX, hitY, hitZ, 0, player));
		}
		return result;
	}

	public static EnumActionResult place(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, Item item, IBlockPlacementInfo stateProvider, IMultipart multipartBlock, IBlockPlacementLogic blockLogic, IPartPlacementLogic partLogic) {
		ItemStack stack = player.getHeldItem(hand);
		EnumFacing orientation = player.getHorizontalFacing().getOpposite();
		if (!stack.isEmpty()) {
			List<BlockPos> positions = BagelsHelper.getDeskPositions(world, pos, orientation);
			int meta = item.getMetadata(stack.getMetadata());
			float d = Math.abs(hitX * facing.getFrontOffsetX() + hitY * facing.getFrontOffsetY() + hitZ * facing.getFrontOffsetZ());

			for (int i = 0; i < 3; i++) {
				BlockPos actualPos = positions.get(i);
				int metaMap = DeskMetadata.getMeta(player.getHorizontalFacing().getOpposite(), DeskPosition.values()[i]);
				if (!placeAt(stack, player, hand, world, actualPos, facing, hitX, hitY, hitZ, stateProvider, metaMap, multipartBlock, blockLogic, partLogic)) {
					return EnumActionResult.FAIL;
				}
				TileEntity deskTile = world.getTileEntity(actualPos);
				if (deskTile != null && deskTile instanceof TileDesk) {
					TileDesk desk = (TileDesk) deskTile;
					desk.setMiddle(positions.get(1));
				}
			}
			SoundType soundtype = world.getBlockState(pos).getBlock().getSoundType(world.getBlockState(pos), world, pos, player);
			world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
			if (!player.capabilities.isCreativeMode) {
				stack.shrink(1);
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}
}
