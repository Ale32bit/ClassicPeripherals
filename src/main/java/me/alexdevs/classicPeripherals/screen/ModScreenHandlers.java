package me.alexdevs.classicPeripherals.screen;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;

public class ModScreenHandlers {
    public static final MenuType<ScannerMenu> SCANNER =
            Registry.register(
                    BuiltInRegistries.MENU,
                    ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, "scanner"),
                    new MenuType<>(ScannerMenu::new, FeatureFlagSet.of())
            );

    public static void initialize() {
    }
}
