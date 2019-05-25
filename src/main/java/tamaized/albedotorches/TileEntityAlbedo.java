package tamaized.albedotorches;

import elucent.albedo.Albedo;
import elucent.albedo.event.GatherLightsEvent;
import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityAlbedo extends TileEntity {
	private int colorIndex;
	private ILightProvider cap = new ILightProvider() {
		@Override
		public void gatherLights(GatherLightsEvent gatherLightsEvent, Entity entity) {
			gatherLightsEvent.add(

					Light.builder().
							pos(getPos()).
							color(IAlbedoBlock.getColorFromID(colorIndex).getColorValue(), false).
							radius(14).
							build()

			);
		}
	};

	private static boolean canProvideLight(IBlockState state) {
		return state.getBlock() instanceof IAlbedoBlock && ((IAlbedoBlock) state.getBlock()).canProvideLight(state);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	public int getColor() {
		return colorIndex;
	}

	public void setColor(int index) {
		colorIndex = index;
		if (world == null || world.isRemote)
			return;
		IBlockState state = world.getBlockState(getPos());
		world.notifyBlockUpdate(getPos(), state, state, 3);
	}

	@Nonnull
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		setColor(compound.getInteger("color"));
	}

	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("color", colorIndex);
		return super.writeToNBT(compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		colorIndex = pkt.getNbtCompound().getInteger("color");
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("color", colorIndex);
		return new SPacketUpdateTileEntity(getPos(), 1, nbt);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return super.hasCapability(capability, facing) || (capability == Albedo.LIGHT_PROVIDER_CAPABILITY && canProvideLight(world.getBlockState(pos)));
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == Albedo.LIGHT_PROVIDER_CAPABILITY && canProvideLight(world.getBlockState(pos)) ? Albedo.LIGHT_PROVIDER_CAPABILITY.cast(cap) : super.getCapability(capability, facing);
	}
}
