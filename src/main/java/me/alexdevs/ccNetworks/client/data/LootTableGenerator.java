package me.alexdevs.ccNetworks.client.data;

import me.alexdevs.ccNetworks.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class LootTableGenerator extends FabricBlockLootTableProvider {
    public LootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        this.dropSelf(ModBlocks.TOWER_BASE);
        this.dropSelf(ModBlocks.TOWER_SEGMENT);
        this.dropSelf(ModBlocks.TOWER_HEAD);
        this.dropSelf(ModBlocks.ANTENNA);
    }
}
