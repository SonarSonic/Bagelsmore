package sonar.bagels.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sonar.bagels.Bagels;
import sonar.bagels.common.containers.ContainerStorageDrawer;
import sonar.bagels.common.tileentity.TileStorageDrawer;
import sonar.core.client.gui.GuiSonar;

@SideOnly(Side.CLIENT)
public class GuiStorageDrawer extends GuiSonar {
	private static final ResourceLocation SMALL = new ResourceLocation(Bagels.MODID + ":textures/gui/small_drawer.png");
	private static final ResourceLocation LARGE = new ResourceLocation(Bagels.MODID + ":textures/gui/large_drawer.png");
	private final TileStorageDrawer inv;

	public GuiStorageDrawer(TileStorageDrawer inv) {
		super(new ContainerStorageDrawer(inv, Minecraft.getMinecraft().player));
		this.inv = inv;
		this.xSize = 176;
		this.ySize = 186;
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString("Storage Drawer", 8, 6, 4210752);
		if (inv.getSizeInventory() == 32) {
			this.fontRenderer.drawString("Layer 1", 8, 92, 4210752);
			this.fontRenderer.drawString("Layer 2", 8 + 90, 92, 4210752);
		}
	}

	@Override
	public ResourceLocation getBackground() {
		return inv.getSizeInventory() == 32 ? LARGE : SMALL;
	}
}