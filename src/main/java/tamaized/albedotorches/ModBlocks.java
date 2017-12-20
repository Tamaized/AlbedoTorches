package tamaized.albedotorches;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

@GameRegistry.ObjectHolder(AlbedoTorches.MODID)
@Mod.EventBusSubscriber(modid = AlbedoTorches.MODID)
public class ModBlocks {

	@GameRegistry.ObjectHolder("white_torch")
	public static final Block WHITE_TORCH = Blocks.AIR;
	@GameRegistry.ObjectHolder("orange_torch")
	public static final Block ORANGE_TORCH = Blocks.AIR;
	@GameRegistry.ObjectHolder("magenta_torch")
	public static final Block MAGENTA_TORCH = Blocks.AIR;
	@GameRegistry.ObjectHolder("light_blue_torch")
	public static final Block LIGHT_BLUE_TORCH = Blocks.AIR;
	@GameRegistry.ObjectHolder("yellow_torch")
	public static final Block YELLOW_TORCH = Blocks.AIR;
	@GameRegistry.ObjectHolder("lime_torch")
	public static final Block LIME_TORCH = Blocks.AIR;
	@GameRegistry.ObjectHolder("pink_torch")
	public static final Block PINK_TORCH = Blocks.AIR;
	@GameRegistry.ObjectHolder("gray_torch")
	public static final Block GRAY_TORCH = Blocks.AIR;
	@GameRegistry.ObjectHolder("silver_torch")
	public static final Block SILVER_TORCH = Blocks.AIR;
	@GameRegistry.ObjectHolder("cyan_torch")
	public static final Block CYAN_TORCH = Blocks.AIR;
	@GameRegistry.ObjectHolder("purple_torch")
	public static final Block PURPLE_TORCH = Blocks.AIR;
	@GameRegistry.ObjectHolder("blue_torch")
	public static final Block BLUE_TORCH = Blocks.AIR;
	@GameRegistry.ObjectHolder("brown_torch")
	public static final Block BROWN_TORCH = Blocks.AIR;
	@GameRegistry.ObjectHolder("green_torch")
	public static final Block GREEN_TORCH = Blocks.AIR;
	@GameRegistry.ObjectHolder("red_torch")
	public static final Block RED_TORCH = Blocks.AIR;
	@GameRegistry.ObjectHolder("black_torch")
	public static final Block BLACK_TORCH = Blocks.AIR;
	private static final List<ItemBlock> ITEMBLOCKS = Lists.newArrayList();

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> reg = event.getRegistry();
		for (EnumDyeColor color : EnumDyeColor.values())
			register(reg, new ColorTorchBlock(color).setRegistryName(color.getName() + "_torch").setUnlocalizedName(AlbedoTorches.MODID + "." + color.getName() + "_torch"));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> reg = event.getRegistry();
		for (Item item : ITEMBLOCKS)
			reg.register(item);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event) {
		for (ItemBlock item : ITEMBLOCKS)
			if (item.getRegistryName() != null)
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	private static void register(IForgeRegistry<Block> reg, Block b) {
		reg.register(b);
		if (b.getRegistryName() == null)
			return;
		ItemBlock item = new ItemBlock(b);
		item.setRegistryName(b.getRegistryName());
		ITEMBLOCKS.add(item);
	}

}
