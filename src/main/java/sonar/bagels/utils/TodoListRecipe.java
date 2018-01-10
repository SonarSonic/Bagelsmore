package sonar.bagels.utils;

import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class TodoListRecipe extends ShapelessOreRecipe {

	public TodoListRecipe(ResourceLocation location, ItemStack result, Object... recipe) {
		super(location, result, recipe);
	}

	public TodoListRecipe(ResourceLocation location, ItemStack output, List<ItemStack> inputList) {
		super(location, output, inputList);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting crafting) {
		ItemStack outputStack = output.copy();
		for (int i = 0; i < crafting.getSizeInventory(); i++) {
			ItemStack stack = crafting.getStackInSlot(i);
			if (stack != null && stack.getItem() == Items.PAPER) {
				TodoList list = TodoList.getListFromStack(stack);
				list.writeListToStack(outputStack);
			}
		}
		return outputStack;
	}
}
