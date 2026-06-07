package me.alexdevs.classicPeripherals.datagen.generators;

import me.alexdevs.classicPeripherals.block.ModBlocks;
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

import java.util.function.BiConsumer;

public class LootTableGenerator implements LootTableSubProvider {
    @SuppressWarnings("unused")
    private final HolderLookup.Provider registries;

    public LootTableGenerator(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>, LootTable.Builder> output) {
        dropSelf(output, ModBlocks.TOWER_BASE);
        dropSelf(output, ModBlocks.TOWER_SEGMENT);
        dropSelf(output, ModBlocks.TOWER_HEAD);
        dropSelf(output, ModBlocks.ANTENNA);
        dropSelf(output, ModBlocks.NFC_READER);
        dropSelf(output, ModBlocks.RFID_SCANNER);
        dropSelf(output, ModBlocks.CRYPTOGRAPHIC_ACCELERATOR);
        dropSelf(output, ModBlocks.SCANNER);
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
