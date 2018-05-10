package sonar.bagels;

import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import sonar.bagels.api.DeskType;
import sonar.bagels.api.DrawerPosition;
import sonar.bagels.api.EnumCandleSlot;
import sonar.bagels.common.blocks.BlockBookshelf;
import sonar.bagels.common.blocks.BlockCabinet;
import sonar.bagels.common.blocks.BlockCandle;
import sonar.bagels.common.blocks.BlockDesk;
import sonar.bagels.common.blocks.BlockEnderDrawer;
import sonar.bagels.common.blocks.BlockFluidDrawer;
import sonar.bagels.common.blocks.BlockLargeStorageDrawer;
import sonar.bagels.common.blocks.BlockPaper;
import sonar.bagels.common.blocks.BlockRecyclingDrawer;
import sonar.bagels.common.blocks.BlockSmallStorageDrawer;
import sonar.bagels.common.blocks.BlockSmeltingDrawer;
import sonar.bagels.common.blocks.BlockSwordMount;
import sonar.bagels.common.tileentity.TileBookshelf;
import sonar.bagels.common.tileentity.TileCabinet;
import sonar.bagels.common.tileentity.TileCandle;
import sonar.bagels.common.tileentity.TileDesk;
import sonar.bagels.common.tileentity.TileEnderDrawer;
import sonar.bagels.common.tileentity.TileFluidDrawer;
import sonar.bagels.common.tileentity.TilePaper;
import sonar.bagels.common.tileentity.TileRecyclingDrawer;
import sonar.bagels.common.tileentity.TileSmeltingDrawer;
import sonar.bagels.common.tileentity.TileStorageDrawer;
import sonar.bagels.common.tileentity.TileSwordMount;
import sonar.bagels.items.Clipboard;
import sonar.bagels.items.DeskDrawerItem;
import sonar.bagels.items.DeskItem;
import sonar.bagels.network.BagelCommon;
import sonar.bagels.utils.TodoListRecipe;
import sonar.core.SonarCrafting;
import sonar.core.SonarRegister;
import sonar.core.registries.SonarRegistryMultipart;

@Mod(modid = BagelsConstants.MODID, name = "bagelsmore", version = BagelsConstants.VERSION, acceptedMinecraftVersions = BagelsConstants.MC_VERSIONS, dependencies = "required-after:sonarcore@[" + BagelsConstants.SONAR_CORE + ",);" + "required-after:mcmultipart@[" + BagelsConstants.MCMULTIPART + ",);")
public class Bagels {

	@Instance(BagelsConstants.MODID)
	public static Bagels instance;

	@SidedProxy(clientSide = "sonar.bagels.network.BagelClient", serverSide = "sonar.bagels.network.BagelCommon")
	public static BagelCommon proxy;

	public static SimpleNetworkWrapper network;

