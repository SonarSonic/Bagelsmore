package sonar.bagels.client;

import static net.minecraft.client.renderer.GlStateManager.popMatrix;
import static net.minecraft.client.renderer.GlStateManager.pushMatrix;
import static net.minecraft.client.renderer.GlStateManager.rotate;
import static net.minecraft.client.renderer.GlStateManager.scale;
import static net.minecraft.client.renderer.GlStateManager.translate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import sonar.bagels.api.DeskMetadata;
import sonar.bagels.common.tileentity.TileDesk;

//horrors lie in this code, please cover your eyes and pray to your respective god. which could be me because yes I still have clear conscious
public class TileDeskRightRenderer extends TileEntitySpecialRenderer<TileDesk.RIGHT> {

	@Override
	public void render(TileDesk.RIGHT te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		EnumFacing face = DeskMetadata.getMetaMap(te.getBlockMetadata()).getFace();
		double p = 0.0625;
		Entity view = Minecraft.getMinecraft().getRenderViewEntity();
		double vX = view.lastTickPosX + (view.posX - view.lastTickPosX) * (double) partialTicks;
		double vY = view.lastTickPosY + (view.posY - view.lastTickPosY) * (double) partialTicks;
		double vZ = view.lastTickPosZ + (view.posZ - view.lastTickPosZ) * (double) partialTicks;
		pushMatrix();
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		translate(te.getPos().getX() - vX, te.getPos().getY() - vY, te.getPos().getZ() - vZ);
		rotate(face.getHorizontal(face.getHorizontalIndex() + 1).getHorizontalAngle(), 0, 1, 0);
		scale(0.5, 0.5, 0.5);
		double offsetY = 1.8625, offsetX = 1.5;
		switch (face) {
		case EAST:
			translate(-1+offsetX, offsetY, offsetX);
			break;
		case NORTH:
			rotate(180, 0, 1, 0);
			translate(-offsetX, offsetY, offsetX);	
			break;
		case SOUTH:
			rotate(180, 0, 1, 0);
			translate(-1+offsetX, offsetY, 1 - offsetX);
			break;
		case WEST:
			translate(-offsetX, offsetY, 1 - offsetX);
			break;
		default:
			break;

		}
		//translate(0.5, 1.8, 1.5);
		for (int i = 0; i < 9; i++) {
			int row = i / 3;
			int colomn = i - (row * 3);
			pushMatrix();
			translate(row * (p * 8), 0, -colomn * (p * 8));
			if (i == 4) {
				translate(0, 0.5, 0);
				
				int rotation = (int) ((int) -view.rotationYaw - (face.getAxis()==Axis.X? face.getHorizontal(face.getHorizontalIndex()+1).getHorizontalAngle(): face.getHorizontal(face.getOpposite().getHorizontalIndex()+1).getHorizontalAngle()));
				
				rotate(rotation, 0, 1, 0);
				ItemStack result = te.getStackInSlot(9);
				if (!result.isEmpty() && result.getCount() != 0) {
					renderItem.renderItem(result, TransformType.GROUND);
				}
				rotate(-rotation, 0, 1, 0);
				translate(0, -0.5, 0);
			}
			ItemStack stack = te.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getCount() != 0) {
				if (!renderItem.shouldRenderItemIn3D(stack)) {
					translate(p * 2, p * 2, 0);
					rotate(90, 0, 0, 1);
				}
				// ouch, that moment when you can't be bothered to scroll up to
				// fix your own code, so you fix it at the bottom instead
				// #majestic
				
				if (face == EnumFacing.NORTH || face == EnumFacing.SOUTH) {
					rotate(-face.getHorizontal(face.getHorizontalIndex() - 1).getHorizontalAngle(), 0, 1, 0);
				} else if (face == EnumFacing.EAST) {
					rotate(face.getHorizontal(face.getHorizontalIndex()).getHorizontalAngle(), 0, 1, 0);
				} else {
					rotate(face.getHorizontal(face.getOpposite().getHorizontalIndex()).getHorizontalAngle(), 0, 1, 0);
				}

				renderItem.renderItem(stack, TransformType.GROUND);
			}
			popMatrix();
		}

		popMatrix();
	}

}
