package tamaized.albedotorches;

import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class TileEntityColorTorch extends TileEntity implements ILightProvider {

	public TileEntityColorTorch() {

	}

	@Override
	public Light provideLight() {
		Block b = world.getBlockState(getPos()).getBlock();
		return Light.builder().pos(getPos()).color(b instanceof ColorTorchBlock ? ((ColorTorchBlock) b).color.getColorValue() : 0xFFFF00, false).radius(15).build();
	}
}
