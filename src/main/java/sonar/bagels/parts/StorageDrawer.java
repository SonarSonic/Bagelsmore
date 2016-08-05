package sonar.bagels.parts;

import mcmultipart.raytrace.PartMOP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import sonar.bagels.Bagels;
import sonar.bagels.client.gui.GuiStorageDrawer;
import sonar.bagels.common.containers.ContainerStorageDrawer;

public abstract class StorageDrawer extends DeskDrawer implements IGuiPart {

	public StorageDrawer() {
		super();
	}

	public StorageDrawer(DrawerPosition position, EnumFacing face) {
		super(position, face);
	}

	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
		if (!player.isSneaking() && hit.sideHit == EnumFacing.UP) {
			if (!this.getWorld().isRemote)
				player.openGui(Bagels.instance, this.getHashedID(), this.getWorld(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
			return true;
		} else {
			return super.onActivated(player, hand, heldItem, hit);
		}
	}

	@Override
	public Object getServerElement(EntityPlayer player) {
		return new ContainerStorageDrawer(this, player);
	}

	@Override
	public Object getClientElement(EntityPlayer player) {
		return new GuiStorageDrawer(this);
	}

	@Override
	public int getHashedID() {
		return this.getDrawerPosition().ordinal();
	}

}
