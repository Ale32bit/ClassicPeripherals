package me.alexdevs.classicPeripherals.screen;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModScreenHandlers {
    public static final DeferredHolder<MenuType<?>, MenuType<ScannerMenu>> SCANNER =
            ClassicPeripherals.MENUS.register("scanner", () -> new MenuType<>(ScannerMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static void initialize() {
    }
}
