package tamaized.albedotorches;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = AlbedoTorches.MODID, version = AlbedoTorches.VERSION, dependencies = "required-before-client:albedo@[" + AlbedoTorches.ALBEDO_VERSION + ",)")
public class AlbedoTorches {

	public static final String MODID = "albedotorches";
	public static final String VERSION = "${version}";
	public static final String ALBEDO_VERSION = "${albedoversion}";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		GameRegistry.registerTileEntity(TileEntityColorTorch.class, "TileEntityColorTorch");
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		for (Block torch : ModBlocks.getTorches())
			OreDictionary.registerOre("torch", torch);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

}
