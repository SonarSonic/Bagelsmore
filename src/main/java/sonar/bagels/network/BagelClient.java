package sonar.bagels.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sonar.bagels.client.DrawerRenderer;
import sonar.bagels.client.SwordRenderer;
import sonar.bagels.client.TileDeskRightRenderer;
import sonar.bagels.client.gui.GuiTodoList;
import sonar.bagels.common.tileentity.TileDesk;
import sonar.bagels.common.tileentity.TileEnderDrawer;
import sonar.bagels.common.tileentity.TileFluidDrawer;
import sonar.bagels.common.tileentity.TileRecyclingDrawer;
import sonar.bagels.common.tileentity.TileSmeltingDrawer;
import sonar.bagels.common.tileentity.TileStorageDrawer;
import sonar.bagels.common.tileentity.TileSwordMount;

public class BagelClient extends BagelCommon {

	public void registerRenderThings() {}

	public void registerSpecialRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileDesk.RIGHT.class, new TileDeskRightRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileSwordMount.class, new SwordRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileStorageDrawer.Large.class, new DrawerRenderer.StorageDrawer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileStorageDrawer.Small.class, new DrawerRenderer.StorageDrawer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEnderDrawer.class, new DrawerRenderer.EnderDrawer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileFluidDrawer.class, new DrawerRenderer.FluidDrawer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileRecyclingDrawer.class, new DrawerRenderer.RecyclingDrawer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileSmeltingDrawer.class, new DrawerRenderer.SmeltingDrawer());
	}

	public void updateTodoListGui() {
		GuiScreen screen = FMLClientHandler.instance().getClient().currentScreen;
		if (screen instanceof GuiTodoList) {
			((GuiTodoList) screen).reset(false);
		}
	}

	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
	}
}
