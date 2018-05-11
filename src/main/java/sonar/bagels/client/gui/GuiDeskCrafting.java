package sonar.bagels.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sonar.bagels.BagelsConstants;
import sonar.bagels.common.containers.ContainerDeskCrafting;
import sonar.core.client.gui.GuiSonar;
import sonar.core.inventory.ISonarInventoryTile;

@SideOnly(Side.CLIENT)
public class GuiDeskCrafting extends GuiSonar {
	private static final ResourceLocation background = new ResourceLocation(BagelsConstants.MODID + ":textures/gui/crafting_part.png");
	private final ISonarInventoryTile inv;

	public GuiDeskCrafting(EntityPlayer player, ISonarInventoryTile tileDeskCraftingPart) {
		super(new ContainerDeskCrafting(player, tileDeskCraftingPart));
		this.inv = tileDeskCraftingPart;
		this.xSize = 176;
		this.ySize = 166;

	}

	public void drawGuiContainerForegroundLayer(int x, int y) {
		super.drawGuiContainerForegroundLayer(x, y);
		this.fontRenderer.drawString("Desk Crafting", 8, 6, 4210752);
	}

	@Override
	public ResourceLocation getBackground() {
		return background;
	}
}