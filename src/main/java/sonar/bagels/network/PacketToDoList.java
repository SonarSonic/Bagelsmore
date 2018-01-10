package sonar.bagels.network;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import mcmultipart.api.multipart.IMultipartTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sonar.bagels.common.tileentity.TilePaper;
import sonar.core.SonarCore;
import sonar.core.network.PacketMultipart;
import sonar.core.network.PacketMultipartHandler;

public class PacketToDoList extends PacketMultipart {

	public TilePaper paper;

	public BlockPos recievedPos;
	public ByteBuf recievedBuf;
	public UUID recievedUUID;

	public PacketToDoList() {}

	public PacketToDoList(TilePaper multipart) {
		super(multipart.getSlotID(), multipart.getPos());
		this.paper = multipart;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		buf.retain();
		recievedBuf = buf;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		paper.list.writeUpdatePacket(buf, true);
	}

	public static class Handler extends PacketMultipartHandler<PacketToDoList> {

		@Override
		public IMessage processMessage(PacketToDoList message, EntityPlayer player, World world, IMultipartTile part, MessageContext ctx) {

			SonarCore.proxy.getThreadListener(ctx.side).addScheduledTask(() -> {
				if (part != null && part instanceof TilePaper) {
					((TilePaper) part).list.readUpdatePacket(message.recievedBuf, false);
					((TilePaper) part).markDirty();
				}
				message.recievedBuf.release();

			});
			return null;
		}

	}

}
