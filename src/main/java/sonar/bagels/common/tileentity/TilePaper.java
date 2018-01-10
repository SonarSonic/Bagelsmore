package sonar.bagels.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import sonar.bagels.client.gui.GuiTodoList;
import sonar.bagels.common.containers.ContainerTodoList;
import sonar.bagels.utils.TodoList;
import sonar.core.api.IFlexibleGui;
import sonar.core.helpers.NBTHelper.SyncType;
import sonar.core.integration.multipart.TileSonarMultipart;

public class TilePaper extends TileSonarMultipart implements IFlexibleGui {

	public boolean wasSet = false;
	public TodoList list = new TodoList();

	@Override
	public NBTTagCompound writeData(NBTTagCompound nbt, SyncType type) {
		super.writeData(nbt, type);
		list.writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void readData(NBTTagCompound nbt, SyncType type) {
		super.readData(nbt, type);
		list.readFromNBT(nbt);
	}
	
	@Override
	public Object getServerElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
		return new ContainerTodoList(player, list);
	}

	@Override
	public Object getClientElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
		return new GuiTodoList.Block(this, player, list);
	}

}
