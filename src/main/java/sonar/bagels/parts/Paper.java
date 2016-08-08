package sonar.bagels.parts;

import java.util.List;

import io.netty.buffer.ByteBuf;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import sonar.bagels.Bagels;
import sonar.bagels.client.gui.GuiTodoList;
import sonar.bagels.common.containers.ContainerTodoList;
import sonar.bagels.utils.IGuiPart;
import sonar.bagels.utils.TodoList;

public class Paper extends BagelsMultipart implements IGuiPart {

	public boolean wasSet = false;
	public TodoList list = new TodoList();

	public Paper() {
		super();
	}

	public Paper(EnumFacing face) {
		super(face);
	}

	@Override
	public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {

		switch (face) {
		case EAST:
			boxes.add(new AxisAlignedBB(0.3, 1 - (0.0625 / 2), 0.2, 1 - 0.2, 1, 1 - 0.3));
			break;
		case NORTH:
			boxes.add(new AxisAlignedBB(0.2, 1 - (0.0625 / 2), 0.2, 1 - 0.3, 1, 1 - 0.3));
			break;
		case SOUTH:
			boxes.add(new AxisAlignedBB(0.3, 1 - (0.0625 / 2), 0.3, 1 - 0.2, 1, 1 - 0.2));
			break;
		case WEST:
			boxes.add(new AxisAlignedBB(1 - 0.3, 1 - (0.0625 / 2), 1 - 0.2, 0.2, 1, 0.3));
			break;
		default:
			break;

		}
	}

	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
		if (!this.getWorld().isRemote) {
			if (heldItem != null && heldItem.getItem() == Bagels.clipboardEmpty) {
				ItemStack stack = new ItemStack(Bagels.clipboard, 1);
				list.writeListToStack(stack);
				player.setHeldItem(hand, stack);
			} else {
				player.openGui(Bagels.instance, this.getHashedID(), this.getWorld(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
			}
		}
		return true;
	}

	public boolean shouldDropItem() {
		return true;
	}

	@Override
	public ItemStack createItemStack() {
		ItemStack stack = new ItemStack(Items.PAPER, 1);
		list.writeListToStack(stack);
		return stack;
	}

	@Override
	public int getHashedID() {
		return -1;
	}

	@Override
	public Object getServerElement(EntityPlayer player) {
		return new ContainerTodoList(player, list);
	}

	@Override
	public Object getClientElement(EntityPlayer player) {
		return new GuiTodoList.Block(this, player, list);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		list.writeToNBT(tag);
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		list.readFromNBT(tag);
	}

	@Override
	public void writeUpdatePacket(PacketBuffer buf) {
		super.writeUpdatePacket(buf);
		list.writeUpdatePacket(buf, FMLCommonHandler.instance().getEffectiveSide().isClient());
	}

	@Override
	public void readUpdatePacket(PacketBuffer buf) {
		super.readUpdatePacket(buf);
		if (list.readUpdatePacket(buf, FMLCommonHandler.instance().getEffectiveSide().isClient())) {
			sendUpdatePacket();
		}
	}

}
