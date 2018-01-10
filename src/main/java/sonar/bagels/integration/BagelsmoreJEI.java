package sonar.bagels.integration;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;
import sonar.bagels.Bagels;
import sonar.bagels.client.gui.GuiDeskCrafting;
import sonar.bagels.common.containers.ContainerDeskCrafting;

@JEIPlugin
public class BagelsmoreJEI extends BlankModPlugin {

	@Override
	public void register(IModRegistry registry) {
		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
		registry.addRecipeCatalyst(new ItemStack(Bagels.deskFancy, 1), VanillaRecipeCategoryUid.CRAFTING);
		registry.addRecipeClickArea(GuiDeskCrafting.class, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerDeskCrafting.class, VanillaRecipeCategoryUid.CRAFTING, 0, 9, 10, 36);
		registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(Bagels.blockPaper));
	}

}
