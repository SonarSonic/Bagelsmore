package sonar.bagels.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import sonar.bagels.client.gui.GuiTodoList;
import sonar.bagels.common.containers.ContainerTodoList;
import sonar.bagels.utils.TodoList;
import sonar.core.SonarCore;
import sonar.core.api.IFlexibleGui;

public class Clipboard extends Item implements IFlexibleGui {

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote) {
			SonarCore.instance.guiHandler.openBasicItemStack(false, stack, player, world, player.getPosition(), 0);
			return new ActionResult(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public Object getServerElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
		return new ContainerTodoList(player, TodoList.getListFromStack(player.getHeldItemMainhand()));
	}

	@Override
	public Object getClientElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
		return new GuiTodoList.Item(player, TodoList.getListFromStack(player.getHeldItemMainhand()));
	}

	public String getListName(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("todo")) {
			String name = stack.getTagCompound().getCompoundTag("todo").getString("name");
			if (name != null && !name.isEmpty() && !name.equals(""))
				return name;
		}
		return null;
	}

	public String getHighlightTip(ItemStack stack, String displayName) {
		String listName = this.getListName(stack);
		return listName != null ? listName : displayName;
	}

	public String getItemStackDisplayName(ItemStack stack) {
		String listName = this.getListName(stack);
		return listName != null ? listName : super.getItemStackDisplayName(stack);
	}
}
