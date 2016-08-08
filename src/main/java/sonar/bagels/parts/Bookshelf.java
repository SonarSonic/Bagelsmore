package sonar.bagels.parts;

import java.util.List;

import mcmultipart.MCMultiPartMod;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import sonar.bagels.Bagels;
import sonar.bagels.client.gui.GuiBookshelf;
import sonar.bagels.common.containers.ContainerBookshelf;
import sonar.bagels.utils.IGuiPart;

public class Bookshelf extends InventoryMultipart implements IGuiPart {

	public static final PropertyBool book1 = PropertyBool.create("book1");
	public static final PropertyBool book2 = PropertyBool.create("book2");
	public static final PropertyBool book3 = PropertyBool.create("book3");
	public static final PropertyBool book4 = PropertyBool.create("book4");
	public static final PropertyBool book5 = PropertyBool.create("book5");
	public static final PropertyBool book6 = PropertyBool.create("book6");
	public static final PropertyBool book7 = PropertyBool.create("book7");

	public int bookCount = 0;

	public Bookshelf() {
		super();
	}

	public Bookshelf(EnumFacing face) {
		super(face);
	}

	public static boolean isBook(ItemStack stack) {
		if (stack != null && stack.getItem() != null) {
			if (stack.getItem() instanceof ItemBook) {
				return true;
			} else if (stack.getItem() == Items.BOOK || stack.getItem() == Items.WRITABLE_BOOK || stack.getItem() == Items.WRITTEN_BOOK || stack.getItem() == Items.ENCHANTED_BOOK) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
		double p = 0.0625;
		double minY = 1 - p / 2;
		double maxY = 1 + p * 5 + p / 2;
		switch (face) {
		case EAST:
			boxes.add(new AxisAlignedBB(p * 5 + p / 2, minY, p * 3, p / 2, maxY, p * 13));
			break;
		case NORTH:
			boxes.add(new AxisAlignedBB(p * 3, minY, 1 - p * 5 + p / 2, p * 13, maxY, 1 - p * 1 + p / 2));
			break;
		case SOUTH:
			boxes.add(new AxisAlignedBB(p * 3, minY, p * 5 + p / 2, p * 15, maxY, p * 1 + p / 2));
			break;
		case WEST:
			boxes.add(new AxisAlignedBB(1 - p * 5 + p / 2, minY, p * 3, 1 - p * 1 + p / 2, maxY, p * 13));
			break;
		default:
			break;

		}
	}

	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
		if (!this.getWorld().isRemote)
			player.openGui(Bagels.instance, this.getHashedID(), this.getWorld(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
		return true;
	}

	@Override
	public int getInvSize() {
		return 7;
	}

	public void markPartDirty() {
		int newBookCount = 0;
		for (int i = 0; i < getInvSize(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (isBook(inv.getStackInSlot(i))) {
				newBookCount++;
			}
		}
		this.bookCount = newBookCount;
		super.markPartDirty();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("bookCount", bookCount);
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		bookCount = tag.getInteger("bookCount");
	}

	@Override
	public void writeUpdatePacket(PacketBuffer buf) {
		super.writeUpdatePacket(buf);
		buf.writeInt(bookCount);
	}

	@Override
	public void readUpdatePacket(PacketBuffer buf) {
		super.readUpdatePacket(buf);
		bookCount = buf.readInt();
	}

	public ItemStack createItemStack() {
		return new ItemStack(Bagels.bookshelf, 1);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(MCMultiPartMod.multipart, FACING, book1, book2, book3, book4, book5, book6, book7);
	}

	@Override
	public IBlockState getActualState(IBlockState state) {
		return state.withProperty(FACING, face).withProperty(book1, bookCount > 0).withProperty(book2, bookCount > 1).withProperty(book3, bookCount > 2).withProperty(book4, bookCount > 3).withProperty(book5, bookCount > 4).withProperty(book6, bookCount > 5).withProperty(book7, bookCount > 6);
	}

	@Override
	public int getHashedID() {
		return -1000;
	}

	@Override
	public Object getServerElement(EntityPlayer player) {
		return new ContainerBookshelf(player.inventory, this);
	}

	@Override
	public Object getClientElement(EntityPlayer player) {
		return new GuiBookshelf(this);
	}
}
