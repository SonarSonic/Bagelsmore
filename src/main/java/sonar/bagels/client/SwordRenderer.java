package sonar.bagels.client;

import org.lwjgl.opengl.GL11;

import mcmultipart.client.multipart.MultipartSpecialRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import sonar.bagels.parts.SwordMount;

//TWEAKED FAST MSR
public class SwordRenderer extends MultipartSpecialRenderer<SwordMount> {

	@Override
	public void renderMultipartAt(SwordMount part, double x, double y, double z, float partialTicks, int destroyStage) {
		ItemStack stack = part.getSword();
		if (stack != null) {
			
			Entity view = Minecraft.getMinecraft().getRenderViewEntity();
			double vX = view.lastTickPosX + (view.posX - view.lastTickPosX) * (double) partialTicks;
			double vY = view.lastTickPosY + (view.posY - view.lastTickPosY) * (double) partialTicks;
			double vZ = view.lastTickPosZ + (view.posZ - view.lastTickPosZ) * (double) partialTicks;
			GlStateManager.pushMatrix();
			GlStateManager.translate(part.getPos().getX() - vX, part.getPos().getY() - vY, part.getPos().getZ() - vZ);			
			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
			GL11.glRotated(part.face.getHorizontalAngle(), 0, 1, 0);
			switch(part.face){
			case EAST:
				GL11.glTranslated(0.41, 1+ 0.0425,-0.0625*2);
				break;
			case NORTH:
				GL11.glTranslated(-1+0.41, 1+ 0.0425, -1+ 0.0625*2);
				break;
			case SOUTH:
				GL11.glTranslated(0.41, 1+ 0.0425, 0.0625*2);
				break;
			case WEST:
				GL11.glTranslated(-1+0.41, 1+ 0.0425, 1- 0.0625*2);
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
