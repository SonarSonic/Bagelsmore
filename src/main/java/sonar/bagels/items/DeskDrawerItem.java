package sonar.bagels.items;

import mcmultipart.api.item.ItemBlockMultipart;
import mcmultipart.api.multipart.IMultipart;
import net.minecraft.block.SoundType;
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
import sonar.bagels.common.blocks.BlockDrawer;
import sonar.bagels.utils.BagelsHelper;
import sonar.core.registries.SonarRegistryBlock;

public class DeskDrawerItem extends ItemBlockMultipart {

	
	public static class SonarRegistryDrawer<T extends BlockDrawer & IMultipart> extends SonarRegistryBlock<T>{
		
		public SonarRegistryDrawer(T block, String name) {
			super(block, name);
		}
		
		public SonarRegistryDrawer(T block, String name, Class<? extends TileEntity> tile) {
			super(block, name, tile);
		}

		@Override
		public Item getItemBlock() {
			return new DeskDrawerItem(value);
		}
	}

	public DeskDrawerItem(BlockDrawer drawer) {
		super(drawer, drawer);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return place(player, world, pos, hand, facing, hitX, hitY, hitZ, this, this.block::getStateForPlacement, multipartBlock, this::placeBlockAtTested, ItemBlockMultipart::placePartAt);
	}

	public static EnumActionResult place(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, Item item, IBlockPlacementInfo stateProvider, IMultipart multipartBlock, IBlockPlacementLogic blockLogic, IPartPlacementLogic partLogic) {
		ItemStack stack = player.getHeldItem(hand);

		if (!stack.isEmpty()) {
			int meta = item.getMetadata(stack.getMetadata());
			float d = Math.abs(hitX * facing.getFrontOffsetX() + hitY * facing.getFrontOffsetY() + hitZ * facing.getFrontOffsetZ());
			//boolean shouldOffset = ;;// && (d == 0 || d == 1);
			
			if ( BagelsHelper.getDrawerContainer(world, pos)==null || /*shouldOffset ||*/ !placeAt(stack, player, hand, world, pos, facing, hitX, hitY, hitZ, stateProvider, meta, multipartBlock, blockLogic, partLogic)) {
				//pos = pos.offset(facing);
				//if (!placeAt(stack, player, hand, world, pos, facing, hitX, hitY, hitZ, stateProvider, meta, multipartBlock, blockLogic, partLogic)) {
					return EnumActionResult.FAIL;
				//}
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
