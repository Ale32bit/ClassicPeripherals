package me.alexdevs.classicPeripherals.platform;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * Registers peripheral providers for block entities. The loader supplies an implementation at the
 * correct lifecycle moment (immediately on Fabric via {@code PeripheralLookup}; during
 * {@code RegisterCapabilitiesEvent} on NeoForge).
 */
public interface PeripheralRegistrar {
    <B extends BlockEntity> void register(BlockEntityType<B> type, PeripheralProvider<B> provider);
}
