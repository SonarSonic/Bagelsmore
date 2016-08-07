package sonar.bagels.parts;

import java.util.List;

import io.netty.buffer.ByteBuf;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
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

public class Paper extends SidedMultipart implements IGuiPart {

	public boolean wasSet = false;
	public String listName = "Todo List";
	public String[] entries = new String[10];

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
		if (!this.getWorld().isRemote)
			player.openGui(Bagels.instance, this.getHashedID(), this.getWorld(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
		return true;
	}

	@Override
	public ItemStack createItemStack() {
		return null;
	}

	@Override
	public int getHashedID() {
		return -1;
	}

	@Override
	public Object getServerElement(EntityPlayer player) {
		return new ContainerTodoList(this);
	}

	@Override
	public Object getClientElement(EntityPlayer player) {
		return new GuiTodoList(this);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString("listName", listName);
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < entries.length; i++) {
			String entry = entries[i];
			if (entry != null && !entry.isEmpty()) {
				list.appendTag(new NBTTagString(entry));
			}
		}
		tag.setTag("entries", list);
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		listName = tag.getString("listName");
		NBTTagList list = tag.getTagList("entries", 8);
		for (int i = 0; i < list.tagCount(); i++) {
			String entry = list.getStringTagAt(i);
			entries[i] = entry;
		}
	}

	public void writeUpdatePacket(ByteBuf buf, boolean isClient) {
		ByteBufUtils.writeUTF8String(buf, listName);
		for (int i = 0; i < entries.length; i++) {
			String entry = entries[i];
			if (entry != null) {
				ByteBufUtils.writeUTF8String(buf, entry);
			}
		}
	}

	public void readUpdatePacket(ByteBuf buf, boolean isClient) {
		listName = ByteBufUtils.readUTF8String(buf);
		int offset = 0;
		for (int i = 0; i < entries.length; i++) {
			if (buf.isReadable()) {
				String entry = ByteBufUtils.readUTF8String(buf);
				entries[i - offset] = entry;
				if (entry.isEmpty() || entry.equals("")) {
					offset++;
				}
			} else {
				break;
			}
		}
		if (isClient) {
			GuiScreen screen = FMLClientHandler.instance().getClient().currentScreen;
			if (screen instanceof GuiTodoList) {
				((GuiTodoList) screen).reset(false);
			}
			this.wasSet = true;
		} else {
			sendUpdatePacket();
		}
	}

	@Override
	public void writeUpdatePacket(PacketBuffer buf) {
		super.writeUpdatePacket(buf);
		this.writeUpdatePacket(buf, FMLCommonHandler.instance().getEffectiveSide().isClient());
	}

	@Override
	public void readUpdatePacket(PacketBuffer buf) {
		super.readUpdatePacket(buf);
		this.readUpdatePacket(buf, FMLCommonHandler.instance().getEffectiveSide().isClient());
	}

}
