package me.alexdevs.classicPeripherals.recipe;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.platform.Registrar;
import me.alexdevs.classicPeripherals.platform.RegistrySupplier;
import me.alexdevs.classicPeripherals.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class ModRecipes {
    private static final Registrar<RecipeSerializer<?>> RECIPES = Services.REGISTRATION.create(Registries.RECIPE_SERIALIZER, ClassicPeripherals.MOD_ID);

    @SuppressWarnings("unchecked")
    public static final RegistrySupplier<SimpleCraftingRecipeSerializer<NfcCardRecipe>> NFC_CARD_DYE =
            (RegistrySupplier<SimpleCraftingRecipeSerializer<NfcCardRecipe>>) (RegistrySupplier<?>) RECIPES.register("nfc_card_dye",
                    () -> new SimpleCraftingRecipeSerializer<>(NfcCardRecipe::new));

    @SuppressWarnings("unchecked")
    public static final RegistrySupplier<SimpleCraftingRecipeSerializer<RfidBadgeRecipe>> RFID_BADGE_RECIPE =
            (RegistrySupplier<SimpleCraftingRecipeSerializer<RfidBadgeRecipe>>) (RegistrySupplier<?>) RECIPES.register("rfid_badge_dye",
                    () -> new SimpleCraftingRecipeSerializer<>(RfidBadgeRecipe::new));

    public static void initialize() {
    }
}