	public static CreativeTabs tab = new CreativeTabs(BagelsConstants.MODID) {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(deskFancy);
		}
	};

	// decoration blocks
	public static Block fancyPlanks, fancyStone, treatedPlanks;
	// cabinets
	public static Block cabinetFancy, cabinetStone, cabinetTreated;
	// desks
	public static Block deskFancy, deskStone, deskTreated;
	// drawers
	public static Block smallDrawer, largeDrawer, smeltingDrawer, recyclingDrawer, enderDrawer, fluidDrawer;
	// desk decoration
	public static Block bookshelf, blockPaper, swordMount, candle;
	// items
	public static Item clipboard, clipboardEmpty;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// decoration blocks
		fancyPlanks = SonarRegister.addBlock(BagelsConstants.MODID, tab, "FancyPlanks", new Block(Material.WOOD).setHardness(0.4f));
		treatedPlanks = SonarRegister.addBlock(BagelsConstants.MODID, tab, "TreatedPlanks", new Block(Material.WOOD).setHardness(0.4f));
		fancyStone = SonarRegister.addBlock(BagelsConstants.MODID, tab, "FancyStone", new Block(Material.WOOD).setHardness(0.4f));

		// cabinets
		cabinetFancy = SonarRegister.addBlock(BagelsConstants.MODID, tab, new SonarRegistryMultipart(new BlockCabinet(DeskType.FANCY), "CabinetFancy").setProperties(0.4F, 20.0F));
		cabinetStone = SonarRegister.addBlock(BagelsConstants.MODID, tab, new SonarRegistryMultipart(new BlockCabinet(DeskType.STONE), "CabinetStone").setProperties(0.4F, 20.0F));
		cabinetTreated = SonarRegister.addBlock(BagelsConstants.MODID, tab, new SonarRegistryMultipart(new BlockCabinet(DeskType.TREATED), "CabinetTreated").setProperties(0.4F, 20.0F));
		
		// desks
		deskFancy = SonarRegister.addBlock(BagelsConstants.MODID, tab, new DeskItem.SonarRegistryDesk(new BlockDesk(DeskType.FANCY), "DeskFancy").setProperties(0.4F, 20.0F));
		deskStone = SonarRegister.addBlock(BagelsConstants.MODID, tab, new DeskItem.SonarRegistryDesk(new BlockDesk(DeskType.STONE), "DeskStone").setProperties(0.4F, 20.0F));
		deskTreated = SonarRegister.addBlock(BagelsConstants.MODID, tab, new DeskItem.SonarRegistryDesk(new BlockDesk(DeskType.TREATED), "DeskTreated").setProperties(0.4F, 20.0F));

		// drawers
		smallDrawer = SonarRegister.addBlock(BagelsConstants.MODID, tab, new DeskDrawerItem.SonarRegistryDrawer(new BlockSmallStorageDrawer(), "DrawerSmall", TileStorageDrawer.Small.class).setProperties(0.4F, 20.0F));
		largeDrawer = SonarRegister.addBlock(BagelsConstants.MODID, tab, new DeskDrawerItem.SonarRegistryDrawer(new BlockLargeStorageDrawer(), "DrawerLarge", TileStorageDrawer.Large.class).setProperties(0.4F, 20.0F));
		smeltingDrawer = SonarRegister.addBlock(BagelsConstants.MODID, tab, new DeskDrawerItem.SonarRegistryDrawer(new BlockSmeltingDrawer(), "SmeltingDrawer", TileSmeltingDrawer.class).setProperties(0.4F, 20.0F));
		recyclingDrawer = SonarRegister.addBlock(BagelsConstants.MODID, tab, new DeskDrawerItem.SonarRegistryDrawer(new BlockRecyclingDrawer(), "RecyclingDrawer", TileRecyclingDrawer.class).setProperties(0.4F, 20.0F));
		enderDrawer = SonarRegister.addBlock(BagelsConstants.MODID, tab, new DeskDrawerItem.SonarRegistryDrawer(new BlockEnderDrawer(), "EnderDrawer", TileEnderDrawer.class).setProperties(0.4F, 20.0F));
		fluidDrawer = SonarRegister.addBlock(BagelsConstants.MODID, tab, new DeskDrawerItem.SonarRegistryDrawer(new BlockFluidDrawer(), "FluidDrawer", TileFluidDrawer.class).setProperties(0.4F, 20.0F));

		// desk decoration
		bookshelf = SonarRegister.addBlock(BagelsConstants.MODID, tab, new SonarRegistryMultipart(new BlockBookshelf().setHardness(0.4f), "Bookshelf", TileBookshelf.class));
		blockPaper = SonarRegister.addBlock(BagelsConstants.MODID, tab, new SonarRegistryMultipart(new BlockPaper().setHardness(0.4f), "Paper", TilePaper.class));
		swordMount = SonarRegister.addBlock(BagelsConstants.MODID, tab, new SonarRegistryMultipart(new BlockSwordMount().setHardness(0.4f), "SwordMount", TileSwordMount.class));
		candle = SonarRegister.addBlock(BagelsConstants.MODID, tab, new SonarRegistryMultipart(new BlockCandle().setHardness(0.4f), "Candle", TileCandle.class));

		// items
		clipboard = SonarRegister.addItem(BagelsConstants.MODID, tab, "Clipboard", new Clipboard().setMaxStackSize(1));
		clipboardEmpty = SonarRegister.addItem(BagelsConstants.MODID, tab, "ClipboardEmpty", new Item().setMaxStackSize(1));

		// additional tile entities
		GameRegistry.registerTileEntity(TileDesk.LEFT.class, "AbstractDeskLeft");
		GameRegistry.registerTileEntity(TileDesk.MIDDLE.class, "AbstractDeskMiddle");
		GameRegistry.registerTileEntity(TileDesk.RIGHT.class, "AbstractDeskRight");
		GameRegistry.registerTileEntity(TileCabinet.class, "AbstractCabinet");

		// proxy trigger
		proxy.registerRenderThings();
		proxy.registerPackets();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerSpecialRenderers();

		// IMultipart Slots
		GameRegistry.findRegistry(IPartSlot.class).register(DrawerPosition.LARGE_BOTTOM);
		GameRegistry.findRegistry(IPartSlot.class).register(DrawerPosition.LARGE_TOP);
		GameRegistry.findRegistry(IPartSlot.class).register(DrawerPosition.SMALL_BOTTOM);
		GameRegistry.findRegistry(IPartSlot.class).register(DrawerPosition.SMALL_MIDDLE);
		GameRegistry.findRegistry(IPartSlot.class).register(DrawerPosition.SMALL_TOP);
		GameRegistry.findRegistry(IPartSlot.class).register(EnumCandleSlot.CANDLE_N);
		GameRegistry.findRegistry(IPartSlot.class).register(EnumCandleSlot.CANDLE_E);
		GameRegistry.findRegistry(IPartSlot.class).register(EnumCandleSlot.CANDLE_S);
		GameRegistry.findRegistry(IPartSlot.class).register(EnumCandleSlot.CANDLE_W);
		// shaped recipes
		SonarCrafting.addShapedOre(BagelsConstants.MODID, new ItemStack(fancyPlanks, 16), new Object[] { "WPW", "P P", "WPW", 'P', "plankWood", 'W', "logWood" });
		SonarCrafting.addShapedOre(BagelsConstants.MODID, new ItemStack(deskFancy, 1), new Object[] { "CPC", "   ", "   ", 'P', fancyPlanks, 'C', cabinetFancy });
		SonarCrafting.addShapedOre(BagelsConstants.MODID, new ItemStack(deskTreated, 1), new Object[] { "CPC", "   ", "   ", 'P', treatedPlanks, 'C', cabinetTreated });
		SonarCrafting.addShapedOre(BagelsConstants.MODID, new ItemStack(deskStone, 1), new Object[] { "CPC", "   ", "   ", 'P', fancyStone, 'C', cabinetStone });
		SonarCrafting.addShapedOre(BagelsConstants.MODID, new ItemStack(cabinetFancy, 1), new Object[] { "PPP", "P P", "PPP", 'P', fancyPlanks });
		SonarCrafting.addShapedOre(BagelsConstants.MODID, new ItemStack(cabinetTreated, 1), new Object[] { "PPP", "P P", "PPP", 'P', treatedPlanks });
		SonarCrafting.addShapedOre(BagelsConstants.MODID, new ItemStack(cabinetStone, 1), new Object[] { "PPP", "P P", "PPP", 'P', fancyStone });
		SonarCrafting.addShapedOre(BagelsConstants.MODID, new ItemStack(smallDrawer, 1), new Object[] { "   ", "P N", "PPP", 'P', fancyPlanks, 'N', Items.GOLD_NUGGET });
		SonarCrafting.addShapedOre(BagelsConstants.MODID, new ItemStack(bookshelf, 1), new Object[] { "PPP", "   ", "   ", 'P', fancyPlanks });

		// shapeless recipes
		SonarCrafting.addShapelessOre(BagelsConstants.MODID, new ItemStack(fancyStone, 2), new Object[] { fancyPlanks, Blocks.STONE });
		SonarCrafting.addShapelessOre(BagelsConstants.MODID, new ItemStack(treatedPlanks, 2), new Object[] { fancyPlanks, "plankWood" });

		SonarCrafting.addShapelessOre(BagelsConstants.MODID, new ItemStack(largeDrawer, 1), new Object[] { smallDrawer, smallDrawer });
		SonarCrafting.addShapelessOre(BagelsConstants.MODID, new ItemStack(smallDrawer, 2), new Object[] { largeDrawer });
		SonarCrafting.addShapelessOre(BagelsConstants.MODID, new ItemStack(smeltingDrawer, 1), new Object[] { smallDrawer, Blocks.FURNACE });
		SonarCrafting.addShapelessOre(BagelsConstants.MODID, new ItemStack(recyclingDrawer, 1), new Object[] { smallDrawer, Blocks.OBSIDIAN });
		SonarCrafting.addShapelessOre(BagelsConstants.MODID, new ItemStack(enderDrawer, 1), new Object[] { recyclingDrawer, Blocks.ENDER_CHEST });
		SonarCrafting.addShapelessOre(BagelsConstants.MODID, new ItemStack(fluidDrawer, 1), new Object[] { largeDrawer, Items.BUCKET });
		SonarCrafting.addShapelessOre(BagelsConstants.MODID, new ItemStack(clipboardEmpty, 1), new Object[] { fancyPlanks, Items.STICK });
		SonarCrafting.addShapelessOre(BagelsConstants.MODID, new ItemStack(candle, 1), new Object[] { new ItemStack(Items.COAL, 1, 1), "string", Items.CLAY_BALL});

		// custom recipes
		ResourceLocation clipBoardRecipe = SonarCrafting.getRecipeResourceLocation(BagelsConstants.MODID, new ItemStack(clipboard));
		SonarCrafting.registerForgeRecipe(clipBoardRecipe, new TodoListRecipe(clipBoardRecipe, new ItemStack(clipboard), new Object[] { Items.PAPER, Bagels.clipboardEmpty }));
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}

	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent event) {}
}