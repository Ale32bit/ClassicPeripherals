package me.alexdevs.classicPeripherals.datagen;

import me.alexdevs.classicPeripherals.datagen.generators.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DataProviders {

    public interface GeneratorSink {
        CompletableFuture<HolderLookup.Provider> registries();

        <T extends DataProvider> T add(DataProvider.Factory<T> factory);
    }

    public static void add(GeneratorSink generator) {
        var registries = generator.registries();

        generator.add(output -> new ModelProvider(output, BlockModelProvider::addBlockModels, ItemModelProvider::addItemModels));
        generator.add(output -> new LanguageGenerator(output));
        generator.add(output -> new RecipeGenerator(output, registries));
        generator.add(output -> new TagGenerator(output, registries));
        generator.add(output -> new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(LootTableGenerator::new, LootContextParamSets.BLOCK)
        ), registries));
    }
}
