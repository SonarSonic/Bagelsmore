package sonar.bagels.parts;

import java.util.ArrayList;
import java.util.List;

import mcmultipart.MCMultiPartMod;
import mcmultipart.multipart.INormallyOccludingPart;
import mcmultipart.multipart.Multipart;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import sonar.bagels.utils.IDeskPart;

public abstract class BagelsMultipart extends Multipart implements INormallyOccludingPart, IDeskPart, IWorldNameable {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public boolean wasRemoved = false;
	public EnumFacing face;

	public BagelsMultipart() {
	}

	public BagelsMultipart(EnumFacing face) {
		this.face = face;
	}

	public EnumFacing getPartFacing() {
		return face;
	}

	public void onRemoved() {
		super.onRemoved();
		wasRemoved = true;
	}

	public boolean wasRemoved() {
		return wasRemoved;
	}

	public void defaultHarvest(EntityPlayer player, PartMOP hit) {
		super.harvest(player, hit);
	}

	@Override
	public List<ItemStack> getDrops() {
		ArrayList<ItemStack> drops = new ArrayList();
		if (shouldDropItem()) {
			ItemStack stack = this.createItemStack();
			if (stack != null) {
				drops.add(stack);
			}
		}
		return drops;

	}

	public boolean shouldDropItem() {
		return true;
	}

	@Override
	public ItemStack getPickBlock(EntityPlayer player, PartMOP hit) {
		return createItemStack();
	}

	public abstract ItemStack createItemStack();

	public void addSelectionBoxes(List<AxisAlignedBB> list) {
		this.addOcclusionBoxes(list);
	}

	@Override
	public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
		ArrayList<AxisAlignedBB> boxes = new ArrayList();
		addSelectionBoxes(boxes);
		boxes.forEach(box -> {
			if (box.intersectsWith(mask)) {
				list.add(box);
			}
		});
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("face", face.ordinal());
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		face = EnumFacing.VALUES[tag.getInteger("face")];
	}

	@Override
	public void writeUpdatePacket(PacketBuffer buf) {
		buf.writeInt(face.ordinal());
	}

	@Override
	public void readUpdatePacket(PacketBuffer buf) {
		face = EnumFacing.VALUES[buf.readInt()];
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(MCMultiPartMod.multipart, FACING);
	}

	@Override
	public IBlockState getActualState(IBlockState state) {
		return state.withProperty(FACING, face);
	}

	@Override
	public String getName() {
		return "Bagelsmore Part";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation("Bagelsmore Part");
	}

	public float getHardness(PartMOP hit) {

		return 0.3F;
	}

	public Material getMaterial() {

		return Material.WOOD;
	}

	public boolean isClient() {
		if (getWorld() == null) {
			return FMLCommonHandler.instance().getEffectiveSide().isClient();
		}
		return this.getWorld().isRemote;
	}

	public boolean isServer() {
		if (getWorld() == null) {
			return FMLCommonHandler.instance().getEffectiveSide().isServer();
		}
		return !this.getWorld().isRemote;
	}
}
