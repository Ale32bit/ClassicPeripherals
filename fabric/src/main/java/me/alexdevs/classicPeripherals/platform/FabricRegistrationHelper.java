package me.alexdevs.classicPeripherals.platform;

import me.alexdevs.classicPeripherals.platform.services.IRegistrationFactory;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class FabricRegistrationHelper implements IRegistrationFactory {
    @Override
    public <T> Registrar<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
        return new Registrar<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public <R extends T> RegistrySupplier<R> register(String name, Supplier<R> factory) {
                var id = ResourceLocation.fromNamespaceAndPath(modId, name);
                var registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(resourceKey.location());
                var value = Registry.register(registry, id, factory.get());
                return new RegistrySupplier<R>() {
                    @Override
                    public ResourceLocation getId() {
                        return id;
                    }

                    @Override
                    @SuppressWarnings("unchecked")
                    public R get() {
                        return (R) value;
                    }
                };
            }
        };
    }
}
