package me.alexdevs.classicPeripherals.block;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.antenna.AntennaBlock;
import me.alexdevs.classicPeripherals.block.tower.TowerBaseBlock;
import me.alexdevs.classicPeripherals.block.tower.TowerHeadBlock;
import me.alexdevs.classicPeripherals.block.tower.TowerSegmentBlock;
import me.alexdevs.classicPeripherals.item.ModItems;
import me.alexdevs.classicPeripherals.platform.Registrar;
import me.alexdevs.classicPeripherals.platform.RegistrySupplier;
import me.alexdevs.classicPeripherals.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class ModBlocks {
    private static final Registrar<Block> BLOCKS = Services.REGISTRATION.create(Registries.BLOCK, ClassicPeripherals.MOD_ID);

    public static final RegistrySupplier<TowerBaseBlock> TOWER_BASE = register("tower_base", () -> new TowerBaseBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.STONE)
            .noOcclusion()
            .isValidSpawn(ModBlocks::never)
            .isRedstoneConductor(ModBlocks::never)
            .isSuffocating(ModBlocks::never)
            .isViewBlocking(ModBlocks::never)
    ), true);

    public static final RegistrySupplier<TowerSegmentBlock> TOWER_SEGMENT = register("tower_segment", () -> new TowerSegmentBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.STONE)
            .noOcclusion()
            .isValidSpawn(ModBlocks::never)
            .isRedstoneConductor(ModBlocks::never)
            .isSuffocating(ModBlocks::never)
            .isViewBlocking(ModBlocks::never)
    ), true);

    public static final RegistrySupplier<TowerHeadBlock> TOWER_HEAD = register("tower_head", () -> new TowerHeadBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.GOLD)
            .noOcclusion()
            .isValidSpawn(ModBlocks::never)
            .isRedstoneConductor(ModBlocks::never)
            .isSuffocating(ModBlocks::never)
            .isViewBlocking(ModBlocks::never)
    ), true);

    public static final RegistrySupplier<AntennaBlock> ANTENNA = register("antenna", () -> new AntennaBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.GOLD)
            .noOcclusion()
            .isValidSpawn(ModBlocks::never)
            .isRedstoneConductor(ModBlocks::never)
            .isSuffocating(ModBlocks::never)
            .isViewBlocking(ModBlocks::never)
    ), true);

    public static final RegistrySupplier<NfcReaderBlock> NFC_READER = register("nfc_reader", () -> new NfcReaderBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.STONE)
            .isValidSpawn(ModBlocks::never)
    ), true);

    public static final RegistrySupplier<RfidScannerBlock> RFID_SCANNER = register("rfid_scanner", () -> new RfidScannerBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.STONE)
            .isValidSpawn(ModBlocks::never)
            .isRedstoneConductor(ModBlocks::never)
    ), true);

    public static final RegistrySupplier<CryptographicAcceleratorBlock> CRYPTOGRAPHIC_ACCELERATOR = register("cryptographic_accelerator", () -> new CryptographicAcceleratorBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.STONE)
            .isValidSpawn(ModBlocks::never)
            .isRedstoneConductor(ModBlocks::never)
    ), true);

    public static final RegistrySupplier<ScannerBlock> SCANNER = register("scanner", () -> new ScannerBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.STONE)
            .isValidSpawn(ModBlocks::never)
            .isRedstoneConductor(ModBlocks::never)
    ), true);

    private static <T extends Block> RegistrySupplier<T> register(String name, Supplier<T> block, boolean shouldRegisterItem) {
        var registered = BLOCKS.register(name, block);
        if (shouldRegisterItem) {
            ModItems.ITEMS.register(name, () -> new BlockItem(registered.get(), new Item.Properties()));
        }
        return registered;
    }

    public static void initialize() {
    }

    public static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos, EntityType<?> entity) {
        return false;
    }

    public static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }
}
