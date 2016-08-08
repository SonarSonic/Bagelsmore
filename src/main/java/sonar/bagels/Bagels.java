package sonar.bagels;

import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import sonar.bagels.items.BookshelfItem;
import sonar.bagels.items.Clipboard;
import sonar.bagels.items.DeskDrawerItem;
import sonar.bagels.items.DeskDrawerItem.SmeltingDrawerItem;
import sonar.bagels.items.DeskItem;
import sonar.bagels.network.BagelCommon;
import sonar.bagels.parts.Bookshelf;
import sonar.bagels.parts.Candle;
import sonar.bagels.parts.DeskCraftingPart;
import sonar.bagels.parts.DeskMultipart;
import sonar.bagels.parts.DrawerLarge;
import sonar.bagels.parts.DrawerSmall;
import sonar.bagels.parts.FluidDrawer;
import sonar.bagels.parts.Paper;
import sonar.bagels.parts.RecyclingDrawer;
import sonar.bagels.parts.SmeltingDrawer;
import sonar.bagels.parts.SwordMount;
import sonar.bagels.utils.DeskType;
import sonar.bagels.utils.TodoListRecipe;

@Mod(modid = Bagels.modid, name = "bagelsmore", version = Bagels.version, dependencies = "required-after:mcmultipart")
public class Bagels {
	// HORRORS LIE IN THIS SOURCE CODE, STAY BACK
	public static final String modid = "bagelsmore";
	public static final String version = "1.0.0";

	@Instance(modid)
	public static Bagels instance;

	@SidedProxy(clientSide = "sonar.bagels.network.BagelClient", serverSide = "sonar.bagels.network.BagelCommon")
	public static BagelCommon proxy;

	public static SimpleNetworkWrapper network;
	
	public CreativeTabs tab = new CreativeTabs(modid){

		@Override
		public Item getTabIconItem() {
			return deskFancy;
		}
		
	};

	public static Item deskFancy, deskStone, deskTreated, smallDrawer, largeDrawer, smeltingDrawer, recyclingDrawer, bookshelf, clipboard, clipboardEmpty, fluidDrawer;
	public static Block fancyPlanks, fancyStone, treatedPlanks;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		fancyPlanks = new Block(Material.WOOD).setUnlocalizedName("FancyPlanks").setRegistryName("FancyPlanks").setCreativeTab(tab);
		GameRegistry.registerBlock(fancyPlanks);
		treatedPlanks = new Block(Material.WOOD).setUnlocalizedName("TreatedPlanks").setRegistryName("TreatedPlanks").setCreativeTab(tab);
		GameRegistry.registerBlock(treatedPlanks);
		fancyStone = new Block(Material.WOOD).setUnlocalizedName("FancyStone").setRegistryName("FancyStone").setCreativeTab(tab);
		GameRegistry.registerBlock(fancyStone);
		deskFancy = new DeskItem(DeskType.FANCY).setUnlocalizedName("DeskFancy").setRegistryName("DeskFancy").setCreativeTab(tab);
		GameRegistry.register(deskFancy);
		deskStone = new DeskItem(DeskType.STONE).setUnlocalizedName("DeskStone").setRegistryName("DeskStone").setCreativeTab(tab);
		GameRegistry.register(deskStone);
		deskTreated = new DeskItem(DeskType.TREATED).setUnlocalizedName("DeskTreated").setRegistryName("DeskTreated").setCreativeTab(tab);
		GameRegistry.register(deskTreated);
		smallDrawer = new DeskDrawerItem.DrawerSmallItem().setUnlocalizedName("DrawerSmall").setRegistryName("DrawerSmall").setCreativeTab(tab);
		GameRegistry.register(smallDrawer);
		smeltingDrawer = new SmeltingDrawerItem().setUnlocalizedName("SmeltingDrawer").setRegistryName("SmeltingDrawer").setCreativeTab(tab);
		GameRegistry.register(smeltingDrawer);
		recyclingDrawer = new DeskDrawerItem.RecyclingDrawerItem().setUnlocalizedName("RecyclingDrawer").setRegistryName("RecyclingDrawer").setCreativeTab(tab);
		GameRegistry.register(recyclingDrawer);
		largeDrawer = new DeskDrawerItem.DrawerLargeItem().setUnlocalizedName("DrawerLarge").setRegistryName("DrawerLarge").setCreativeTab(tab);
		GameRegistry.register(largeDrawer);
		fluidDrawer = new DeskDrawerItem.FluidDrawerItem().setUnlocalizedName("FluidDrawer").setRegistryName("FluidDrawer").setCreativeTab(tab);
		GameRegistry.register(fluidDrawer);
		bookshelf = new BookshelfItem().setUnlocalizedName("Bookshelf").setRegistryName("Bookshelf").setCreativeTab(tab);
		GameRegistry.register(bookshelf);
		clipboard = new Clipboard().setMaxStackSize(1).setUnlocalizedName("Clipboard").setRegistryName("Clipboard").setCreativeTab(tab);
		GameRegistry.register(clipboard);
		clipboardEmpty = new Item().setMaxStackSize(1).setUnlocalizedName("ClipboardEmpty").setRegistryName("ClipboardEmpty").setCreativeTab(tab);
		GameRegistry.register(clipboardEmpty);

