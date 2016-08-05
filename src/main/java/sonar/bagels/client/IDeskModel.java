package sonar.bagels.client;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;

public interface IDeskModel {

    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand);
	
}
