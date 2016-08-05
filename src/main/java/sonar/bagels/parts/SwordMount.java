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

public class SwordMount extends InventoryMultipart {

	public SwordMount() {
		super();
	}

	public SwordMount(EnumFacing face) {
		super(face);
	}

	public ItemStack getSword() {
		return inv.getStackInSlot(0);
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
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
		if (!player.isSneaking()) {
			if (inv.getStackInSlot(0) == null && heldItem != null && heldItem.getItem() instanceof ItemSword && heldItem.stackSize == 1) {
				if (!getWorld().isRemote) {
					inv.setStackInSlot(0, heldItem.copy());
					heldItem.stackSize--;
					this.sendUpdatePacket();
				} else {
					this.getWorld().playSound(player, this.getPos(), SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.NEUTRAL, 0.3F, 0.1F);
				}
			}
		} else if (inv.getStackInSlot(0) != null) {
			if (!getWorld().isRemote) {
				InventoryHelper.spawnItemStack(this.getWorld(), this.getPos().getX(), this.getPos().getY() + 1, this.getPos().getZ(), inv.getStackInSlot(0).copy());
				inv.setStackInSlot(0, null);
				this.sendUpdatePacket();
			}
			this.getWorld().playSound(player, this.getPos(), SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.NEUTRAL, 0.3F, 0.1F);
		}
		return false;
	}

	@Override
	public int getInvSize() {
		return 1;
	}

	@Override
	public ItemStack createItemStack() {
		return null;
	}
}
