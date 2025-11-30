package me.alexdevs.classicPeripherals.item;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
    public static final RegistryObject<Item> COPPER_COIL = register("copper_coil", () -> new Item(new Item.Properties()));
    public static final RegistryObject<NfcCardItem> NFC_CARD = register("nfc_card", () -> new NfcCardItem(new Item.Properties()
            .stacksTo(1)
    ));
    public static final RegistryObject<RfidBadgeItem> RFID_BADGE = register("rfid_badge", () -> new RfidBadgeItem(new Item.Properties()
            .stacksTo(1)
    ));

    public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item) {
        return ClassicPeripherals.ITEMS.register(name, item);
    }

    public static void initialize() {

    }
}
