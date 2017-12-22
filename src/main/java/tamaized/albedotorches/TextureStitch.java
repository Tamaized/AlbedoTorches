package tamaized.albedotorches;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = AlbedoTorches.MODID, value = Side.CLIENT)
public class TextureStitch {

	public static final ResourceLocation FLAME = new ResourceLocation(AlbedoTorches.MODID, "particle/flame");

	@SubscribeEvent
	public static void hook(TextureStitchEvent.Pre e) {
		e.getMap().registerSprite(FLAME);
	}

}
