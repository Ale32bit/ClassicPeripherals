package me.alexdevs.classicPeripherals.item;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredItem<Item> COPPER_COIL = register("copper_coil", () -> new Item(new Item.Properties()));
    public static final DeferredItem<NfcCardItem> NFC_CARD = register("nfc_card", () -> new NfcCardItem(new Item.Properties()
            .stacksTo(1)
    ));
    public static final DeferredItem<RfidBadgeItem> RFID_BADGE = register("rfid_badge", () -> new RfidBadgeItem(new Item.Properties()
            .stacksTo(1)
    ));

    public static <T extends Item> DeferredItem<T> register(String name, Supplier<T> item) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, name);
        return ClassicPeripherals.ITEMS.register(name, item);
    }

    public static void initialize() {

    }
}
