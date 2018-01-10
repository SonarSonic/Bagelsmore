package sonar.bagels.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sonar.bagels.Bagels;
import sonar.bagels.common.containers.ContainerBookshelf;
import sonar.bagels.common.tileentity.TileBookshelf;
import sonar.core.client.gui.GuiSonar;

@SideOnly(Side.CLIENT)
public class GuiBookshelf extends GuiSonar {
	private static final ResourceLocation background = new ResourceLocation(Bagels.MODID + ":textures/gui/bookshelf.png");
	private final TileBookshelf inv;

	public GuiBookshelf(TileBookshelf inv) {
		super(new ContainerBookshelf(Minecraft.getMinecraft().player.inventory, inv));
		this.inv = inv;
		this.xSize = 176;
		this.ySize = 166;

	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString("Bookshelf", 8, 6, 4210752);
	}

	@Override
	public ResourceLocation getBackground() {
		return background;
	}
}