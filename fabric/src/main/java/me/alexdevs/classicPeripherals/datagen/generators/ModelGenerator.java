package me.alexdevs.classicPeripherals.datagen.generators;

import dan200.computercraft.api.ComputerCraftAPI;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.block.NfcReaderBlock;
import me.alexdevs.classicPeripherals.block.RfidScannerBlock;
import me.alexdevs.classicPeripherals.block.ScannerBlock;
import me.alexdevs.classicPeripherals.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraft.data.models.model.ModelLocationUtils.getModelLocation;
import static net.minecraft.data.models.model.TextureMapping.getItemTexture;

public class ModelGenerator extends FabricModelProvider {

    public static final ModelTemplate RFID_SCANNER_MODEL = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, "block/rfid_scanner_base")),
            Optional.empty(),
            TextureSlot.ALL
    );

    private static final ModelTemplate TURTLE_UPGRADE_LEFT = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(ComputerCraftAPI.MOD_ID, "block/turtle_upgrade_base_left")),
            Optional.of("_left"),
            TextureSlot.TEXTURE
    );
    private static final ModelTemplate TURTLE_UPGRADE_RIGHT = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(ComputerCraftAPI.MOD_ID, "block/turtle_upgrade_base_right")),
            Optional.of("_left"),
            TextureSlot.TEXTURE
    );

    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        generators.delegateItemModel(ModBlocks.TOWER_BASE.get(), getModelLocation(ModBlocks.TOWER_BASE.get()));
        generators.delegateItemModel(ModBlocks.TOWER_SEGMENT.get(), getModelLocation(ModBlocks.TOWER_SEGMENT.get()));
        generators.delegateItemModel(ModBlocks.TOWER_HEAD.get(), getModelLocation(ModBlocks.TOWER_HEAD.get(), "_off"));
        generators.delegateItemModel(ModBlocks.ANTENNA.get(), getModelLocation(ModBlocks.ANTENNA.get(), "_off"));

        createNfcReaderModel(generators, ModBlocks.NFC_READER.get());
        createRfidScannerModel(generators, ModBlocks.RFID_SCANNER.get());
        createScannerModel(generators, ModBlocks.SCANNER.get());

        generators.createHorizontallyRotatedBlock(ModBlocks.CRYPTOGRAPHIC_ACCELERATOR.get(), TexturedModel.ORIENTABLE);
        generators.delegateItemModel(ModBlocks.CRYPTOGRAPHIC_ACCELERATOR.get(), getModelLocation(ModBlocks.CRYPTOGRAPHIC_ACCELERATOR.get()));

        registerTurtleUpgrade(generators, "block/turtle_radio", "block/turtle_radio_face");
        registerTurtleUpgrade(generators, "block/turtle_crypto", "block/turtle_crypto_face");
    }

    @Override
    public void generateItemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(ModItems.COPPER_COIL.get(), ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(ModItems.NFC_CARD.get(), ModelTemplates.FLAT_ITEM);

        generateTwoLayeredItem(generators, ModItems.RFID_BADGE.get(), "_color");
    }

    private void generateTwoLayeredItem(ItemModelGenerators generators, Item item, String secondLayerSuffix) {
        ModelTemplates.TWO_LAYERED_ITEM.create(
                ModelLocationUtils.getModelLocation(item),
                TextureMapping.layered(getItemTexture(item), getItemTexture(item, secondLayerSuffix)),
                generators.output);
    }

    private void createNfcReaderModel(BlockModelGenerators generators, NfcReaderBlock block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(BlockModelGenerators.createHorizontalFacingDispatch())
                .with(createModelDispatch(NfcReaderBlock.STATE, value -> {
                    var suffix = "_front" + switch (value) {
                        case NONE -> "";
                        case READING -> "_reading";
                        case WRITING -> "_writing";
                        case SIGNING -> "_signing";
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

    private void createRfidScannerModel(BlockModelGenerators generators, RfidScannerBlock block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(BlockModelGenerators.createFacingDispatch())
                .with(createModelDispatch(RfidScannerBlock.ACTIVE, value -> {
                    var suffix = value ? "_on" : "";

                    return RFID_SCANNER_MODEL.create(
                            getModelLocation(block, suffix),
                            new TextureMapping().put(TextureSlot.ALL, TextureMapping.getBlockTexture(block, suffix)),
                            generators.modelOutput
                    );
                })));

        generators.delegateItemModel(block, getModelLocation(block));
    }

    private void createScannerModel(BlockModelGenerators generators, ScannerBlock block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(BlockModelGenerators.createHorizontalFacingDispatch())
                .with(createModelDispatch(ScannerBlock.TRAY, value -> {
                    var suffix = "_front" + (value ? "_tray" : "");

                    return ModelTemplates.CUBE_ORIENTABLE.createWithSuffix(
                            block, suffix,
                            TextureMapping.orientableCube(block).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, suffix)),
                            generators.modelOutput
                    );
                }))
        );

        generators.delegateItemModel(block, getModelLocation(block, "_front"));
    }

    private static void registerTurtleUpgrade(BlockModelGenerators generators, String name, String texture) {
        TURTLE_UPGRADE_LEFT.create(
                ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, name + "_left"),
                TextureMapping.defaultTexture(ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, texture)),
                generators.modelOutput
        );
        TURTLE_UPGRADE_RIGHT.create(
                ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, name + "_right"),
                TextureMapping.defaultTexture(ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, texture)),
                generators.modelOutput
        );
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
