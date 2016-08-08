package sonar.bagels.parts;

import java.util.ArrayList;
import java.util.List;

import mcmultipart.raytrace.PartMOP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import sonar.bagels.Bagels;
import sonar.bagels.client.gui.GuiDeskCrafting;
import sonar.bagels.common.containers.ContainerCraftingPart;
import sonar.bagels.utils.IGuiPart;

public class DeskCraftingPart extends InventoryMultipart implements IGuiPart {

	public DeskCraftingPart() {
		super();
	}

	public DeskCraftingPart(EnumFacing face) {
		super(face);
	}

	@Override
	public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
		boxes.add(new AxisAlignedBB(0, 1 - (0.0625), 0, 1, 1 - (0.0625 / 2), 1));
	}

	@Override
	public boolean canRenderInLayer(BlockRenderLayer layer) {
		return false;
	}

	@Override
	public int getInvSize() {
		return 10;
	}

	@Override
	public void harvest(EntityPlayer player, PartMOP hit) {
		DeskMultipart part = DeskMultipart.getDeskPart(getContainer());
		if (part != null) {
			part.breakDesk(player, hit);
		} else {
			super.harvest(player, hit);
		}
	}

	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
		if (!this.getWorld().isRemote)
			player.openGui(Bagels.instance, this.getHashedID(), this.getWorld(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
		return true;
	}

	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList();
		for (int i = 0; i < 9; i++) {
			if (inv.getStackInSlot(i) != null) {
				list.add(inv.getStackInSlot(i));
			}
		}
		return list;
	}

	public boolean shouldDropItem() {
		return false;
	}

	@Override
	public ItemStack createItemStack() {
		DeskMultipart part = DeskMultipart.getDeskPart(getContainer());
		if (part != null) {
			return part.createItemStack();
		}
		return null;// type.getDesk();
	}

	@Override
	public int getHashedID() {
		return -99;
	}

	@Override
	public Object getServerElement(EntityPlayer player) {
		return new ContainerCraftingPart(player, this);
	}

	@Override
	public Object getClientElement(EntityPlayer player) {
		return new GuiDeskCrafting(this);
	}
}
