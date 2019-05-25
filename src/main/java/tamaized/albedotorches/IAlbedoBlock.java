package tamaized.albedotorches;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IAlbedoBlock {

	EnumDyeColor[] VALUES = EnumDyeColor.values();

	static EnumDyeColor getColorFromID(int id) {
		return VALUES[id % VALUES.length];
	}

	static int getColorIndexFromStack(ItemStack stack) {
		return stack.getOrCreateSubCompound(AlbedoTorches.MODID).getInteger("color");
	}

	default ItemStack makeStack(IBlockState state, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityAlbedo)
			return makeStack(state, (TileEntityAlbedo) tile);
		return makeStack(state, 0);
	}

	default ItemStack makeStack(IBlockState state, TileEntityAlbedo tile) {
		return makeStack(state, tile.getColor());
	}

	default ItemStack makeStack(IBlockState state, int color) {
		return makeStack(state, getColorFromID(color));
	}

	default ItemStack makeStack(IBlockState state, EnumDyeColor color) {
		ItemStack stack = asStack(state);
		stack.getOrCreateSubCompound(AlbedoTorches.MODID).setInteger("color", color.ordinal());
		return stack;
	}

	default Item asItem() {
		return Item.getItemFromBlock(asBlock());
	}

	Block asBlock();

	default ItemStack asStack(IBlockState state) {
		return new ItemStack(asBlock());
	}

	boolean canProvideLight(IBlockState state);

}
