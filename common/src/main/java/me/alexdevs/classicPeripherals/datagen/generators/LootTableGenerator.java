package me.alexdevs.classicPeripherals.datagen.generators;

import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.platform.RegistrySupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jspecify.annotations.NonNull;

import java.util.function.BiConsumer;

public class LootTableGenerator implements LootTableSubProvider {

    public LootTableGenerator(HolderLookup.Provider ignoredRegistries) {
    }

    @Override
    public void generate(@NonNull BiConsumer<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>, LootTable.Builder> output) {
        dropSelf(output, ModRegistry.Blocks.TOWER_BASE);
        dropSelf(output, ModRegistry.Blocks.TOWER_SEGMENT);
        dropSelf(output, ModRegistry.Blocks.TOWER_HEAD);
        dropSelf(output, ModRegistry.Blocks.ANTENNA);
        dropSelf(output, ModRegistry.Blocks.NFC_READER);
        dropSelf(output, ModRegistry.Blocks.RFID_SCANNER);
        dropSelf(output, ModRegistry.Blocks.CRYPTOGRAPHIC_ACCELERATOR);
        dropSelf(output, ModRegistry.Blocks.SCANNER);
    }

    private static void dropSelf(BiConsumer<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>, LootTable.Builder> output,
                                  RegistrySupplier<? extends Block> supplier) {
        Block block = supplier.get();
        output.accept(block.getLootTable(), LootTable.lootTable()
                .setParamSet(LootContextParamSets.BLOCK)
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(block))
                        .apply(ApplyExplosionDecay.explosionDecay())));
    }
}
