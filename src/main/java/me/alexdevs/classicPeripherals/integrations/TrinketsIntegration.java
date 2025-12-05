package me.alexdevs.classicPeripherals.integrations;

import net.fabricmc.loader.api.FabricLoader;

public class TrinketsIntegration {
    public static boolean isLoaded() {
        return FabricLoader.getInstance().isModLoaded("trinkets");
    }
}
