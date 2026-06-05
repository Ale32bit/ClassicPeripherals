package me.alexdevs.classicPeripherals.screen;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.platform.Registrar;
import me.alexdevs.classicPeripherals.platform.RegistrySupplier;
import me.alexdevs.classicPeripherals.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ModScreenHandlers {
    private static final Registrar<MenuType<?>> MENUS = Services.REGISTRATION.create(Registries.MENU, ClassicPeripherals.MOD_ID);

    @SuppressWarnings("unchecked")
    public static final RegistrySupplier<MenuType<ScannerMenu>> SCANNER =
            (RegistrySupplier<MenuType<ScannerMenu>>) (RegistrySupplier<?>) MENUS.register("scanner",
                    () -> new MenuType<>(ScannerMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static void initialize() {
    }
}
