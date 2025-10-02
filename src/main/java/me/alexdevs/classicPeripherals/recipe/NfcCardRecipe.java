package me.alexdevs.classicPeripherals.recipe;

import dan200.computercraft.shared.util.ColourTracker;
import me.alexdevs.classicPeripherals.item.ModItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class NfcCardRecipe extends CustomRecipe {
    public NfcCardRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        var hasItem = false;
        var tracker = new ColourTracker();

        for (int i = 0; i < container.getContainerSize(); i++) {
            var stack = container.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.is(ModItems.NFC_CARD)) {
                hasItem = true;
            } else if (stack.getItem() instanceof DyeItem dye) {
                tracker.addColour(dye.getDyeColor());
            }
        }

        return hasItem && tracker.hasColour();
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack item = null;
        var tracker = new ColourTracker();

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(ModItems.NFC_CARD)) {
                    item = stack.copy();
                    item.setCount(1);
                    if (item.hasTag()) {
                        var tag = item.getOrCreateTag();
                        if (tag.contains("color")) {
                            var value = tag.getInt("color");
                            var r = value >> 16 & 0xFF;
                            var g = value >> 8 & 0xFF;
                            var b = value & 0xFF;

                            tracker.addColour(r, g, b);
                        }
                    }
                } else if (stack.getItem() instanceof DyeItem dye) {
                    tracker.addColour(dye.getDyeColor());
                }
            }
        }

        if (item != null && tracker.hasColour()) {
            item.getOrCreateTag().putInt("color", tracker.getColour());
            return item;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.NFC_CARD_DYE;
    }
}
