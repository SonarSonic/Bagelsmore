package sonar.bagels.client;

import org.lwjgl.opengl.GL11;

import mcmultipart.client.multipart.MultipartSpecialRenderer;
import mcmultipart.client.multipart.MultipartStateMapper;
import mcmultipart.multipart.PartState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import sonar.bagels.parts.DeskDrawer;
import sonar.bagels.parts.StorageDrawer;

//TWEAKED FAST MSR
public class DrawerRenderer extends MultipartSpecialRenderer<DeskDrawer> {

	@Override
	public void renderMultipartAt(DeskDrawer part, double x, double y, double z, float partialTicks, int destroyStage) {
		Entity view = Minecraft.getMinecraft().getRenderViewEntity();
		double dX = view.lastTickPosX + (view.posX - view.lastTickPosX) * (double) partialTicks;
		double dY = view.lastTickPosY + (view.posY - view.lastTickPosY) * (double) partialTicks;
		double dZ = view.lastTickPosZ + (view.posZ - view.lastTickPosZ) * (double) partialTicks;
		GlStateManager.pushMatrix();
		GlStateManager.translate(-dX, -dY, -dZ);
		GlStateManager.disableCull();

		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buffer = tessellator.getBuffer();
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableBlend();
		GlStateManager.disableCull();

		if (Minecraft.isAmbientOcclusionEnabled()) {
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
		} else {
			GlStateManager.shadeModel(GL11.GL_FLAT);
		}
		GlStateManager.translate(0, part.getDrawerPosition().offsetY(), 0);
		if (part.isDrawerOpen()) {
			switch (part.face) {
			case EAST:
				GlStateManager.translate(0.4, 0, 0);
				break;
			case NORTH:
				GlStateManager.translate(0, 0, -0.4);
				break;
			case SOUTH:
				GlStateManager.translate(0, 0, 0.4);
				break;
			case WEST:
				GlStateManager.translate(-0.4, 0, 0);
				break;
			default:
				break;

			}
		}

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		PartState partState = PartState.fromPart(part);
		ModelResourceLocation modelLocation = new ModelResourceLocation(partState.modelPath, MultipartStateMapper.instance.getPropertyString(partState.state.getProperties()));
		IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(modelLocation);
		BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		renderer.getBlockModelRenderer().renderModel(part.getWorld(), model, partState.state, part.getPos(), buffer, true);
		buffer.setTranslation(0, 0, 0);
		tessellator.draw();

		RenderHelper.enableStandardItemLighting();

		if (part instanceof StorageDrawer) {
			GlStateManager.translate(part.getPos().getX(), part.getPos().getY(), part.getPos().getZ());

			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
			GlStateManager.scale(0.5, 0.5, 0.5);
			GlStateManager.translate(0.0625*8.5, 0.0625*4, 0.0625*5);
			StorageDrawer drawer = (StorageDrawer) part;
			for (int i = 0; i < drawer.getInvSize(); i++) {
				int layer = i / 16;
				int row = (i - (layer * 16)) / 4;
				int colomn = i - (layer * 16) - (row * 4);
				GlStateManager.translate(row * (0.0625 * 5), layer*0.0625 * 5, colomn * (0.0625 * 5));
				ItemStack stack = drawer.inv.getStackInSlot(i);
				if (stack != null && stack.stackSize != 0) {
					renderItem.renderItem(stack, TransformType.GROUND);
				}
				GlStateManager.translate(-(row * (0.0625 * 5)), -layer*0.0625 * 5, -(colomn * (0.0625 * 5)));
			}
		}
		GlStateManager.popMatrix();
		

	}
}
