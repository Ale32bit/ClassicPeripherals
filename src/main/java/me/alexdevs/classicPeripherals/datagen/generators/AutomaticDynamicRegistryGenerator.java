package me.alexdevs.classicPeripherals.datagen.generators;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;

import java.util.concurrent.CompletableFuture;

public class AutomaticDynamicRegistryGenerator extends FabricDynamicRegistryProvider {
    private final String name;
    public AutomaticDynamicRegistryGenerator(String name, FabricDataOutput output, CompletableFuture<RegistrySetBuilder.PatchedRegistries> registries) {
        super(output, registries.thenApply(RegistrySetBuilder.PatchedRegistries::patches));
        this.name = name;
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        for (var r : DynamicRegistries.getDynamicRegistries()) entries.addAll(registries.lookupOrThrow(r.key()));
    }

    @Override
    public String getName() {
        return this.name;
    }
}
