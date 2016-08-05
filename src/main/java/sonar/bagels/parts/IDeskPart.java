package sonar.bagels.parts;

import mcmultipart.multipart.IMultipart;
import net.minecraft.util.EnumFacing;

public interface IDeskPart extends IMultipart {
	
	public EnumFacing getPartFacing();
}
