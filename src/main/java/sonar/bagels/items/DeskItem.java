package sonar.bagels.items;

import java.util.ArrayList;

import com.google.common.collect.Lists;

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
import sonar.bagels.parts.DeskMultipart;
import sonar.bagels.parts.DeskMultipart.DeskPosition;
import sonar.bagels.parts.Paper;

public class DeskItem extends ItemMultiPart {
	
	public boolean place(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
		if (!player.canPlayerEdit(pos, side, stack))
			return false;
		
		EnumFacing facing = player.getHorizontalFacing().getOpposite();
		EnumFacing hoz = EnumFacing.getHorizontal(facing.getHorizontalIndex() + 1);
		ArrayList<DeskMultipart> parts = Lists.newArrayList(new DeskMultipart(DeskPosition.LEFT, facing), new DeskMultipart(DeskPosition.MIDDLE, facing), new DeskMultipart(DeskPosition.RIGHT, facing));

		for (int i = 0; i < parts.size(); i++) {
			if (!MultipartHelper.canAddPart(world, pos.offset(hoz, i - 1), parts.get(i))) {
				return false;
			}
		}
		if (!world.isRemote){
			parts.forEach(part -> MultipartHelper.addPart(world, pos.offset(hoz, part.position.ordinal() - 1), part));
			MultipartHelper.addPart(world, pos, new Paper(facing));
		}
		consumeItem(stack);

		SoundType sound = getPlacementSound(stack);
		if (sound != null)
			world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, sound.getVolume(), sound.getPitch());

		return false;
	}

	@Override
	public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
		return null;
	}
}
