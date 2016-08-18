package sonar.bagels.parts;

import mcmultipart.raytrace.PartMOP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import sonar.bagels.Bagels;
import sonar.bagels.utils.DrawerPosition;
import sonar.bagels.utils.DrawerType;
import sonar.bagels.utils.IGuiPart;

public class EnderDrawer extends DeskDrawer implements IGuiPart {

	public EnderDrawer() {
		super();
	}

	public EnderDrawer(DrawerPosition position, EnumFacing face) {
		super(position, face);
	}

	@Override
	public DrawerType getDrawerType() {
		return DrawerType.SMALL;
	}

	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
		if (!player.isSneaking() && hit.sideHit == EnumFacing.UP) {
			if (!this.getWorld().isRemote) {
				player.displayGUIChest(player.getInventoryEnderChest());
			}
			// player.openGui(Bagels.instance, this.getHashedID(), this.getWorld(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
			return true;
		} else {
			return super.onActivated(player, hand, heldItem, hit);
		}
	}

	@Override
	public int getHashedID() {
		return -this.getDrawerPosition().ordinal() - 100;
	}

	@Override
	public Object getServerElement(EntityPlayer player) {
		return null; // new ContainerRecyclingDrawer(player.inventory, this);
	}

	@Override
	public Object getClientElement(EntityPlayer player) {
		return null; // new GuiRecyclingDrawer(this);
	}

	@Override
	public int getInvSize() {
		return 1;
	}

	@Override
	public ItemStack createItemStack() {
		return new ItemStack(Bagels.enderDrawer, 1);
	}

}
