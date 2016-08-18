package sonar.bagels.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sonar.bagels.Bagels;
import sonar.bagels.common.containers.ContainerRecyclingDrawer;
import sonar.bagels.parts.RecyclingDrawer;

@SideOnly(Side.CLIENT)
public class GuiRecyclingDrawer extends GuiContainer {
	private static final ResourceLocation background = new ResourceLocation(Bagels.modid + ":textures/gui/recycling_drawer.png");
	private final RecyclingDrawer inv;

	public GuiRecyclingDrawer(RecyclingDrawer inv) {
		super(new ContainerRecyclingDrawer(Minecraft.getMinecraft().thePlayer.inventory, inv));
		this.inv = inv;
		this.xSize = 176;
		this.ySize = 166;

	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRendererObj.drawString("Recycling Drawer", 8, 6, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
}