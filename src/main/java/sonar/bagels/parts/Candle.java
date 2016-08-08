package sonar.bagels.parts;

import java.util.List;

import mcmultipart.raytrace.PartMOP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

public class Candle extends BagelsMultipart {

	public Candle() {
		super();
	}

	public Candle(EnumFacing face) {
		super(face);
	}

	@Override
	public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
		double p = 0.0625;
		switch (face) {
		case EAST:
			boxes.add(new AxisAlignedBB(p * 6.5, 1 - p / 2, p * 11, p * 2.5, 1 + p * 3, p * 16));
			break;
		case NORTH:
			boxes.add(new AxisAlignedBB(p * 11, 1 - p / 2, 1 - p * 6.5, p * 16, 1 + p * 3, 1 - p * 2.5));
			break;
		case SOUTH:
			boxes.add(new AxisAlignedBB(1 - p * 11, 1 - p / 2, p * 6.5, 1 - p * 16, 1 + p * 3, p * 2.5));
			break;
		case WEST:
			boxes.add(new AxisAlignedBB(1 - p * 6.5, 1 - p / 2, 1 - p * 11, 1 - p * 2.5, 1 + p * 3, 1 - p * 16));
			break;
		default:
			break;

		}
	}

	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {

		return false;
	}

	public boolean shouldDropItem() {
		return false;
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
	public ItemStack createItemStack() {
		DeskMultipart part = DeskMultipart.getDeskPart(getContainer());
		if (part != null) {
			return part.createItemStack();
		}
		return null;// type.getDesk();
	}
}