		MultipartRegistry.registerPart(DeskMultipart.Fancy.class, "bagelsmore:DeskFancy");
		MultipartRegistry.registerPart(DeskMultipart.Stone.class, "bagelsmore:DeskStone");
		MultipartRegistry.registerPart(DeskMultipart.Treated.class, "bagelsmore:DeskTreated");
		MultipartRegistry.registerPart(Paper.class, "bagelsmore:Paper");
		MultipartRegistry.registerPart(DrawerSmall.class, "bagelsmore:DrawerSmall");
		MultipartRegistry.registerPart(DrawerLarge.class, "bagelsmore:DrawerLarge");
		MultipartRegistry.registerPart(FluidDrawer.class, "bagelsmore:FluidDrawer");
		MultipartRegistry.registerPart(SmeltingDrawer.class, "bagelsmore:SmeltingDrawer");
		MultipartRegistry.registerPart(RecyclingDrawer.class, "bagelsmore:RecyclingDrawer");
		MultipartRegistry.registerPart(SwordMount.class, "bagelsmore:SwordMount");
		MultipartRegistry.registerPart(Bookshelf.class, "bagelsmore:Bookshelf");
		MultipartRegistry.registerPart(DeskCraftingPart.class, "bagelsmore:CraftingPart");
		MultipartRegistry.registerPart(Candle.class, "bagelsmore:Candle");
		proxy.registerRenderThings();
		proxy.registerPackets();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerSpecialRenderers();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
		RecipeSorter.register("bagelsmore:todolist", TodoListRecipe.class, Category.SHAPELESS, "after:forge:shapedore");
		GameRegistry.addRecipe(new TodoListRecipe(new ItemStack(clipboard), new Object[] { Items.PAPER, Bagels.clipboardEmpty }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancyPlanks, 16), new Object[] { "WPW", "P P", "WPW", 'P', "plankWood", 'W', "logWood" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(deskFancy, 1), new Object[] { "PPP", "P P", "   ", 'P', fancyPlanks }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(deskTreated, 1), new Object[] { "PPP", "P P", "   ", 'P', treatedPlanks }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(deskStone, 1), new Object[] { "PPP", "P P", "   ", 'P', fancyStone }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(smallDrawer, 1), new Object[] { "   ", "P N", "PPP", 'P', fancyPlanks, 'N', Items.GOLD_NUGGET }));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(largeDrawer, 1), new Object[] { smallDrawer, smallDrawer }));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(smallDrawer, 2), new Object[] { largeDrawer }));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(smeltingDrawer, 1), new Object[] { smallDrawer, Blocks.FURNACE }));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(recyclingDrawer, 1), new Object[] { smallDrawer, Blocks.OBSIDIAN }));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(fluidDrawer, 1), new Object[] { largeDrawer, Items.BUCKET }));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(clipboardEmpty, 1), new Object[] { fancyPlanks, Items.STICK }));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(fancyStone, 2), new Object[] { fancyPlanks, Blocks.STONE }));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(treatedPlanks, 2), new Object[] { fancyPlanks, "plankWood" }));

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent event) {

	}
}