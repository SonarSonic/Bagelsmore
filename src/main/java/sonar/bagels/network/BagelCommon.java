package sonar.bagels.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import sonar.bagels.Bagels;
import sonar.bagels.BagelsConstants;

public class BagelCommon {

	public void registerRenderThings() {}

	public void registerSpecialRenderers() {}

	public void updateTodoListGui() {}

	public static void registerPackets() {
		if (Bagels.network == null) {
			Bagels.network = NetworkRegistry.INSTANCE.newSimpleChannel(BagelsConstants.MODID);
		}

		Bagels.network.registerMessage(PacketToDoList.Handler.class, PacketToDoList.class, 0, Side.SERVER);
		Bagels.network.registerMessage(PacketClipboard.Handler.class, PacketClipboard.class, 1, Side.SERVER);

	}

	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().player;
	}

}