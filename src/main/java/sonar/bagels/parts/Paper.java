package sonar.bagels.parts;

import java.util.ArrayList;
import java.util.List;

import mcmultipart.MCMultiPartMod;
import mcmultipart.multipart.INormallyOccludingPart;
import mcmultipart.multipart.Multipart;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;

public class Paper extends SidedMultipart {

	public Paper() {
		super();
	}

	public Paper(EnumFacing face) {
		super(face);
	}

	@Override
	public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
		switch(face){
		case EAST:
			break;
		case NORTH:
			boxes.add(new AxisAlignedBB(0.2, 1-(0.0625/2), 0.2, 1-0.3, 1, 1-0.3));
			break;
		case SOUTH:
			boxes.add(new AxisAlignedBB(0.3, 1-(0.0625/2), 0.3, 1-0.2, 1, 1-0.2));
			break;
		case WEST:
			boxes.add(new AxisAlignedBB(1-0.3, 1-(0.0625/2), 1-0.2, 0.2, 1, 0.3));
			break;
		default:
			break;
		
		}
	}

}
