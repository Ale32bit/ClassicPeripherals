package me.alexdevs.classicPeripherals.platform;

import java.util.function.Supplier;

/**
 * A per-registry registration handle obtained from
 * {@link me.alexdevs.classicPeripherals.platform.services.IRegistrationFactory}. Collects entries
 * to register into a single Minecraft registry.
 */
public interface Registrar<T> {
    <R extends T> RegistrySupplier<R> register(String name, Supplier<R> factory);
}
