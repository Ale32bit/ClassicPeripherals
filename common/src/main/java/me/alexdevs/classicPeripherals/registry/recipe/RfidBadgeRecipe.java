package me.alexdevs.classicPeripherals.registry.recipe;

import dan200.computercraft.shared.util.ColourTracker;
import me.alexdevs.classicPeripherals.core.ItemDataHandler;
import me.alexdevs.classicPeripherals.registry.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RfidBadgeRecipe extends CustomRecipe {
    public RfidBadgeRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput container, Level level) {
        var hasItem = false;
        var tracker = new ColourTracker();

        for (int i = 0; i < container.size(); i++) {
            var stack = container.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.is(ModRegistry.Items.RFID_BADGE.get())) {
                hasItem = true;
            } else if (stack.getItem() instanceof DyeItem dye) {
                tracker.addColour(dye.getDyeColor());
            }
        }

        return hasItem && tracker.hasColour();
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput container, HolderLookup.Provider registries) {
        ItemStack item = null;
        var tracker = new ColourTracker();

        for (int i = 0; i < container.size(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(ModRegistry.Items.RFID_BADGE.get())) {
                    item = stack.copy();
                    item.setCount(1);
                    var tag = item.getComponents();
                    if (tag.has(ModRegistry.DataComponents.DATAHOLDER_COLOR.get())) {
                        int value = tag.getOrDefault(ModRegistry.DataComponents.DATAHOLDER_COLOR.get(), 0xFFFFFF);
                        var r = value >> 16 & 0xFF;
                        var g = value >> 8 & 0xFF;
                        var b = value & 0xFF;

                        tracker.addColour(r, g, b);
                    }
                } else if (stack.getItem() instanceof DyeItem dye) {
                    tracker.addColour(dye.getDyeColor());
                }
            }
        }

        if (item != null && tracker.hasColour()) {
            ItemDataHandler.setColor(item, tracker.getColour());
            return item;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRegistry.Recipes.RFID_BADGE_RECIPE.get();
    }
}
