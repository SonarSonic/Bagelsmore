package sonar.bagels.parts;

import mcmultipart.multipart.IMultipart;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import sonar.bagels.Bagels;
import sonar.bagels.client.gui.GuiStorageDrawer;
import sonar.bagels.common.containers.ContainerStorageDrawer;
import sonar.bagels.utils.DrawerPosition;
import sonar.bagels.utils.DrawerType;
import sonar.bagels.utils.IGuiPart;

public class FluidDrawer extends DeskDrawer {

	public int size = Fluid.BUCKET_VOLUME * 4;
	public FluidTank[] tanks = new FluidTank[] { new FluidTank(size), new FluidTank(size) };

	public FluidDrawer() {
		super();
	}

	public FluidDrawer(DrawerPosition position, EnumFacing face) {
		super(position, face);
	}

	public void onSlaveChanged() {
		shouldRenderSpecials=true;
	}
	
	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
		if (hit.sideHit == EnumFacing.UP) {
			if (!this.getWorld().isRemote) {
				int tankID = -1;
				if (this.face.getAxis() == Axis.X) {
					double pos = hit.hitVec.zCoord - this.getPos().getZ();
					tankID = face == EnumFacing.WEST ? pos < 0.5 ? 0 : 1 : pos < 0.5 ? 1 : 0;
				} else {
					double pos = hit.hitVec.xCoord - this.getPos().getX();
					tankID = face == EnumFacing.SOUTH ? pos < 0.5 ? 0 : 1 : pos < 0.5 ? 1 : 0;
				}
				if (tankID != -1) {
					FluidTank targetTank = tanks[tankID];
					FluidUtil.interactWithFluidHandler(heldItem, targetTank, player);
					this.sendUpdatePacket();
					player.addChatComponentMessage(new TextComponentTranslation((targetTank.getFluid() != null ? targetTank.getFluid().getLocalizedName() : "Empty") + ": " + targetTank.getFluidAmount() + " mB"));
				}
			}
			return true;
		} else {
			return super.onActivated(player, hand, heldItem, hit);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		for (int i = 0; i < tanks.length; i++)
			tanks[i].readFromNBT(tag.getCompoundTag("tank" + i));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		for (int i = 0; i < tanks.length; i++) {
			NBTTagCompound tankTag = tanks[i].writeToNBT(new NBTTagCompound());
			tag.setTag("tank" + i, tankTag);
		}
		return tag;
	}

	@Override
	public void writeUpdatePacket(PacketBuffer buf) {
		super.writeUpdatePacket(buf);
		for (int i = 0; i < tanks.length; i++) {
			NBTTagCompound tankTag = tanks[i].writeToNBT(new NBTTagCompound());
			ByteBufUtils.writeTag(buf, tankTag);
		}
	}

	@Override
	public void readUpdatePacket(PacketBuffer buf) {
		super.readUpdatePacket(buf);
		for (int i = 0; i < tanks.length; i++){			
			tanks[i].readFromNBT(ByteBufUtils.readTag(buf));
		}
	}

	@Override
	public DrawerType getDrawerType() {
		return DrawerType.LARGE;
	}

	@Override
	public int getInvSize() {
		return 0;
	}

	@Override
	public ItemStack createItemStack() {
		return new ItemStack(Bagels.fluidDrawer, 1);
	}

	public boolean disableInv() {
		return true;
	}

}
