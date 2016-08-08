package sonar.bagels.parts;

import javax.annotation.Nullable;

import mcmultipart.MCMultiPartMod;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import sonar.bagels.Bagels;
import sonar.bagels.utils.DrawerPosition;
import sonar.bagels.utils.DrawerType;
import sonar.bagels.utils.IGuiPart;
import sonar.bagels.utils.IInventorySync;

public class SmeltingDrawer extends DeskDrawer implements IGuiPart, ITickable, IInventorySync {

	public static final PropertyBool active = PropertyBool.create("on");

	private int furnaceBurnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;

	public SmeltingDrawer() {
		super();
	}

	public SmeltingDrawer(DrawerPosition position, EnumFacing face) {
		super(position, face);
	}

	@Override
	public DrawerType getDrawerType() {
		return DrawerType.SMALL;
	}

	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
		if (!player.isSneaking() && hit.sideHit == EnumFacing.UP) {
			if (!this.getWorld().isRemote)
				player.openGui(Bagels.instance, this.getHashedID(), this.getWorld(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
			return true;
		} else {
			return super.onActivated(player, hand, heldItem, hit);
		}
	}

	@Override
	public int getHashedID() {
		return -this.getDrawerPosition().ordinal();
	}

	@Override
	public Object getServerElement(EntityPlayer player) {
		return new ContainerFurnace(player.inventory, inv);
	}

	@Override
	public Object getClientElement(EntityPlayer player) {
		return new GuiFurnace(player.inventory, inv);
	}

	@Override
	public int getInvSize() {
		return 3;
	}

	@Override
	public ItemStack createItemStack() {
		return new ItemStack(Bagels.smeltingDrawer, 1);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(MCMultiPartMod.multipart, FACING, active);
	}

	@Override
	public IBlockState getActualState(IBlockState state) {
		return state.withProperty(FACING, face).withProperty(active, this.isBurning());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("BurnTime", this.furnaceBurnTime);
		tag.setInteger("CookTime", this.cookTime);
		tag.setInteger("CookTimeTotal", this.totalCookTime);
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.furnaceBurnTime = tag.getInteger("BurnTime");
		this.cookTime = tag.getInteger("CookTime");
		this.totalCookTime = tag.getInteger("CookTimeTotal");
		this.currentItemBurnTime = TileEntityFurnace.getItemBurnTime(this.inv.getStackInSlot(1));

	}

	// here we see some magical copy and paste skills, flawless
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation("item.SmeltingDrawer.name");
	}

	public boolean isBurning() {
		return this.furnaceBurnTime > 0;
	}

	public void update() {
		boolean flag = this.isBurning();
		boolean flag1 = false;

		if (this.isBurning()) {
			--this.furnaceBurnTime;
		}

		if (!this.getWorld().isRemote) {
			if (this.isBurning() || getStacks()[1] != null && getStacks()[0] != null) {
				if (!this.isBurning() && this.canSmelt()) {
					this.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(getStacks()[1]);
					this.currentItemBurnTime = this.furnaceBurnTime;

					if (this.isBurning()) {
						flag1 = true;

						if (getStacks()[1] != null) {
							--getStacks()[1].stackSize;

							if (getStacks()[1].stackSize == 0) {
								getStacks()[1] = getStacks()[1].getItem().getContainerItem(getStacks()[1]);
							}
						}
					}
				}

				if (this.isBurning() && this.canSmelt()) {
					++this.cookTime;

					if (this.cookTime >= this.totalCookTime) {
						this.cookTime = 0;
						this.totalCookTime = this.getCookTime(getStacks()[0]);
						this.smeltItem();
						flag1 = true;
					}
				} else {
					this.cookTime = 0;
				}
			} else if (!this.isBurning() && this.cookTime > 0) {
				this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.totalCookTime);
			}
		}
		if (flag != isBurning()) {
			this.sendUpdatePacket();
		}

		if (flag1) {
			this.markDirty();
		}
	}

	public int getCookTime(@Nullable ItemStack stack) {
		return 200;
	}

	private boolean canSmelt() {
		if (getStacks()[0] == null) {
			return false;
		} else {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(getStacks()[0]);
			if (itemstack == null)
				return false;
			if (getStacks()[2] == null)
				return true;
			if (!getStacks()[2].isItemEqual(itemstack))
				return false;
			int result = getStacks()[2].stackSize + itemstack.stackSize;
			return result <= inv.getInventoryStackLimit() && result <= getStacks()[2].getMaxStackSize();
		}
	}

	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(getStacks()[0]);

			if (getStacks()[2] == null) {
				getStacks()[2] = itemstack.copy();
			} else if (getStacks()[2].getItem() == itemstack.getItem()) {
				getStacks()[2].stackSize += itemstack.stackSize;
			}

			if (getStacks()[0].getItem() == Item.getItemFromBlock(Blocks.SPONGE) && getStacks()[0].getMetadata() == 1 && getStacks()[1] != null && getStacks()[1].getItem() == Items.BUCKET) {
				getStacks()[1] = new ItemStack(Items.WATER_BUCKET);
			}

			--getStacks()[0].stackSize;

			if (getStacks()[0].stackSize <= 0) {
				getStacks()[0] = null;
			}
		}
	}

	public int getField(int id) {
		switch (id) {
		case 0:
			return this.furnaceBurnTime;
		case 1:
			return this.currentItemBurnTime;
		case 2:
			return this.cookTime;
		case 3:
			return this.totalCookTime;
		default:
			return 0;
		}
	}

	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.furnaceBurnTime = value;
			break;
		case 1:
			this.currentItemBurnTime = value;
			break;
		case 2:
			this.cookTime = value;
			break;
		case 3:
			this.totalCookTime = value;
		}
	}

	public int getFieldCount() {
		return 4;
	}
}
