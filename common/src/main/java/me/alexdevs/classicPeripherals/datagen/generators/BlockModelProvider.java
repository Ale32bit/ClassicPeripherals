package me.alexdevs.classicPeripherals.datagen.generators;

import dan200.computercraft.api.ComputerCraftAPI;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.peripherals.nfc.NfcReaderBlock;
import me.alexdevs.classicPeripherals.peripherals.rfid.RfidScannerBlock;
import me.alexdevs.classicPeripherals.peripherals.scanner.ScannerBlock;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Optional;
import java.util.function.Function;

import static net.minecraft.data.models.model.ModelLocationUtils.getModelLocation;
import static net.minecraft.data.models.model.TextureMapping.getBlockTexture;

public class BlockModelProvider {

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
            Optional.of("_right"),
            TextureSlot.TEXTURE
    );

    public static void addBlockModels(BlockModelGenerators generators) {
        generators.delegateItemModel(ModRegistry.Blocks.TOWER_BASE.get(), getModelLocation(ModRegistry.Blocks.TOWER_BASE.get()));
        generators.delegateItemModel(ModRegistry.Blocks.TOWER_SEGMENT.get(), getModelLocation(ModRegistry.Blocks.TOWER_SEGMENT.get()));
        generators.delegateItemModel(ModRegistry.Blocks.TOWER_HEAD.get(), getModelLocation(ModRegistry.Blocks.TOWER_HEAD.get(), "_off"));
        generators.delegateItemModel(ModRegistry.Blocks.ANTENNA.get(), getModelLocation(ModRegistry.Blocks.ANTENNA.get(), "_off"));

        createNfcReaderModel(generators, ModRegistry.Blocks.NFC_READER.get());
        createRfidScannerModel(generators, ModRegistry.Blocks.RFID_SCANNER.get());
        createScannerModel(generators, ModRegistry.Blocks.SCANNER.get());

        generators.createHorizontallyRotatedBlock(ModRegistry.Blocks.CRYPTOGRAPHIC_ACCELERATOR.get(), TexturedModel.ORIENTABLE);
        generators.delegateItemModel(ModRegistry.Blocks.CRYPTOGRAPHIC_ACCELERATOR.get(), getModelLocation(ModRegistry.Blocks.CRYPTOGRAPHIC_ACCELERATOR.get()));

        registerTurtleUpgrade(generators, "block/turtle_radio", "block/turtle_radio_face");
        registerTurtleUpgrade(generators, "block/turtle_crypto", "block/turtle_crypto_face");
    }

    private static void createNfcReaderModel(BlockModelGenerators generators, NfcReaderBlock block) {
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
                            TextureMapping.orientableCube(block).put(TextureSlot.FRONT, getBlockTexture(block, suffix)),
                            generators.modelOutput
                    );
                }))
        );
        generators.delegateItemModel(block, getModelLocation(block, "_none"));
    }

    private static void createRfidScannerModel(BlockModelGenerators generators, RfidScannerBlock block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(BlockModelGenerators.createFacingDispatch())
                .with(createModelDispatch(RfidScannerBlock.ACTIVE, value -> {
                    var suffix = value ? "_on" : "";
                    return RFID_SCANNER_MODEL.create(
                            getModelLocation(block, suffix),
                            new TextureMapping().put(TextureSlot.ALL, getBlockTexture(block, suffix)),
                            generators.modelOutput
                    );
                }))
        );
        generators.delegateItemModel(block, getModelLocation(block));
    }

    private static void createScannerModel(BlockModelGenerators generators, ScannerBlock block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(BlockModelGenerators.createHorizontalFacingDispatch())
                .with(createModelDispatch(ScannerBlock.TRAY, value -> {
                    var suffix = "_front" + (value ? "_tray" : "");
                    return ModelTemplates.CUBE_ORIENTABLE.createWithSuffix(
                            block, suffix,
                            TextureMapping.orientableCube(block).put(TextureSlot.FRONT, getBlockTexture(block, suffix)),
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

    private static <T extends Comparable<T>> PropertyDispatch createModelDispatch(
            Property<T> property, Function<T, ResourceLocation> makeModel) {
        var dispatch = PropertyDispatch.property(property);
        for (var value : property.getPossibleValues())
            dispatch.select(value, Variant.variant().with(VariantProperties.MODEL, makeModel.apply(value)));
        return dispatch;
    }
}
