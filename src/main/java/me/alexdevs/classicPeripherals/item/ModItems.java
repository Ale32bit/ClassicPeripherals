package me.alexdevs.classicPeripherals.item;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final Item COPPER_COIL = register("copper_coil", new Item(new Item.Properties()));
    public static final NfcCardItem NFC_CARD = register("nfc_card", new NfcCardItem(new Item.Properties()
            .stacksTo(1)
    ));

    public static <T extends Item> T register(String name, T item) {
        ResourceLocation id = new ResourceLocation(ClassicPeripherals.MOD_ID, name);
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }

    public static void initialize() {

    }
}
