package tamaized.albedotorches;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;
import net.minecraftforge.oredict.DyeUtils;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class AlbedoRecipe extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	private final Supplier<Ingredient> factory;
	private final IAlbedoBlock output;
	private Ingredient input;

	public AlbedoRecipe(Supplier<Ingredient> input, IAlbedoBlock output) {
		factory = input;
		this.output = output;
	}

	private void doCheck() {
		input = factory.get();
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
		doCheck();
		int i = 0;
		int j = 0;

		boolean flag = false;
		for (int k = 0; k < inv.getSizeInventory(); ++k) {
			ItemStack itemstack = inv.getStackInSlot(k);

			if (!itemstack.isEmpty()) {
				if (input.apply(itemstack)) {
					flag = itemstack.getItem() == output.asItem();
					++i;
				} else {
					if (!net.minecraftforge.oredict.DyeUtils.isDye(itemstack)) {
						return false;
					}

					++j;
				}

				if (j > 1 || i > 1) {
					return false;
				}
			}
		}

		return i == 1 && (j == 1 || flag);
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
		doCheck();
		ItemStack itemstack1 = ItemStack.EMPTY;

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack itemstack2 = inv.getStackInSlot(i);

			if (!itemstack2.isEmpty()) {
				if (net.minecraftforge.oredict.DyeUtils.isDye(itemstack2)) {
					itemstack1 = itemstack2;
				}
			}
		}

		Optional<EnumDyeColor> color = DyeUtils.colorFromStack(itemstack1);

		return color.isPresent() ? output.makeStack(output.asBlock().getDefaultState(), color.get()) : input.getMatchingStacks().length > 0 ? input.getMatchingStacks()[0].copy() : output.asStack(output.asBlock().getDefaultState());
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}

	@Nonnull
	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(Objects.requireNonNull(AlbedoTorches.albedoTorch));
	}
}
