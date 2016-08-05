package sonar.bagels.network;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import sonar.bagels.parts.IGuiPart;

public class BagelCommon implements IGuiHandler {

	public void registerRenderThings() {
	}

	public void registerSpecialRenderers() {
	}

	public static void registerPackets() {
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