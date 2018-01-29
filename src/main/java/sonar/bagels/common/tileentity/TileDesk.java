package sonar.bagels.common.tileentity;

import java.util.Optional;

import mcmultipart.api.item.ItemBlockMultipart;
import mcmultipart.api.multipart.IMultipart;
import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.slot.EnumFaceSlot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.bagels.Bagels;
import sonar.bagels.api.DeskPosition;
import sonar.bagels.client.gui.GuiDeskCrafting;
import sonar.bagels.common.containers.ContainerDeskCrafting;
import sonar.core.api.IFlexibleGui;
import sonar.core.api.utils.BlockCoords;
import sonar.core.helpers.NBTHelper.SyncType;
import sonar.core.integration.multipart.TileSonarMultipart;
import sonar.core.inventory.ISonarInventory;
import sonar.core.inventory.ISonarInventoryTile;
import sonar.core.inventory.SonarInventory;
import sonar.core.network.FlexibleGuiHandler;
import sonar.core.network.sync.SyncCoords;

public class TileDesk extends TileSonarMultipart {

	public boolean shouldHarvestAdjacent = true;

	public SyncCoords middle = new SyncCoords(1);

	{
		syncList.addParts(middle);
	}

	public TileDesk() {}

	public TileDesk setMiddle(BlockPos pos) {
		middle.setCoords(new BlockCoords(pos));
		return this;
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return false;
	}

	public static class LEFT extends TileDesk {

		public LEFT() {}

	}

	public static class MIDDLE extends TileDesk {

		public MIDDLE() {}

		public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
			ItemStack heldItem = player.getHeldItem(hand);
			if (heldItem.isEmpty()) {
				return false;
			}
			if (facing == EnumFacing.UP) {
				if (heldItem.getItem() == Items.PAPER) {
					if (!world.isRemote) {
						addPaper(heldItem, world, pos, state, player, hand, facing, hitX, hitY, hitZ);
					}
					return true;
				} else if (heldItem.getItem() == Bagels.clipboard) {
					if (!world.isRemote) {
						addPaper(heldItem, world, pos, state, player, hand, facing, hitX, hitY, hitZ);
						player.setHeldItem(hand, new ItemStack(Bagels.clipboardEmpty, 1));
					}
					return true;
				}
			}
			return false;
		}

		public void addPaper(ItemStack stack, World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
			EnumFacing face = player.getHorizontalFacing().getOpposite();
			ItemStack paperStack = stack.copy();
			EnumActionResult result = ItemBlockMultipart.place(player, world, pos, hand, facing, hitX, hitY, hitZ, stack.getItem(), Bagels.blockPaper::getStateForPlacement, (IMultipart) Bagels.blockPaper, this::canPlacePaper, ItemBlockMultipart::placePartAt);
			if (result == EnumActionResult.SUCCESS && paperStack.getTagCompound() != null && paperStack.getTagCompound().hasKey("todo") && this.info != null && this.info.getContainer() != null) {
				Optional<IMultipartTile> paperInfo = info.getContainer().getPartTile(EnumFaceSlot.UP);
				if (paperInfo.isPresent() && paperInfo.get() instanceof TilePaper) {
					TilePaper paper = (TilePaper) paperInfo.get();
					paper.list.readFromNBT(paperStack.getTagCompound().getCompoundTag("todo"));
				}
			}
		}

		public boolean canPlacePaper(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, IBlockState newState) {
			return false;
		}
	}

	public static class RIGHT extends TileDesk implements ISonarInventoryTile, IFlexibleGui {

		protected ISonarInventory inv;

		public RIGHT() {
			inv = new SonarInventory(this, 10);
			syncList.addParts(inv);
		}

		@Override
		public ISonarInventory inv() {
			return inv;
		}

		@Override
		public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
			if (facing == EnumFacing.UP) {
				if (!world.isRemote) {
					FlexibleGuiHandler.openMultipartGui(0, player, world, pos);
				}
				return true;
			}
			return false;
		}

		@Override
		public SyncType getUpdateTagType() {
			return SyncType.SAVE;
		}

		@Override
		public Object getServerElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
			return new ContainerDeskCrafting(player, this);
		}

		@Override
		public Object getClientElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
			return new GuiDeskCrafting(player, this);
		}

	}

	public static TileDesk createDesk(DeskPosition position) {
		switch (position) {
		case LEFT:
			return new TileDesk.LEFT();
		case RIGHT:
			return new TileDesk.RIGHT();
		default:
			return new TileDesk.MIDDLE();
		}
	}

}
