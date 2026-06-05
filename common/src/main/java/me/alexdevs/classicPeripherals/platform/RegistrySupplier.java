package me.alexdevs.classicPeripherals.platform;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

/**
 * A handle to a registered object. On NeoForge this wraps a {@code DeferredHolder}; on Fabric the
 * value is registered eagerly and held directly. Either way {@link #get()} returns the registered
 * value, so common code can use the same access pattern on both loaders.
 */
public interface RegistrySupplier<T> extends Supplier<T> {
    ResourceLocation getId();
}
