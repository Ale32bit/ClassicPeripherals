package me.alexdevs.classicPeripherals.item;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.platform.Registrar;
import me.alexdevs.classicPeripherals.platform.RegistrySupplier;
import me.alexdevs.classicPeripherals.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ModItems {
    public static final Registrar<Item> ITEMS = Services.REGISTRATION.create(Registries.ITEM, ClassicPeripherals.MOD_ID);

    public static final RegistrySupplier<Item> COPPER_COIL = register("copper_coil", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<NfcCardItem> NFC_CARD = register("nfc_card", () -> new NfcCardItem(new Item.Properties()
            .stacksTo(1)
    ));
    public static final RegistrySupplier<RfidBadgeItem> RFID_BADGE = register("rfid_badge", () -> new RfidBadgeItem(new Item.Properties()
            .stacksTo(1)
    ));

    public static <T extends Item> RegistrySupplier<T> register(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    public static void initialize() {

    }
}
