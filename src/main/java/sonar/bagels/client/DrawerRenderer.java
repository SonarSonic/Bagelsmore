package sonar.bagels.client;

import static net.minecraft.client.renderer.GlStateManager.blendFunc;
import static net.minecraft.client.renderer.GlStateManager.disableBlend;
import static net.minecraft.client.renderer.GlStateManager.disableCull;
import static net.minecraft.client.renderer.GlStateManager.disableLighting;
import static net.minecraft.client.renderer.GlStateManager.enableBlend;
import static net.minecraft.client.renderer.GlStateManager.popAttrib;
import static net.minecraft.client.renderer.GlStateManager.popMatrix;
import static net.minecraft.client.renderer.GlStateManager.pushAttrib;
import static net.minecraft.client.renderer.GlStateManager.pushMatrix;
import static net.minecraft.client.renderer.GlStateManager.rotate;
import static net.minecraft.client.renderer.GlStateManager.scale;
import static net.minecraft.client.renderer.GlStateManager.shadeModel;
import static net.minecraft.client.renderer.GlStateManager.translate;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import sonar.bagels.common.tileentity.TileDrawer;
import sonar.bagels.common.tileentity.TileEnderDrawer;
import sonar.bagels.common.tileentity.TileFluidDrawer;
import sonar.bagels.common.tileentity.TileRecyclingDrawer;
import sonar.bagels.common.tileentity.TileSmeltingDrawer;
import sonar.core.inventory.ISonarInventoryTile;

public class DrawerRenderer<T extends TileDrawer> extends TileEntitySpecialRenderer<T> {

	public static class StorageDrawer<T extends TileDrawer & ISonarInventoryTile> extends DrawerRenderer<T> {

		public void renderSpecials(T drawer, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
			for (int i = 0; i < drawer.getSizeInventory(); i++) {
				pushMatrix();
				int layer = i / 16;
				int row = (i - (layer * 16)) / 4;
				int colomn = i - (layer * 16) - (row * 4);
				translate(row * (0.0625 * 5), layer * 0.0625 * 4.5, colomn * (0.0625 * 5));
				ItemStack stack = drawer.getStackInSlot(i);
				if (!stack.isEmpty() && stack.getCount() != 0) {
					renderItem.renderItem(stack, TransformType.GROUND);
				}
				popMatrix();
			}

		}
	}

	public static class FluidDrawer<T extends TileFluidDrawer> extends DrawerRenderer<T> {

		public void renderSpecials(T drawer, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
			FluidTank[] tanks = drawer.tanks;
			for (int i = 0; i < tanks.length; i++) {
				FluidTank tank = tanks[i];
				if (tank.getFluidAmount() != 0 && tank.getFluid() != null) {
					pushAttrib();
					pushMatrix();
					disableLighting();
					disableBlend();
					ResourceLocation fluid = tank.getFluid().getFluid().getStill();
					double ySize = (-0.0625 * 6.5) * ((double) tank.getFluidAmount() / tank.getCapacity());
					translate(0.0, 0, -0.2);
					drawTexturedModalRect(i == 1 ? -0.0625 * 2 - 0.05 : 0.0625 * 12 - 0.05, -0.0625 * 0.5, fluid, 0.4, 0.0625 * 3.5 - ySize);
					translate(0.0, 0, 0.18);
					rotate(90, 1, 0, 0);
					translate(i == 1 ? 0.0625 : 0.0625 * 11, 0, -0.0625 * 3 + ySize);
					drawTexturedModalRect(-0.0625 * 3 - 0.05, -0.0625 * 3, fluid, 0.7, 1.5);
					popMatrix();
					popAttrib();

				}
			}

		}
	}

	public static class EnderDrawer<T extends TileEnderDrawer> extends DrawerRenderer<T> {

		public static final ResourceLocation portal = new ResourceLocation("minecraft:blocks/obsidian");

		public void renderSpecials(TileEnderDrawer drawer, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
			renderLiquidInDrawer(portal, x, y, z, partialTicks, destroyStage, alpha);
		}
	}

	public static class SmeltingDrawer<T extends TileSmeltingDrawer> extends DrawerRenderer<T> {

		public static final ResourceLocation portal = FluidRegistry.LAVA.getStill();

		public void renderSpecials(TileSmeltingDrawer drawer, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
			renderLiquidInDrawer(portal, x, y, z, partialTicks, destroyStage, alpha);
		}
	}

