package sonar.bagels.parts;

import net.minecraft.entity.player.EntityPlayer;

public interface IGuiPart {

	public int getHashedID();
	
	public Object getServerElement(EntityPlayer player);
	
	public Object getClientElement(EntityPlayer player);
}
