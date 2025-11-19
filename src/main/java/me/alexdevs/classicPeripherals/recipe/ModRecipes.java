package me.alexdevs.classicPeripherals.recipe;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final RegistryObject<RecipeSerializer<NfcCardRecipe>> NFC_CARD_DYE = ClassicPeripherals.RECIPES
            .register("nfc_card_dye", () -> new SimpleCraftingRecipeSerializer<>(NfcCardRecipe::new));

    public static final RegistryObject<RecipeSerializer<RfidBadgeRecipe>> RFID_BADGE_RECIPE = ClassicPeripherals.RECIPES
            .register("rfid_badge_dye", () -> new SimpleCraftingRecipeSerializer<>(RfidBadgeRecipe::new));

    public static void initialize() {}

}
