package me.alexdevs.classicPeripherals.datagen;

import com.google.gson.JsonElement;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A loader-neutral copy of {@link net.minecraft.data.models.ModelProvider} that accepts consumer callbacks
 * instead of requiring subclassing. Fabric and NeoForge both support this via their respective access wideners.
 */
public class ModelProvider implements DataProvider {
    private final PackOutput.PathProvider blockStatePath;
    private final PackOutput.PathProvider modelPath;
    private final Consumer<BlockModelGenerators> blocks;
    private final Consumer<ItemModelGenerators> items;

    public ModelProvider(PackOutput output, Consumer<BlockModelGenerators> blocks, Consumer<ItemModelGenerators> items) {
        this.blockStatePath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.modelPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
        this.blocks = blocks;
        this.items = items;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Map<Block, BlockStateGenerator> blockStates = new HashMap<>();
        Map<ResourceLocation, Supplier<JsonElement>> models = new HashMap<>();
        Set<Item> explicitItems = new HashSet<>();

        Consumer<BlockStateGenerator> addBlockState = generator -> {
            if (blockStates.containsKey(generator.getBlock()))
                throw new IllegalStateException("Duplicate blockstate definition for " + generator.getBlock());
            blockStates.put(generator.getBlock(), generator);
        };
        BiConsumer<ResourceLocation, Supplier<JsonElement>> addModel = (id, supplier) -> {
            if (models.containsKey(id)) throw new IllegalStateException("Duplicate model definition for " + id);
            models.put(id, supplier);
        };

        blocks.accept(new BlockModelGenerators(addBlockState, addModel, explicitItems::add));
        items.accept(new ItemModelGenerators(addModel));

        // Auto-delegate item models for any block that has a blockstate but no explicit item model
        for (var block : BuiltInRegistries.BLOCK) {
            if (!blockStates.containsKey(block)) continue;
            var item = Item.BY_BLOCK.get(block);
            if (item == null || explicitItems.contains(item)) continue;
            var model = ModelLocationUtils.getModelLocation(item);
            if (!models.containsKey(model))
                models.put(model, new DelegatedModel(ModelLocationUtils.getModelLocation(block)));
        }

        List<CompletableFuture<?>> futures = new ArrayList<>();
        saveCollection(cache, futures, blockStates,
                block -> blockStatePath.json(BuiltInRegistries.BLOCK.getKey(block)));
        saveCollection(cache, futures, models, modelPath::json);
        return Util.sequenceFailFast(futures);
    }

    private <T> void saveCollection(CachedOutput cache, List<CompletableFuture<?>> futures,
                                    Map<T, ? extends Supplier<JsonElement>> map, Function<T, Path> getPath) {
        for (var entry : map.entrySet())
            futures.add(DataProvider.saveStable(cache, entry.getValue().get(), getPath.apply(entry.getKey())));
    }

    @Override
    public String getName() {
        return "Models";
    }
}
