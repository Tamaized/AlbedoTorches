package tamaized.albedotorches;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockAlbedoLamp extends Block implements IAlbedoBlock {

	public static IProperty<Boolean> LIT = PropertyBool.create("lit");

	public BlockAlbedoLamp() {
		super(Material.CIRCUITS);
		setSoundType(SoundType.GLASS);
		setDefaultState(getDefaultState().withProperty(LIT, false));
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, LIT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(LIT) ? 1 : 0);
	}

	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(LIT, (meta & 0b1) == 1);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			if (state.getValue(LIT) && !worldIn.isBlockPowered(pos))
				worldIn.setBlockState(pos, state.withProperty(LIT, false), 2);
			else if (!state.getValue(LIT) && worldIn.isBlockPowered(pos))
				worldIn.setBlockState(pos, state.withProperty(LIT, true), 2);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!worldIn.isRemote) {
			if (state.getValue(LIT) && !worldIn.isBlockPowered(pos)) {
				worldIn.scheduleUpdate(pos, this, 4);
			} else if (!state.getValue(LIT) && worldIn.isBlockPowered(pos)) {
				worldIn.setBlockState(pos, state.withProperty(LIT, true), 2);
			}
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			if (state.getValue(LIT) && !worldIn.isBlockPowered(pos)) {
				worldIn.setBlockState(pos, state.withProperty(LIT, false), 2);
			}
		}
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(LIT) && FMLCommonHandler.instance().getEffectiveSide().isServer() ? 15 : super.getLightValue(state, world, pos);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (state.getValue(LIT) && world.isRemote && world.getLight(pos) == 15)
			world.checkLightFor(EnumSkyBlock.BLOCK, pos);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
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
		drops.add(getPickBlock(state, null, null, pos, null));
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (EnumDyeColor color : VALUES)
			items.add(makeStack(getDefaultState().withProperty(LIT, false), color));
	}

	@Override
	public Block asBlock() {
		return this;
	}

	@Override
	public boolean canProvideLight(IBlockState state) {
		return state.getValue(LIT);
	}
}