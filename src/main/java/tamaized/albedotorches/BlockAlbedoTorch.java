package tamaized.albedotorches;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockAlbedoTorch extends BlockTorch implements IAlbedoBlock {

	public BlockAlbedoTorch() {
		setSoundType(SoundType.WOOD);
	}

	@SideOnly(Side.CLIENT)
	private static void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz, int color) {
		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleFlameColor(world, x, y, z, vx, vy, vz, color));
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return FMLCommonHandler.instance().getEffectiveSide().isServer() ? 14 : super.getLightValue(state, world, pos);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (world.isRemote && world.getLight(pos) == 14)
			world.checkLightFor(EnumSkyBlock.BLOCK, pos);
		TileEntity tile = world.getTileEntity(pos);
		if (!(tile instanceof TileEntityAlbedo))
			return;
		EnumFacing enumfacing = state.getValue(FACING);
		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 0.7D;
		double d2 = (double) pos.getZ() + 0.5D;
		int color = IAlbedoBlock.getColorFromID(((TileEntityAlbedo) tile).getColor()).getColorValue();

		if (enumfacing.getAxis().isHorizontal()) {
			EnumFacing enumfacing1 = enumfacing.getOpposite();
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.27D * (double) enumfacing1.getFrontOffsetX(), d1 + 0.22D, d2 + 0.27D * (double) enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D);
			spawnParticle(world, d0 + 0.27D * (double) enumfacing1.getFrontOffsetX(), d1 + 0.22D, d2 + 0.27D * (double) enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, color);
		} else {
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
			spawnParticle(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, color);
		}
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityAlbedo();
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, @Nullable EntityLivingBase entity, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityAlbedo)
			((TileEntityAlbedo) tile).setColor(IAlbedoBlock.getColorIndexFromStack(stack));
	}

	@Nonnull
	@Override
	public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @SuppressWarnings("NullableProblems") @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
		return makeStack(state, world, pos);
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
		if (world instanceof World)
			drops.add(getPickBlock(state, null, (World) world, pos, null));
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (EnumDyeColor color : VALUES)
			items.add(makeStack(getDefaultState(), color));
	}

	@Override
	public Block asBlock() {
		return this;
	}

	@Override
	public boolean canProvideLight(IBlockState state) {
		return true;
	}
}