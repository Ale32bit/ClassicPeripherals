package me.alexdevs.classicPeripherals;

import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import me.alexdevs.classicPeripherals.datagen.DataProviders;
import me.alexdevs.classicPeripherals.datagen.generators.PocketUpgradeGenerator;
import me.alexdevs.classicPeripherals.datagen.generators.TurtleUpgradeGenerator;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class ClassicPeripheralsDataGenerator {

    public static void gatherData(GatherDataEvent event) {
        // Register datapack entries first so getLookupProvider() includes them
        event.createDatapackRegistryObjects(Util.make(new RegistrySetBuilder(), builder -> {
            builder.add(IPocketUpgrade.REGISTRY, PocketUpgradeGenerator::addUpgrades);
            builder.add(ITurtleUpgrade.REGISTRY, TurtleUpgradeGenerator::addUpgrades);
        }));

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        DataProviders.add(new NeoForgeGeneratorSink(generator, output, lookupProvider));
    }

    private record NeoForgeGeneratorSink(DataGenerator generator, PackOutput output,
                                         CompletableFuture<HolderLookup.Provider> registriesFuture)
            implements DataProviders.GeneratorSink {

        @Override
        public CompletableFuture<HolderLookup.Provider> registries() {
            return registriesFuture;
        }

        @Override
        public <T extends DataProvider> T add(DataProvider.Factory<T> factory) {
            return generator.addProvider(true, factory.create(output));
        }
    }
}
