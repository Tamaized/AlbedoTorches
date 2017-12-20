package tamaized.albedotorches;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.world.World;

public class ParticleFlameColor extends ParticleFlame {

	public ParticleFlameColor(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int color) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		setParticleTexture(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TextureStitch.FLAME.toString()));
		particleRed = ((color & 0xFF0000) >> 16) / 255F;
		particleGreen = ((color & 0xFF00) >> 8) / 255F;
		particleBlue = (color & 0xFF) / 255F;
	}

	@Override
	public int getFXLayer() {
		return 1;
	}

	@Override
	public int getBrightnessForRender(float p_189214_1_) {
		return 0xF000F0;
	}

	@Override
	public void setParticleTextureIndex(int particleTextureIndex) {
		// NO-OP
	}
}
