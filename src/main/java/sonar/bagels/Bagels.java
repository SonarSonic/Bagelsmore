package sonar.bagels;

import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import sonar.bagels.items.DeskDrawerItem;
import sonar.bagels.items.DeskItem;
import sonar.bagels.network.BagelCommon;
import sonar.bagels.parts.DeskMultipart;
import sonar.bagels.parts.DrawerLarge;
import sonar.bagels.parts.DrawerSmall;
import sonar.bagels.parts.Paper;
import sonar.bagels.parts.SwordMount;

@Mod(modid = Bagels.modid, name = "bagelsmore", version = Bagels.version)
public class Bagels {
	//HORRORS LIE IN THIS SOURCE CODE, STAY BACK
	public static final String modid = "bagelsmore";
	public static final String version = "1.0.0";

	@Instance(modid)
	public static Bagels instance;

	@SidedProxy(clientSide = "sonar.bagels.network.BagelClient", serverSide = "sonar.bagels.network.BagelCommon")
	public static BagelCommon proxy;
	
	public static Item desk, smallDrawer, largeDrawer;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		desk = new DeskItem().setUnlocalizedName("Desk").setRegistryName("Desk").setCreativeTab(CreativeTabs.MISC);		
		GameRegistry.register(desk);
		largeDrawer = new DeskDrawerItem.DrawerLargeItem().setUnlocalizedName("DrawerLarge").setRegistryName("DrawerLarge").setCreativeTab(CreativeTabs.MISC);	
		GameRegistry.register(largeDrawer);
		smallDrawer = new DeskDrawerItem.DrawerSmallItem().setUnlocalizedName("DrawerSmall").setRegistryName("DrawerSmall").setCreativeTab(CreativeTabs.MISC);	
		GameRegistry.register(smallDrawer);		
		
		MultipartRegistry.registerPart(DeskMultipart.class, "bagelsmore:Desk");
		MultipartRegistry.registerPart(Paper.class, "bagelsmore:Paper");
		MultipartRegistry.registerPart(DrawerSmall.class, "bagelsmore:DrawerSmall");
		MultipartRegistry.registerPart(DrawerLarge.class, "bagelsmore:DrawerLarge");
		MultipartRegistry.registerPart(SwordMount.class, "bagelsmore:SwordMount");
		proxy.registerRenderThings();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerSpecialRenderers();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent event) {

	}
}