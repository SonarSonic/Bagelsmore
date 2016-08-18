package sonar.bagels.parts;

import java.util.List;
import java.util.Random;

import mcmultipart.raytrace.PartMOP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Candle extends BagelsMultipart {

	public boolean isActive = true;

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

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(Random rand) {
		if (isActive) {
			double d0 = (double) getPos().getX() + 0.5D;
			double d1 = (double) getPos().getY() + 1.08D;
			double d2 = (double) getPos().getZ() + 0.5D;
			double d3 = 0.22D;
			double d4 = 0.22D;

			switch (face) {
			case EAST:
				d2 += 0.34;
				break;
			case NORTH:
				d0 += 0.34;
				break;
			case SOUTH:
				d0 -= 0.34;
				break;
			case WEST:
				d2 -= 0.34;
				break;
			default:
				break;
			}

			EnumFacing opposite = face.getOpposite();
			getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4 * (double) opposite.getFrontOffsetX(), d1 + d3, d2 + d4 * (double) opposite.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
			getWorld().spawnParticle(EnumParticleTypes.FLAME, d0 + d4 * (double) opposite.getFrontOffsetX(), d1 + d3, d2 + d4 * (double) opposite.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
		}

	}

	public int getLightValue() {
		return isActive ? 10 : 0;
	}

	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
		this.isActive = !isActive;
		this.markLightingUpdate();
		return true;
	}

	public boolean shouldDropItem() {
		return false;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setBoolean("isActive", isActive);
		return super.writeToNBT(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		isActive = tag.getBoolean("isActive");
	}

	@Override
	public void writeUpdatePacket(PacketBuffer buf) {
		super.writeUpdatePacket(buf);
		buf.writeBoolean(isActive);
	}

	@Override
	public void readUpdatePacket(PacketBuffer buf) {
		super.readUpdatePacket(buf);
		isActive = buf.readBoolean();
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
