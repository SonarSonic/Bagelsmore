package sonar.bagels.client.gui;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import sonar.bagels.Bagels;
import sonar.bagels.BagelsConstants;
import sonar.bagels.common.containers.ContainerTodoList;
import sonar.bagels.common.tileentity.TilePaper;
import sonar.bagels.network.PacketClipboard;
import sonar.bagels.network.PacketToDoList;
import sonar.bagels.utils.TodoList;
import sonar.core.client.gui.GuiSonar;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public abstract class GuiTodoList extends GuiSonar {
	private static final ResourceLocation background = new ResourceLocation(BagelsConstants.MODID + ":textures/gui/todo_list.png");
	public final TodoList paper;
	private GuiTextField listName;
	private ArrayList<GuiTextField> targets = new ArrayList();

	public static class Block extends GuiTodoList {
		public TilePaper multipart;

		public Block(TilePaper tilePaper, EntityPlayer player, TodoList inv) {
			super(player, inv);
			this.multipart = tilePaper;
		}

		@Override
		public void sendUpdatePacket() {
			Bagels.network.sendToServer(new PacketToDoList(multipart));
		}
	}

	public static class Item extends GuiTodoList {

		public Item(EntityPlayer player, TodoList inv) {
			super(player, inv);
		}

		@Override
		public void sendUpdatePacket() {
			Bagels.network.sendToServer(new PacketClipboard(paper));
		}

	}

	public GuiTodoList(EntityPlayer player, TodoList inv) {
		super(new ContainerTodoList(player, inv));
		this.paper = inv;
		this.xSize = 120;
		this.ySize = 180;

	}

	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		listName = new GuiTextField(0, this.fontRenderer, 20, 13, 90, 12);
		listName.setMaxStringLength(20);
		listName.setText(paper.listName == null ? "Todo List" : paper.listName);
		listName.setEnableBackgroundDrawing(false);
		listName.setTextColor(Color.DARK_GRAY.getRGB());
		targets = new ArrayList();
		for (int i = 0; i < paper.entries.length; i++) {
			GuiTextField target = new GuiTextField(i+1, this.fontRenderer, 14, 5 + (i * 10) + 26, 90, 10);
			target.setMaxStringLength(20);
			target.setText(paper.entries[i] == null ? "" : paper.entries[i]);
			target.setEnableBackgroundDrawing(false);
			targets.add(target);
		}

	}

	@Override
	public void mouseClicked(int i, int j, int k) {
		listName.mouseClicked(i - guiLeft, j - guiTop, k);
		targets.forEach(field -> field.mouseClicked(i - guiLeft, j - guiTop, k));
	}

	@Override
	protected void keyTyped(char c, int i) throws IOException {
		if (listName.isFocused()) {
			if (c == 13 || c == 27) {
				listName.setFocused(false);
			} else {
				listName.textboxKeyTyped(c, i);
			}
			return;
		}
		for (GuiTextField field : targets) {
			if (field.isFocused()) {
				if (c == 13 || c == 27) {
					field.setFocused(false);
				} else {
					field.textboxKeyTyped(c, i);
				}
				return;
			}
		}
		super.keyTyped(c, i);
	}

	public void setListValues() {
		String newListName = listName.getText();
		paper.listName = (newListName == null || newListName.isEmpty() || newListName.equals("")) ? "Todo List" : newListName;
		int pos = 0;
		for (GuiTextField field : targets) {
			String newEntry = field.getText();
			paper.entries[pos] = (newEntry == null || newEntry.isEmpty() || newEntry.equals("")) ? "" : newEntry;
			pos++;
		}
	}

	public void onGuiClosed() {
		setListValues();
		sendUpdatePacket();
		super.onGuiClosed();
	}

	public abstract void sendUpdatePacket();

	public void reset(boolean shouldAddLists) {
		this.buttonList.clear();
		initGui();
		if (shouldAddLists) {
			setListValues();
		}
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// GL11.glPushMatrix();
		fontRenderer.drawString(listName.getText(), xSize / 2 - fontRenderer.getStringWidth(listName.getText()) / 2, listName.y, 0);
		// GL11.glPopMatrix();

		for (int i = 0; i < paper.entries.length; i++) {
			GuiTextField field = i < targets.size() ? targets.get(i) : null;
			String entry = String.format("%s. %s", i + 1, field != null ? field.getText() : "");
			fontRenderer.drawString(entry, 14, 5 + (i * 10) + 26, 0);
		}
	}

	@Override
	public ResourceLocation getBackground() {
		return background;
	}
}