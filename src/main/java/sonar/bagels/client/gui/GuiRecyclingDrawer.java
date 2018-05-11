package sonar.bagels.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sonar.bagels.BagelsConstants;
import sonar.bagels.common.containers.ContainerRecyclingDrawer;
import sonar.core.client.gui.GuiSonar;

@SideOnly(Side.CLIENT)
public class GuiRecyclingDrawer extends GuiSonar {
	private static final ResourceLocation background = new ResourceLocation(BagelsConstants.MODID + ":textures/gui/recycling_drawer.png");

	public GuiRecyclingDrawer(EntityPlayer player) {
		super(new ContainerRecyclingDrawer(player));
		this.xSize = 176;
		this.ySize = 166;

	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString("Recycling Drawer", 8, 6, 4210752);
	}
	@Override
	public ResourceLocation getBackground() {
		return background;
	}
}