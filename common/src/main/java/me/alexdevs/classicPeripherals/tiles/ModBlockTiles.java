package me.alexdevs.classicPeripherals.tiles;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.platform.Registrar;
import me.alexdevs.classicPeripherals.platform.RegistrySupplier;
import me.alexdevs.classicPeripherals.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModBlockTiles {
    private static final Registrar<BlockEntityType<?>> BLOCK_ENTITIES = Services.REGISTRATION.create(Registries.BLOCK_ENTITY_TYPE, ClassicPeripherals.MOD_ID);

    public static final RegistrySupplier<BlockEntityType<TowerBlockEntity>> TOWER_BASE = register("tower_base",
            () -> BlockEntityType.Builder.of(TowerBlockEntity::new, ModBlocks.TOWER_BASE.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<AntennaBlockEntity>> ANTENNA = register("antenna",
            () -> BlockEntityType.Builder.of(AntennaBlockEntity::new, ModBlocks.ANTENNA.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<NfcReaderBlockEntity>> NFC_READER = register("nfc_reader",
            () -> BlockEntityType.Builder.of(NfcReaderBlockEntity::new, ModBlocks.NFC_READER.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<RfidScannerBlockEntity>> RFID_SCANNER = register("rfid_scanner",
            () -> BlockEntityType.Builder.of(RfidScannerBlockEntity::new, ModBlocks.RFID_SCANNER.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<CryptographicAcceleratorBlockEntity>> CRYPTOGRAPHIC_ACCELERATOR = register("cryptographic_accelerator",
            () -> BlockEntityType.Builder.of(CryptographicAcceleratorBlockEntity::new, ModBlocks.CRYPTOGRAPHIC_ACCELERATOR.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<ScannerBlockEntity>> SCANNER = register("scanner",
            () -> BlockEntityType.Builder.of(ScannerBlockEntity::new, ModBlocks.SCANNER.get()).build(null));

    @SuppressWarnings("unchecked")
    private static <T extends BlockEntityType<?>> RegistrySupplier<T> register(String path, Supplier<T> factory) {
        return (RegistrySupplier<T>) (RegistrySupplier<?>) BLOCK_ENTITIES.register(path, factory);
    }

    public static void initialize() {
    }
}
