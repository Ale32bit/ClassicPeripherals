package me.alexdevs.classicPeripherals.platform;

import me.alexdevs.classicPeripherals.platform.services.IRegistrationFactory;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NeoForgeRegistrationHelper implements IRegistrationFactory {
    public static IEventBus MOD_BUS;

    @Override
    public <T> Registrar<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
        var dr = DeferredRegister.create(resourceKey, modId);
        dr.register(MOD_BUS);
        return new Registrar<T>() {
            @Override
            public <R extends T> RegistrySupplier<R> register(String name, Supplier<R> factory) {
                var id = ResourceLocation.fromNamespaceAndPath(modId, name);
                var holder = dr.register(name, factory);
                return new RegistrySupplier<R>() {
                    @Override
                    public ResourceLocation getId() {
                        return id;
                    }

                    @Override
                    public R get() {
                        return holder.get();
                    }
                };
            }
        };
    }
}
