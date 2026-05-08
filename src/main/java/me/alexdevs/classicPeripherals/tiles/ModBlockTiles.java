package me.alexdevs.classicPeripherals.tiles;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockTiles {
    public static final BlockEntityType<TowerBlockEntity> TOWER_BASE = register("tower_base",
            BlockEntityType.Builder.of(TowerBlockEntity::new, ModBlocks.TOWER_BASE).build());

    public static final BlockEntityType<AntennaBlockEntity> ANTENNA = register("antenna",
            BlockEntityType.Builder.of(AntennaBlockEntity::new, ModBlocks.ANTENNA).build());

    public static final BlockEntityType<NfcReaderBlockEntity> NFC_READER = register("nfc_reader",
            BlockEntityType.Builder.of(NfcReaderBlockEntity::new, ModBlocks.NFC_READER).build());

    public static final BlockEntityType<RfidScannerBlockEntity> RFID_SCANNER = register("rfid_scanner",
            BlockEntityType.Builder.of(RfidScannerBlockEntity::new, ModBlocks.RFID_SCANNER).build());

    public static final BlockEntityType<CryptographicAcceleratorBlockEntity> CRYPTOGRAPHIC_ACCELERATOR = register("cryptographic_accelerator",
            BlockEntityType.Builder.of(CryptographicAcceleratorBlockEntity::new, ModBlocks.CRYPTOGRAPHIC_ACCELERATOR).build());

    public static final BlockEntityType<ScannerBlockEntity> SCANNER = register("scanner",
            BlockEntityType.Builder.of(ScannerBlockEntity::new, ModBlocks.SCANNER).build());

    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, path), blockEntityType);
    }

    public static void initialize() {
    }
}
