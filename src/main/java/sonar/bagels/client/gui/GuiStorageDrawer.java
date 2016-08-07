package sonar.bagels.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sonar.bagels.Bagels;
import sonar.bagels.common.containers.ContainerStorageDrawer;
import sonar.bagels.parts.InventoryMultipart;

@SideOnly(Side.CLIENT)
public class GuiStorageDrawer extends GuiContainer {
	private static final ResourceLocation SMALL = new ResourceLocation(Bagels.modid + ":textures/gui/small_drawer.png");
	private static final ResourceLocation LARGE = new ResourceLocation(Bagels.modid + ":textures/gui/large_drawer.png");
	private final InventoryMultipart inv;

	public GuiStorageDrawer(InventoryMultipart inv) {
		super(new ContainerStorageDrawer(inv, Minecraft.getMinecraft().thePlayer));
		this.inv = inv;
		this.xSize = 176;
		this.ySize = 186;

	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRendererObj.drawString("Storage Drawer", 8, 6, 4210752);
		if (inv.getInvSize() == 32) {
			this.fontRendererObj.drawString("Layer 1", 8, 92, 4210752);
			this.fontRendererObj.drawString("Layer 2", 8 + 90, 92, 4210752);
		}
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(inv.getInvSize() == 32 ? LARGE : SMALL);
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
}