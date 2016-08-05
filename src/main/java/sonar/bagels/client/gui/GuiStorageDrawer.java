package sonar.bagels.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sonar.bagels.common.containers.ContainerStorageDrawer;
import sonar.bagels.parts.InventoryMultipart;

@SideOnly(Side.CLIENT)
public class GuiStorageDrawer extends GuiContainer {
	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
	private final InventoryMultipart inv;
	private final int inventoryRows;

	public GuiStorageDrawer(InventoryMultipart inv) {
		super(new ContainerStorageDrawer(inv, Minecraft.getMinecraft().thePlayer));
		this.inv = inv;
		this.allowUserInput = false;
		int i = 222;
		int j = 114;
		this.inventoryRows = inv.inv.getSlots() / 9;
		this.ySize = 114 + this.inventoryRows * 18;
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRendererObj.drawString("Storage Drawer", 8, 6, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
		this.drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
	}
}