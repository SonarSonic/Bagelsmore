package sonar.bagels.utils;

import mcmultipart.multipart.IMultipart;
import net.minecraft.util.EnumFacing;

public interface IDeskPart extends IMultipart {
	
	public EnumFacing getPartFacing();
	
	public boolean wasRemoved();
}
