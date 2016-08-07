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

public class Bookshelf extends InventoryMultipart {

	public Bookshelf() {
		super();
	}

	public Bookshelf(EnumFacing face) {
		super(face);
	}

	@Override
	public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
		double p = 0.0625;
		switch (face) {
		case EAST:
			boxes.add(new AxisAlignedBB(p * 2 + p / 2, 1 - p / 2, p * 3, p * 1 + p / 2, 1 + p / 2, p * 13));
			break;
		case NORTH:
			boxes.add(new AxisAlignedBB(p * 3, 1 - p / 2, 1 - p * 2 + p / 2, p * 13, 1 + p / 2, 1 - p * 3 + p / 2));
			break;
		case SOUTH:
			boxes.add(new AxisAlignedBB(p * 3, 1 - p / 2, p * 2 + p / 2, p * 13, 1 + p / 2, p * 1 + p / 2));
			break;
		case WEST:
			boxes.add(new AxisAlignedBB(1 - p * 2 + p / 2, 1 - p / 2, p * 3, 1 - p * 3 + p / 2, 1 + p / 2, p * 13));
			break;
		default:
			break;

		}
	}

	@Override
	public int getInvSize() {
		return 6;
	}

	@Override
	public ItemStack createItemStack() {
		return null;
	}
}
