package sonar.bagels.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

import mcmultipart.api.container.IMultipartContainer;
import mcmultipart.api.container.IPartInfo;
import mcmultipart.api.multipart.IMultipart;
import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.multipart.MultipartHelper;
import mcmultipart.api.slot.EnumCenterSlot;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.bagels.api.DrawerPosition;
import sonar.bagels.api.IDeskPart;
import sonar.bagels.api.IDrawerContainer;
import sonar.bagels.common.tileentity.TileDesk;
import sonar.core.integration.multipart.SonarMultipartHelper;

public class BagelsHelper {

	public static List<BlockPos> getDeskPositions(World world, BlockPos pos, EnumFacing deskFacing) {
		EnumFacing hoz = EnumFacing.getHorizontal(deskFacing.getHorizontalIndex() + 1);
		List<BlockPos> positions = Lists.newArrayList(pos.offset(hoz, 1), pos.offset(hoz, 0), pos.offset(hoz, -1));
		return positions;
	}

	public static boolean canPlaceDeskAt(World world, BlockPos pos, EnumFacing deskFacing) {
		List<BlockPos> positions = getDeskPositions(world, pos, deskFacing);
		for (BlockPos adj : positions) {
			IBlockState state = world.getBlockState(adj);
			if (!state.getBlock().isReplaceable(world, adj)) {
				return false;
			}
		}
		return true;
	}

	public static IDrawerContainer getDrawerContainer(World world, BlockPos pos) {
		Optional<IMultipart> part = MultipartHelper.getPart(world, pos, EnumCenterSlot.CENTER);
		if (part.isPresent() && part.get() instanceof IDrawerContainer) {
			return (IDrawerContainer) part.get();
		}
		return null;
	}

	public static TileDesk getDeskAt(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileDesk) {
			return (TileDesk) tile;
		}
		Optional<IMultipartTile> deskTile = MultipartHelper.getPartTile(world, pos, EnumCenterSlot.CENTER);
		if (deskTile.isPresent() && deskTile.get() instanceof TileDesk) {
			return (TileDesk) deskTile.get();
		}
		return null;
	}

	public static void removeAllDeskParts(World world, BlockPos pos) {
		ArrayList<IPartSlot> toDelete = new ArrayList();
		world = SonarMultipartHelper.unwrapWorld(world);
		Optional<IMultipartContainer> container = MultipartHelper.getContainer(world, pos);
		if (container.isPresent()) {
			IMultipartContainer tile = container.get();
			for (IPartInfo info : tile.getParts().values()) {
				if (!(info.getPart() instanceof IDrawerContainer) && info.getPart() instanceof IDeskPart) {
					toDelete.add(info.getSlot());
				}
			}
			toDelete.forEach(slot -> {
				if (tile.get(slot).isPresent())
					tile.removePart(slot);
			});
		}

	}

	public static IPartSlot getNextDrawerPosition(World world, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ, EntityLivingBase placer) {
		Optional<IMultipartContainer> tile = MultipartHelper.getContainer(world, pos);
		if (!tile.isPresent()) {
			return DrawerPosition.NONE;
		}
		IMultipartContainer container = tile.get();
		if (BagelsHelper.getDrawerContainer(world, pos) != null) {
			for (DrawerPosition position : DrawerPosition.values()) {
				if (container.canAddPart(position, state)) {
					return position;
				}
			}
		}
		return DrawerPosition.NONE;
	}

	public static boolean isBook(ItemStack stack) {
		if (stack != null && stack.getItem() != null) {
			if (stack.getItem() instanceof ItemBook) {
				return true;
			} else if (stack.getItem() == Items.BOOK || stack.getItem() == Items.WRITABLE_BOOK || stack.getItem() == Items.WRITTEN_BOOK || stack.getItem() == Items.ENCHANTED_BOOK) {
				return true;
			}
		}
		return false;
	}

}
