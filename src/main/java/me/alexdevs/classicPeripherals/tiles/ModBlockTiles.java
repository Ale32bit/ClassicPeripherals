package me.alexdevs.classicPeripherals.tiles;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.CryptographicAcceleratorBlock;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class ModBlockTiles {
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TowerBlockEntity>> TOWER_BASE = register("tower_base",
            () -> BlockEntityType.Builder.of(TowerBlockEntity::new, ModBlocks.TOWER_BASE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AntennaBlockEntity>> ANTENNA = register("antenna",
            () -> BlockEntityType.Builder.of(AntennaBlockEntity::new, ModBlocks.ANTENNA.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NfcReaderBlockEntity>> NFC_READER = register("nfc_reader",
            () -> BlockEntityType.Builder.of(NfcReaderBlockEntity::new, ModBlocks.NFC_READER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RfidScannerBlockEntity>> RFID_SCANNER = register("rfid_scanner",
            () -> BlockEntityType.Builder.of(RfidScannerBlockEntity::new, ModBlocks.RFID_SCANNER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CryptographicAcceleratorBlockEntity>> CRYPTOGRAPHIC_ACCELERATOR = register("cryptographic_accelerator",
            () -> BlockEntityType.Builder.of(CryptographicAcceleratorBlockEntity::new, ModBlocks.CRYPTOGRAPHIC_ACCELERATOR.get()).build(null));

    public static <T extends BlockEntityType<?>> DeferredHolder<BlockEntityType<?>, T> register(String path, Supplier<T> blockEntityType) {
        return ClassicPeripherals.BLOCK_ENTITIES.register(path, blockEntityType);
    }

    public static void initialize() {
    }
}
