package sonar.bagels.utils;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import sonar.bagels.Bagels;
import sonar.bagels.client.gui.GuiTodoList;

public class TodoList {

	public String listName = "Todo List";
	public String[] entries = new String[10];

	public void writeListToStack(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound list = writeToNBT(new NBTTagCompound());
		stack.getTagCompound().setTag("todo", list);
	}

	public static TodoList getListFromStack(ItemStack stack) {
		TodoList list = new TodoList();
		if (stack != null && (stack.getItem() == Bagels.clipboard || stack.getItem() == Items.PAPER) && stack.hasTagCompound() && stack.getTagCompound().hasKey("todo")) {
			list.readFromNBT(stack.getTagCompound().getCompoundTag("todo"));
		}
		return list;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("name", listName);
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

	public void readFromNBT(NBTTagCompound tag) {
		listName = tag.getString("name");
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

	public boolean readUpdatePacket(ByteBuf buf, boolean isClient) {
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
		if (isClient)
			Bagels.proxy.updateTodoListGui();
		return isClient;
	}
}
