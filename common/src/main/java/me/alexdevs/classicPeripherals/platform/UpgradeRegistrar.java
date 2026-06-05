package me.alexdevs.classicPeripherals.platform;

import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.upgrades.UpgradeType;
import net.minecraft.resources.ResourceLocation;

/**
 * Registers CC: Tweaked turtle/pocket upgrade types. The loader supplies an implementation at the
 * correct lifecycle moment (eagerly on Fabric; during {@code RegisterEvent} on NeoForge).
 */
public interface UpgradeRegistrar {
    void registerPocketUpgrade(ResourceLocation id, UpgradeType<? extends IPocketUpgrade> type);

    void registerTurtleUpgrade(ResourceLocation id, UpgradeType<? extends ITurtleUpgrade> type);
}
