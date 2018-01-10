package sonar.bagels.common.tileentity;

import javax.annotation.Nullable;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import sonar.bagels.api.DrawerType;
import sonar.core.api.IFlexibleGui;
import sonar.core.inventory.ISonarInventory;
import sonar.core.inventory.ISonarInventoryTile;
import sonar.core.inventory.SonarInventory;

public class TileSmeltingDrawer extends TileDrawer implements IFlexibleGui, ITickable, ISonarInventoryTile {

	private int furnaceBurnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;

	protected ISonarInventory inv;

	public TileSmeltingDrawer() {
		super();
		inv = new SonarInventory(this, 3);
		syncList.addParts(inv);

	}

	public ISonarInventory inv() {
		return inv;
	}

	@Override
	public DrawerType getDrawerType() {
		return DrawerType.SMALL;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking() && facing == EnumFacing.UP) {
			if (!this.getWorld().isRemote) {
				openFlexibleGui(player, 0);
			}
			return true;
		} else {
			return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
		}
	}

	@Override
	public Object getServerElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
		return new ContainerFurnace(player.inventory, this);
	}

	@Override
	public Object getClientElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
		return new GuiFurnace(player.inventory, this);
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
		return new TextComponentTranslation("tile.SmeltingDrawer.name");
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

		if (!this.world.isRemote) {
			ItemStack itemstack = getStackInSlot(1);

			if (this.isBurning() || !itemstack.isEmpty() && !((ItemStack) getStackInSlot(0)).isEmpty()) {
				if (!this.isBurning() && this.canSmelt()) {
					this.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(itemstack);
					this.currentItemBurnTime = this.furnaceBurnTime;

					if (this.isBurning()) {
						flag1 = true;

						if (!itemstack.isEmpty()) {
							Item item = itemstack.getItem();
							itemstack.shrink(1);

							if (itemstack.isEmpty()) {
								ItemStack item1 = item.getContainerItem(itemstack);
								this.setInventorySlotContents(1, item1);
							}
						}
					}
				}

				if (this.isBurning() && this.canSmelt()) {
					++this.cookTime;

					if (this.cookTime == this.totalCookTime) {
						this.cookTime = 0;
						this.totalCookTime = this.getCookTime(getStackInSlot(0));
						this.smeltItem();
						flag1 = true;
					}
				} else {
					this.cookTime = 0;
				}
			} else if (!this.isBurning() && this.cookTime > 0) {
				this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
			}

			if (flag != this.isBurning()) {
				this.markBlockForUpdate();
			}
		}

		if (flag1) {
			this.markDirty();
		}
	}

	public int getCookTime(@Nullable ItemStack stack) {
		return 200;
	}

	private boolean canSmelt() {
		if (((ItemStack) getStackInSlot(0)).isEmpty()) {
			return false;
		} else {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(getStackInSlot(0));
			if (itemstack.isEmpty()) {
				return false;
			} else {
				ItemStack itemstack1 = getStackInSlot(2);
				if (itemstack1.isEmpty()) {
					return true;
				} else if (!itemstack1.isItemEqual(itemstack)) {
					return false;
				} else if (itemstack1.getCount() + itemstack.getCount() <= getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
					return true;
				} else {
					return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize();
				}
			}
		}
	}

	/** Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack */
	public void smeltItem() {
		if (canSmelt()) {
			ItemStack itemstack = getStackInSlot(0);
			ItemStack itemstack1 = FurnaceRecipes.instance().getSmeltingResult(itemstack);
			ItemStack itemstack2 = getStackInSlot(2);

			if (itemstack2.isEmpty()) {
				setInventorySlotContents(2, itemstack1.copy());
			} else if (itemstack2.getItem() == itemstack1.getItem()) {
				itemstack2.grow(itemstack1.getCount());
			}

			if (itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && itemstack.getMetadata() == 1 && !((ItemStack) getStackInSlot(1)).isEmpty() && ((ItemStack) getStackInSlot(1)).getItem() == Items.BUCKET) {
				setInventorySlotContents(1, new ItemStack(Items.WATER_BUCKET));
			}

			itemstack.shrink(1);
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