package me.alexdevs.classicPeripherals.recipe;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModRecipes {
    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<NfcCardRecipe>> NFC_CARD_DYE = ClassicPeripherals.RECIPES
                    .register("nfc_card_dye", () -> new SimpleCraftingRecipeSerializer<>(NfcCardRecipe::new));

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<RfidBadgeRecipe>> RFID_BADGE_RECIPE = ClassicPeripherals.RECIPES
            .register("rfid_badge_dye", () -> new SimpleCraftingRecipeSerializer<>(RfidBadgeRecipe::new));

    public static void initialize() {}

}
