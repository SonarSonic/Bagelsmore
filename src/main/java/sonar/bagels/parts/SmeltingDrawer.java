package sonar.bagels.parts;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import sonar.bagels.Bagels;

public class SmeltingDrawer extends DeskDrawer implements IGuiPart {

	public SmeltingDrawer() {
		super();
	}

	public SmeltingDrawer(DrawerPosition position, EnumFacing face) {
		super(position, face);
	}

	@Override
	public DrawerType getDrawerType() {
		return DrawerType.SMALL;
	}

	@Override
	public int getHashedID() {
		return -this.getDrawerPosition().ordinal();
	}

	@Override
	public Object getServerElement(EntityPlayer player) {
		return null;
	}

	@Override
	public Object getClientElement(EntityPlayer player) {
		return null;
	}

	@Override
	public int getInvSize() {
		return 3;
	}

	@Override
	public ItemStack createItemStack() {
		return new ItemStack(Bagels.smeltingDrawer, 1);
	}

}
