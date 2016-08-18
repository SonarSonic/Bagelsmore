package sonar.bagels.network;

import mcmultipart.client.multipart.MultipartRegistryClient;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sonar.bagels.Bagels;
import sonar.bagels.client.DeskCraftingRenderer;
import sonar.bagels.client.DrawerRenderer;
import sonar.bagels.client.SwordRenderer;
import sonar.bagels.parts.DeskCraftingPart;
import sonar.bagels.parts.DrawerLarge;
import sonar.bagels.parts.DrawerSmall;
import sonar.bagels.parts.EnderDrawer;
import sonar.bagels.parts.FluidDrawer;
import sonar.bagels.parts.RecyclingDrawer;
import sonar.bagels.parts.SmeltingDrawer;
import sonar.bagels.parts.SwordMount;

public class BagelClient extends BagelCommon {

	public void registerRenderThings() {
		registerBlock(Bagels.fancyPlanks);
		registerBlock(Bagels.fancyStone);
		registerBlock(Bagels.treatedPlanks);
		registerItem(Bagels.deskFancy);
		registerItem(Bagels.deskStone);
		registerItem(Bagels.deskTreated);
		registerItem(Bagels.largeDrawer);
		registerItem(Bagels.smallDrawer);
		registerItem(Bagels.smeltingDrawer);
		registerItem(Bagels.recyclingDrawer);
		registerItem(Bagels.bookshelf);
		registerItem(Bagels.clipboard);
		registerItem(Bagels.clipboardEmpty);
		registerItem(Bagels.fluidDrawer);
		registerItem(Bagels.enderDrawer);
		// ModelBakery.registerItemVariants(Bagels.smallDrawer, names);
		// ModelLoader.setCustomMeshDefinition(Bagels.smallDrawer,
		// ItemMeshDefinition);
	}

	public void registerSpecialRenderers() {
		MultipartRegistryClient.bindMultipartSpecialRenderer(DrawerSmall.class, new DrawerRenderer());
		MultipartRegistryClient.bindMultipartSpecialRenderer(DrawerLarge.class, new DrawerRenderer());
		MultipartRegistryClient.bindMultipartSpecialRenderer(SmeltingDrawer.class, new DrawerRenderer());
		MultipartRegistryClient.bindMultipartSpecialRenderer(RecyclingDrawer.class, new DrawerRenderer());
		MultipartRegistryClient.bindMultipartSpecialRenderer(FluidDrawer.class, new DrawerRenderer());
		MultipartRegistryClient.bindMultipartSpecialRenderer(EnderDrawer.class, new DrawerRenderer());

		MultipartRegistryClient.bindMultipartSpecialRenderer(SwordMount.class, new SwordRenderer());
		MultipartRegistryClient.bindMultipartSpecialRenderer(DeskCraftingPart.class, new DeskCraftingRenderer());
	}

	public static void registerBlock(Block block) {
		if (block != null) {
			Item item = Item.getItemFromBlock(block);
			if (item != null)
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Bagels.modid + ":" + item.getUnlocalizedName().substring(5), "inventory"));
		}
	}

	public static void registerItem(Item item) {

		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Bagels.modid + ":" + item.getUnlocalizedName().substring(5), "inventory"));
		// ModelLoader.setCustomModelResourceLocation(item, 0, new
		// ModelResourceLocation(new ResourceLocation(Bagels.modid,
		// item.getUnlocalizedName().substring(5)), "inventory"));
	}

	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
	}
}
