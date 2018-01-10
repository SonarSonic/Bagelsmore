package sonar.bagels.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sonar.bagels.Bagels;
import sonar.bagels.items.Clipboard;
import sonar.bagels.utils.TodoList;

public class PacketClipboard implements IMessage {

	public TodoList list;
	public ByteBuf recievedBuf;

	public PacketClipboard() {
	}

	public PacketClipboard(TodoList list) {
		this.list = list;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		buf.retain();
		recievedBuf = buf;
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		list.writeUpdatePacket(buf, true);
	}

	public static class Handler implements IMessageHandler<PacketClipboard, IMessage> {

		@Override
		public IMessage onMessage(PacketClipboard message, MessageContext ctx) {
			EntityPlayer player = Bagels.proxy.getPlayerEntity(ctx);
			ItemStack stack = player.getHeldItemMainhand();
			if (stack != null && stack.getItem() instanceof Clipboard) {
				TodoList list = TodoList.getListFromStack(stack);
				list.readUpdatePacket(message.recievedBuf, false);
				list.writeListToStack(stack);
				message.recievedBuf.release();
			}
			return null;
		}

	}

}
