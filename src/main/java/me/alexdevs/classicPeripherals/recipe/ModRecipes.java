package me.alexdevs.classicPeripherals.recipe;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class ModRecipes {
    public static final SimpleCraftingRecipeSerializer<NfcCardRecipe> NFC_CARD_DYE = Registry
            .register(BuiltInRegistries.RECIPE_SERIALIZER,
                    ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, "nfc_card_dye"),
                    new SimpleCraftingRecipeSerializer<>(NfcCardRecipe::new)
            );

    public static final SimpleCraftingRecipeSerializer<RfidBadgeRecipe> RFID_BADGE_RECIPE = Registry
            .register(BuiltInRegistries.RECIPE_SERIALIZER,
                    new ResourceLocation(ClassicPeripherals.MOD_ID, "rfid_badge_dye"),
                    new SimpleCraftingRecipeSerializer<>(RfidBadgeRecipe::new)
            );

    public static void initialize() {}

}
