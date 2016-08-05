package sonar.bagels.network;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import sonar.bagels.Bagels;

public class BagelClient extends BagelCommon {

	public void registerRenderThings() {
		registerItem(Bagels.desk);
	}

	public static void registerBlock(Block block) {
		if (block != null) {
			Item item = Item.getItemFromBlock(block);
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Bagels.modid + ":" + item.getUnlocalizedName().substring(5), "inventory"));
		}
	}
	public static void registerItem(Item item){
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(Bagels.modid, item.getUnlocalizedName().substring(5)), "inventory"));
	}
}
