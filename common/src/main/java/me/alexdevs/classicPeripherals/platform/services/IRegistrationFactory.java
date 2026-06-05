package me.alexdevs.classicPeripherals.platform.services;

import me.alexdevs.classicPeripherals.platform.Registrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/**
 * Creates {@link Registrar}s for arbitrary Minecraft registries. The Fabric implementation registers
 * entries eagerly; the NeoForge implementation defers them onto {@code DeferredRegister}s that are
 * flushed onto the mod event bus by the loader entrypoint.
 */
public interface IRegistrationFactory {
    <T> Registrar<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId);
}
