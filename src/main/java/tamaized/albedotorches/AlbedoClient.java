package tamaized.albedotorches;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = AlbedoTorches.MODID, value = Side.CLIENT)
public class AlbedoClient {

	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block event) {
		event.getBlockColors().registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			EnumDyeColor color = IAlbedoBlock.VALUES[0];
			if (worldIn != null && pos != null) {
				TileEntity tile = worldIn.getTileEntity(pos);
				if (tile instanceof TileEntityAlbedo)
					color = IAlbedoBlock.getColorFromID(((TileEntityAlbedo) tile).getColor());
			}
			return color.getColorValue();
		}, AlbedoTorches.albedoTorch, AlbedoTorches.albedoLamp);
	}

	@SubscribeEvent
	public static void registerItemColors(ColorHandlerEvent.Item event) {
		event.getItemColors().registerItemColorHandler(

				(stack, tintIndex) -> IAlbedoBlock.getColorFromID(IAlbedoBlock.getColorIndexFromStack(stack)).getColorValue(),

				AlbedoTorches.albedoTorch, AlbedoTorches.albedoLamp

		);
	}

}
