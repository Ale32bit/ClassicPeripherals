package me.alexdevs.classicPeripherals.datagen;

import me.alexdevs.classicPeripherals.datagen.generators.AutomaticDynamicRegistryGenerator;
import me.alexdevs.classicPeripherals.datagen.generators.PocketUpgradeGenerator;
import me.alexdevs.classicPeripherals.datagen.generators.TurtleUpgradeGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;

import java.util.concurrent.CompletableFuture;

public class ClassicPeripheralsDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        DataProviders.add(new FabricGeneratorSink(pack, fabricDataGenerator));

        addPocketUpgrades(pack, fabricDataGenerator.getRegistries());
        addTurtleUpgrades(pack, fabricDataGenerator.getRegistries());
    }

    private record FabricGeneratorSink(FabricDataGenerator.Pack pack, FabricDataGenerator generator)
            implements DataProviders.GeneratorSink {

        @Override
        public CompletableFuture<HolderLookup.Provider> registries() {
            return generator.getRegistries();
        }

        @Override
        public <T extends DataProvider> T add(DataProvider.Factory<T> factory) {
            return pack.addProvider((FabricDataOutput output) -> factory.create(output));
        }
    }

    private static void addPocketUpgrades(FabricDataGenerator.Pack pack, CompletableFuture<HolderLookup.Provider> registries) {
        var patch = PocketUpgradeGenerator.makeUpgradeRegistry(registries);
        pack.addProvider((FabricDataOutput output) -> new AutomaticDynamicRegistryGenerator("PocketRegistries", output, patch));
    }

    private static void addTurtleUpgrades(FabricDataGenerator.Pack pack, CompletableFuture<HolderLookup.Provider> registries) {
        var patch = TurtleUpgradeGenerator.makeUpgradeRegistry(registries);
        pack.addProvider((FabricDataOutput output) -> new AutomaticDynamicRegistryGenerator("TurtleRegistries", output, patch));
    }
}
