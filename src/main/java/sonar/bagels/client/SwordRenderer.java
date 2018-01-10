package sonar.bagels.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import sonar.bagels.common.tileentity.TileSwordMount;

//TWEAKED FAST MSR
public class SwordRenderer extends TileEntitySpecialRenderer<TileSwordMount> {

	@Override
	public void render(TileSwordMount te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		ItemStack stack = te.getSword();
		if (!stack.isEmpty()) {
			EnumFacing face = EnumFacing.VALUES[te.getBlockMetadata()];
			Entity view = Minecraft.getMinecraft().getRenderViewEntity();
			double vX = view.lastTickPosX + (view.posX - view.lastTickPosX) * (double) partialTicks;
			double vY = view.lastTickPosY + (view.posY - view.lastTickPosY) * (double) partialTicks;
			double vZ = view.lastTickPosZ + (view.posZ - view.lastTickPosZ) * (double) partialTicks;
			GlStateManager.pushMatrix();
			GlStateManager.translate(te.getPos().getX() - vX, te.getPos().getY() - vY, te.getPos().getZ() - vZ);
			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
			GL11.glRotated(face.getHorizontalAngle(), 0, 1, 0);
			switch (face) {
			case EAST:
				GL11.glTranslated(0.41, 1 + 0.0425, -0.0625 * 2);
				break;
			case NORTH:
				GL11.glTranslated(-1 + 0.41, 1 + 0.0425, -1 + 0.0625 * 2);
				break;
			case SOUTH:
				GL11.glTranslated(0.41, 1 + 0.0425, 0.0625 * 2);
				break;
			case WEST:
				GL11.glTranslated(-1 + 0.41, 1 + 0.0425, 1 - 0.0625 * 2);
				break;
			default:
				break;
			}
			GL11.glScaled(1, 1, 1);
			GL11.glRotated(-45, 0, 0, 1);
			GL11.glRotated(-90, 0, 0, 1);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, TransformType.GROUND);

			GlStateManager.popMatrix();

		}

	}
}
