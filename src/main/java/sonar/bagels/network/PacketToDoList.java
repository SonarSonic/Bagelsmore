package sonar.bagels.network;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sonar.bagels.Bagels;
import sonar.bagels.parts.Paper;

public class PacketToDoList implements IMessage {

	public Paper paper;

	public BlockPos recievedPos;
	public ByteBuf recievedBuf;
	public UUID recievedUUID;

	public PacketToDoList() {
	}

	public PacketToDoList(Paper paper) {
		this.paper = paper;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		long msb = buf.readLong();
		long lsb = buf.readLong();
		recievedUUID = new UUID(msb, lsb);
		recievedPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		recievedBuf = buf;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		UUID partID = paper.getContainer().getPartID(paper);
		buf.writeLong(partID.getMostSignificantBits());
		buf.writeLong(partID.getLeastSignificantBits());

		buf.writeInt(paper.getPos().getX());
		buf.writeInt(paper.getPos().getY());
		buf.writeInt(paper.getPos().getZ());
		paper.writeUpdatePacket(buf, true);
	}

	public static class Handler implements IMessageHandler<PacketToDoList, IMessage> {

		@Override
		public IMessage onMessage(PacketToDoList message, MessageContext ctx) {
			IMultipartContainer container = MultipartHelper.getPartContainer(Bagels.proxy.getPlayerEntity(ctx).getEntityWorld(), message.recievedPos);
			if (container != null) {
				IMultipart part = container.getPartFromID(message.recievedUUID);
				if (part != null && part instanceof Paper) {
					((Paper) part).readUpdatePacket(message.recievedBuf, false);
				}
			}
			return null;
		}

	}

}
