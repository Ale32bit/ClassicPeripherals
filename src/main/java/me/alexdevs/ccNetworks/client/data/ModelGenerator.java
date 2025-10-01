package me.alexdevs.ccNetworks.client.data;

import me.alexdevs.ccNetworks.block.ModBlocks;
import me.alexdevs.ccNetworks.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;

import static net.minecraft.data.models.model.ModelLocationUtils.getModelLocation;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        generators.delegateItemModel(ModBlocks.TOWER_BASE, getModelLocation(ModBlocks.TOWER_BASE));
        generators.delegateItemModel(ModBlocks.TOWER_SEGMENT, getModelLocation(ModBlocks.TOWER_SEGMENT));
        generators.delegateItemModel(ModBlocks.TOWER_HEAD, getModelLocation(ModBlocks.TOWER_HEAD, "_off"));
        generators.delegateItemModel(ModBlocks.ANTENNA, getModelLocation(ModBlocks.ANTENNA, "_off"));

    }

    @Override
    public void generateItemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(ModItems.COPPER_COIL, ModelTemplates.FLAT_ITEM);
    }
}
