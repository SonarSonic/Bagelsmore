package sonar.bagels.client;

import org.lwjgl.opengl.GL11;

import mcmultipart.client.multipart.MultipartSpecialRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import sonar.bagels.parts.DeskCraftingPart;

//horrors lie in this code, please cover your eyes and pray to your respective god. which could be me because yes I still have clear conscious
public class DeskCraftingRenderer extends MultipartSpecialRenderer<DeskCraftingPart> {

	@Override
	public void renderMultipartAt(DeskCraftingPart part, double x, double y, double z, float partialTicks, int destroyStage) {
		Entity view = Minecraft.getMinecraft().getRenderViewEntity();
		double vX = view.lastTickPosX + (view.posX - view.lastTickPosX) * (double) partialTicks;
		double vY = view.lastTickPosY + (view.posY - view.lastTickPosY) * (double) partialTicks;
		double vZ = view.lastTickPosZ + (view.posZ - view.lastTickPosZ) * (double) partialTicks;
		GlStateManager.pushMatrix();
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		GlStateManager.translate(part.getPos().getX() - vX, part.getPos().getY() - vY, part.getPos().getZ() - vZ);
		EnumFacing face = part.face;

		GL11.glRotated(face.getHorizontal(face.getHorizontalIndex() + 1).getHorizontalAngle(), 0, 1, 0);
		GL11.glScaled(0.5, 0.5, 0.5);
		double offsetY = 1.8625, offsetX = 1.5;
		switch (face) {
		case EAST:
			GL11.glTranslated(-1+offsetX, offsetY, offsetX);
			break;
		case NORTH:
			GL11.glRotated(180, 0, 1, 0);
			GL11.glTranslated(-offsetX, offsetY, offsetX);
			break;
		case SOUTH:
			GL11.glRotated(180, 0, 1, 0);
			GL11.glTranslated(-1+offsetX, offsetY, 1 - offsetX);
			break;
		case WEST:
			GL11.glTranslated(-offsetX, offsetY, 1 - offsetX);
			break;
		default:
			break;

		}
		for (int i = 0; i < 9; i++) {
			int row = i / 3;
			int colomn = i - (row * 3);
			GlStateManager.pushMatrix();
			GlStateManager.translate(row * (0.0625 * 8), 0, -colomn * (0.0625 * 8));
			if (i == 4) {
				GlStateManager.translate(0, 0.5, 0);
				
				int rotation = (int) ((int) -view.rotationYaw - (face.getAxis()==Axis.X? face.getHorizontal(face.getHorizontalIndex()+1).getHorizontalAngle(): face.getHorizontal(face.getOpposite().getHorizontalIndex()+1).getHorizontalAngle()));
				
				GlStateManager.rotate(rotation, 0, 1, 0);
				ItemStack result = part.inv.getStackInSlot(9);
				if (result != null && result.stackSize != 0) {
					renderItem.renderItem(result, TransformType.GROUND);
				}
				GlStateManager.rotate(-rotation, 0, 1, 0);
				GlStateManager.translate(0, -0.5, 0);
			}
			ItemStack stack = part.inv.getStackInSlot(i);
			if (stack != null && stack.stackSize != 0) {
				if (!renderItem.shouldRenderItemIn3D(stack)) {
					GlStateManager.translate(0.0625 * 2, 0.0625 * 2, 0);
					GlStateManager.rotate(90, 0, 0, 1);
				}
				// ouch, that moment when you can't be bothered to scroll up to
				// fix your own code, so you fix it at the bottom instead
				// #majestic
				if (face == EnumFacing.NORTH || face == EnumFacing.SOUTH) {
					GlStateManager.rotate(-face.getHorizontal(face.getHorizontalIndex() - 1).getHorizontalAngle(), 0, 1, 0);
				} else if (face == EnumFacing.EAST) {
					GlStateManager.rotate(face.getHorizontal(face.getHorizontalIndex()).getHorizontalAngle(), 0, 1, 0);
				} else {
					GlStateManager.rotate(face.getHorizontal(face.getOpposite().getHorizontalIndex()).getHorizontalAngle(), 0, 1, 0);
				}

				renderItem.renderItem(stack, TransformType.GROUND);
			}
			GlStateManager.popMatrix();
		}

		GlStateManager.popMatrix();
	}

}
