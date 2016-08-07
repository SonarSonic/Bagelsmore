package sonar.bagels.network;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import sonar.bagels.Bagels;
import sonar.bagels.parts.IGuiPart;

public class BagelCommon implements IGuiHandler {

	public void registerRenderThings() {
	}

	public void registerSpecialRenderers() {
	}

	public static void registerPackets() {
		if (Bagels.network == null) {
			Bagels.network = NetworkRegistry.INSTANCE.newSimpleChannel(Bagels.modid);
		}

		Bagels.network.registerMessage(PacketToDoList.Handler.class, PacketToDoList.class, 0, Side.SERVER);

	}

	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().playerEntity;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		IMultipartContainer container = MultipartHelper.getPartContainer(world, new BlockPos(x, y, z));
		for (IMultipart part : container.getParts()) {
			if (part != null && part instanceof IGuiPart && ((IGuiPart) part).getHashedID() == ID) {
				return ((IGuiPart) part).getServerElement(player);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		IMultipartContainer container = MultipartHelper.getPartContainer(world, new BlockPos(x, y, z));
		for (IMultipart part : container.getParts()) {
			if (part != null && part instanceof IGuiPart && ((IGuiPart) part).getHashedID() == ID) {
				return ((IGuiPart) part).getClientElement(player);
			}
		}
		return null;
	}

}