package sonar.bagels.items;

import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.MultipartHelper;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import sonar.bagels.parts.DeskDrawer;
import sonar.bagels.parts.DrawerLarge;
import sonar.bagels.parts.DrawerSmall;
import sonar.bagels.parts.EnderDrawer;
import sonar.bagels.parts.FluidDrawer;
import sonar.bagels.parts.RecyclingDrawer;
import sonar.bagels.parts.SmeltingDrawer;
import sonar.bagels.utils.DrawerPosition;
import sonar.bagels.utils.IDeskDrawer;

public abstract class DeskDrawerItem extends ItemMultiPart {

	public boolean place(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
		if (!player.canPlayerEdit(pos, side, stack))
			return false;

		IDeskDrawer part = (IDeskDrawer) createPart(world, pos, side, hit, stack, player);
		DrawerPosition drawerPos = DrawerPosition.NONE;
		if (part != null && (drawerPos = DeskDrawer.getValidDrawerPosition(world, pos, part, part.getPartFacing())).isValidPosition()) {
			if (!world.isRemote) {
				part.setDrawerPosition(drawerPos);
				MultipartHelper.addPart(world, pos, part);
			}
			consumeItem(stack);

			SoundType sound = getPlacementSound(stack);
			if (sound != null)
				world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, sound.getVolume(), sound.getPitch());

			return true;
		}

		return false;
	}

	public static class DrawerLargeItem extends DeskDrawerItem {

		@Override
		public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
			return new DrawerLarge(DrawerPosition.NONE, player.getHorizontalFacing().getOpposite());
		}

	}

	public static class DrawerSmallItem extends DeskDrawerItem {

		@Override
		public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
			return new DrawerSmall(DrawerPosition.NONE, player.getHorizontalFacing().getOpposite());
		}

	}

	public static class SmeltingDrawerItem extends DeskDrawerItem {

		@Override
		public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
			return new SmeltingDrawer(DrawerPosition.NONE, player.getHorizontalFacing().getOpposite());
		}

	}

	public static class RecyclingDrawerItem extends DeskDrawerItem {

		@Override
		public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
			return new RecyclingDrawer(DrawerPosition.NONE, player.getHorizontalFacing().getOpposite());
		}

	}

	public static class FluidDrawerItem extends DeskDrawerItem {

		@Override
		public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
			return new FluidDrawer(DrawerPosition.NONE, player.getHorizontalFacing().getOpposite());
		}

	}

	public static class EnderDrawerItem extends DeskDrawerItem {

		@Override
		public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
			return new EnderDrawer(DrawerPosition.NONE, player.getHorizontalFacing().getOpposite());
		}

	}
}
