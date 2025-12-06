package me.alexdevs.classicPeripherals.integrations;


import net.neoforged.fml.ModList;

public class CuriosIntegration {
    public static boolean isLoaded() {
        return ModList.get().isLoaded("curios");
    }
}
