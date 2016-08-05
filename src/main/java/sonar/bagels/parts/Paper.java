package sonar.bagels.parts;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
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
			boxes.add(new AxisAlignedBB(0.3, 1-(0.0625/2), 0.2, 1-0.2, 1, 1-0.3));
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

	@Override
	public ItemStack createItemStack() {
		return null;
	}

}
