package me.alexdevs.classicPeripherals.datagen.generators;

import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.block.NfcReaderBlock;
import me.alexdevs.classicPeripherals.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.function.BiFunction;
import java.util.function.Function;

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

        createNfcReaderModel(generators, ModBlocks.NFC_READER);
    }

    @Override
    public void generateItemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(ModItems.COPPER_COIL, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(ModItems.NFC_CARD, ModelTemplates.FLAT_ITEM);
    }

    private void createNfcReaderModel(BlockModelGenerators generators, NfcReaderBlock block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(BlockModelGenerators.createHorizontalFacingDispatch())
                .with(createModelDispatch(NfcReaderBlock.STATE, value -> {
                    var suffix = "_front" + switch(value) {
                        case NONE -> "";
                        case READING -> "_reading";
                        case WRITING -> "_writing";
                    };

                    return ModelTemplates.CUBE_ORIENTABLE.createWithSuffix(
                            block, "_" + value.getSerializedName(),
                            TextureMapping.orientableCube(block).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, suffix)),
                            generators.modelOutput
                    );
                }))
        );

        generators.delegateItemModel(block, getModelLocation(block, "_none"));
    }

    private static <T extends Comparable<T>> PropertyDispatch createModelDispatch(Property<T> property, Function<T, ResourceLocation> makeModel) {
        var variant = PropertyDispatch.property(property);
        for (var value : property.getPossibleValues()) {
            variant.select(value, Variant.variant().with(VariantProperties.MODEL, makeModel.apply(value)));
        }
        return variant;
    }

    private static <T extends Comparable<T>, U extends Comparable<U>> PropertyDispatch createModelDispatch(
            Property<T> propertyT, Property<U> propertyU, BiFunction<T, U, ResourceLocation> makeModel
    ) {
        var variant = PropertyDispatch.properties(propertyT, propertyU);
        for (var valueT : propertyT.getPossibleValues()) {
            for (var valueU : propertyU.getPossibleValues()) {
                variant.select(valueT, valueU, Variant.variant().with(VariantProperties.MODEL, makeModel.apply(valueT, valueU)));
            }
        }
        return variant;
    }
}
