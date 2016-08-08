package sonar.bagels.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import sonar.bagels.Bagels;
import sonar.bagels.client.gui.GuiTodoList;
import sonar.bagels.common.containers.ContainerTodoList;
import sonar.bagels.utils.IGuiPart;
import sonar.bagels.utils.TodoList;

public class Clipboard extends Item implements IGuiPart {

	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			player.openGui(Bagels.instance, this.getHashedID(), world, -1000, -1000, -1000);
			return new ActionResult(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public Object getServerElement(EntityPlayer player) {
		return new ContainerTodoList(player, TodoList.getListFromStack(player.getHeldItemMainhand()));
	}

	@Override
	public Object getClientElement(EntityPlayer player) {
		return new GuiTodoList.Item(player, TodoList.getListFromStack(player.getHeldItemMainhand()));
	}

	@Override
	public int getHashedID() {
		return 0;
	}

	public String getDisplayName() {
		return null;

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
