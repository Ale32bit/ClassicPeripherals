package me.alexdevs.classicPeripherals.platform;

import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import net.minecraft.resources.ResourceLocation;

/**
 * Registers CC: Tweaked turtle/pocket upgrade types. The loader supplies an implementation at the
 * correct lifecycle moment (eagerly on Fabric; during {@code RegisterEvent} on NeoForge).
 */
public interface UpgradeRegistrar {
    void registerPocketUpgrade(String id, UpgradeType<? extends IPocketUpgrade> type);

    void registerTurtleUpgrade(String id, UpgradeType<? extends ITurtleUpgrade> type);

    default ResourceLocation withPath(String path) {
        return ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, path);
    }
}
