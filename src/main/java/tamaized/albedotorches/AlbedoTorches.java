package tamaized.albedotorches;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

@Mod.EventBusSubscriber
@Mod(modid = AlbedoTorches.MODID, version = AlbedoTorches.VERSION, acceptedMinecraftVersions = "[1.12,)", dependencies = "required-before-client:albedo@[" + AlbedoTorches.ALBEDO_VERSION + ",)")
public class AlbedoTorches {

	public static final String MODID = "albedotorches";
	public static final String VERSION = "${version}";
	public static final String ALBEDO_VERSION = "${albedoversion}";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	@GameRegistry.ObjectHolder(MODID + ":" + RegistryNames.ALBEDO_TORCH)
	public static final BlockAlbedoTorch albedoTorch = getNull();

	@GameRegistry.ObjectHolder(MODID + ":" + RegistryNames.ALBEDO_LAMP)
	public static final BlockAlbedoLamp albedoLamp = getNull();

	public static final CreativeTabs itemGroupAlbedo = new CreativeTabs("albedotorches") {
		@Nonnull
		@Override
		public ItemStack getTabIconItem() {
			return Objects.requireNonNull(albedoTorch).makeStack(albedoTorch.getDefaultState(), new Random().nextInt(0xFFFFFF));
		}
	};

	@SubscribeEvent
	public static void registerBlock(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(

				new BlockAlbedoTorch().
						setHardness(0F).
						setCreativeTab(itemGroupAlbedo).
						setUnlocalizedName(RegistryNames.ALBEDO_TORCH).
						setRegistryName(MODID, RegistryNames.ALBEDO_TORCH),

				new BlockAlbedoLamp().
						setHardness(0.3F).
						setCreativeTab(itemGroupAlbedo).
						setUnlocalizedName(RegistryNames.ALBEDO_LAMP).
						setRegistryName(MODID, RegistryNames.ALBEDO_LAMP)


		);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(

				makeItemBlock(albedoTorch),

				makeItemBlock(albedoLamp)

		);
	}

	private static Item makeItemBlock(Block block) {
		return new ItemBlock(Objects.requireNonNull(block)) {
			@Nonnull
			@Override
			public String getUnlocalizedName(ItemStack stack) {
				return super.getUnlocalizedName(stack) + "." + IAlbedoBlock.getColorFromID(IAlbedoBlock.getColorIndexFromStack(stack)).getUnlocalizedName();
			}
		}.
				setRegistryName(Objects.requireNonNull(block.getRegistryName()));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		event.getRegistry().registerAll(

				new AlbedoRecipe(() -> new OreIngredient("torch"), albedoTorch).setRegistryName(MODID, "recipe_albedo_torch"),

				new AlbedoRecipe(() -> new OreIngredient("lamp"), albedoLamp).setRegistryName(MODID, "recipe_albedo_lamp")

		);
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent e) {
		ModelLoader.setCustomModelResourceLocation(

				Item.getItemFromBlock(Objects.requireNonNull(albedoTorch)),

				0,

				new ModelResourceLocation(Objects.requireNonNull(albedoTorch.getRegistryName()), "normal")

		);
		ModelLoader.setCustomModelResourceLocation(

				Item.getItemFromBlock(Objects.requireNonNull(albedoLamp)),

				0,

				new ModelResourceLocation(Objects.requireNonNull(albedoLamp.getRegistryName()), "lit=false")

		);
		ModelLoader.setCustomModelResourceLocation(

				Item.getItemFromBlock(Objects.requireNonNull(albedoLamp)),

				1,

				new ModelResourceLocation(Objects.requireNonNull(albedoLamp.getRegistryName()), "lit=true")

		);
	}

	public static <T> T getNull() {
		return null;
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		GameRegistry.registerTileEntity(TileEntityAlbedo.class, new ResourceLocation(MODID, "tilealbedo"));
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		OreDictionary.registerOre("torch", albedoTorch);
		OreDictionary.registerOre("lamp", Blocks.REDSTONE_LAMP);
		OreDictionary.registerOre("lamp", Blocks.LIT_REDSTONE_LAMP);
		OreDictionary.registerOre("lamp", albedoLamp);
	}

	public static final class RegistryNames {
		public static final String ALBEDO_TORCH = "albedotorch";
		public static final String ALBEDO_LAMP = "albedolamp";
	}

}