	public static class RecyclingDrawer<T extends TileRecyclingDrawer> extends DrawerRenderer<T> {

		public static final ResourceLocation portal = new ResourceLocation("minecraft:blocks/portal");

		public void renderSpecials(TileRecyclingDrawer drawer, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
			renderLiquidInDrawer(portal, x, y, z, partialTicks, destroyStage, alpha);
		}
	}

	public void renderLiquidInDrawer(ResourceLocation location, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		pushAttrib();
		disableLighting();
		rotate(90, 1, 0, 0);
		translate(0, 0, -0.0625 * 5);
		drawTexturedModalRect(-0.0625 * 3 - 0.05, -0.0625 * 3, location, 1.45, 1.45);
		popAttrib();
	}

	public void renderSpecials(T drawer, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {}

	@Override
	public void render(T drawer, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		EnumFacing face = EnumFacing.VALUES[drawer.getBlockMetadata()];

		Entity view = Minecraft.getMinecraft().getRenderViewEntity();
		double dX = view.lastTickPosX + (view.posX - view.lastTickPosX) * (double) partialTicks;
		double dY = view.lastTickPosY + (view.posY - view.lastTickPosY) * (double) partialTicks;
		double dZ = view.lastTickPosZ + (view.posZ - view.lastTickPosZ) * (double) partialTicks;
		pushMatrix();
		translate(-dX, -dY, -dZ);
		disableCull();

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		RenderHelper.disableStandardItemLighting();
		blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		enableBlend();
		disableCull();

		if (Minecraft.isAmbientOcclusionEnabled()) {
			shadeModel(GL11.GL_SMOOTH);
		} else {
			shadeModel(GL11.GL_FLAT);
		}
		translate(0, drawer.getDrawerPosition().offsetY(), 0);
		if (drawer.isDrawerOpen()) {
			switch (face) {
			case EAST:
				translate(0.4, 0, 0);
				break;
			case NORTH:
				translate(0, 0, -0.4);
				break;
			case SOUTH:
				translate(0, 0, 0.4);
				break;
			case WEST:
				translate(-0.4, 0, 0);
				break;
			default:
				break;

			}
		}

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		IBlockState state = drawer.getWorld().getBlockState(drawer.getPos());
		IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
		BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		renderer.getBlockModelRenderer().renderModel(drawer.getWorld(), model, state, drawer.getPos(), buffer, true);
		buffer.setTranslation(0, 0, 0);
		tessellator.draw();

		RenderHelper.enableStandardItemLighting();
		if (drawer.isDrawerOpen() || drawer.shouldRenderSpecials) {
			translate(drawer.getPos().getX(), drawer.getPos().getY(), drawer.getPos().getZ());
			switch (face) {

			case EAST:
				rotate(-90, 0, 1, 0);
				translate(0, 0, -1);
				break;
			case NORTH:
				break;
			case SOUTH:
				rotate(180, 0, 1, 0);
				translate(-1, 0, -1);
				break;
			case WEST:
				rotate(90, 0, 1, 0);
				translate(-1, 0, 0);
				break;
			default:
				break;

			}

			scale(0.5, 0.5, 0.5);
			translate(0.0625 * 8.5, 0.0625 * 4, 0.0625 * 5);
			renderSpecials(drawer, x, y, z, partialTicks, destroyStage, alpha);
		}
		popMatrix();

	}

	public void drawTexturedModalRect(double xCoord, double yCoord, ResourceLocation location, double widthIn, double heightIn) {
		TextureAtlasSprite textureSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
		if (textureSprite != null) {
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder vertexbuffer = tessellator.getBuffer();
			vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
			vertexbuffer.pos((double) (xCoord + 0), (double) (yCoord + heightIn), (double) 0).tex((double) textureSprite.getMinU(), (double) textureSprite.getMaxV()).endVertex();
			vertexbuffer.pos((double) (xCoord + widthIn), (double) (yCoord + heightIn), (double) 0).tex((double) textureSprite.getMaxU(), (double) textureSprite.getMaxV()).endVertex();
			vertexbuffer.pos((double) (xCoord + widthIn), (double) (yCoord + 0), (double) 0).tex((double) textureSprite.getMaxU(), (double) textureSprite.getMinV()).endVertex();
			vertexbuffer.pos((double) (xCoord + 0), (double) (yCoord + 0), (double) 0).tex((double) textureSprite.getMinU(), (double) textureSprite.getMinV()).endVertex();
			tessellator.draw();
		}
	}
}
