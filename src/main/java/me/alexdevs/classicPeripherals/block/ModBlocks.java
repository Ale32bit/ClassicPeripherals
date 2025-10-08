package me.alexdevs.classicPeripherals.block;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.antenna.AntennaBlock;
import me.alexdevs.classicPeripherals.block.tower.TowerBaseBlock;
import me.alexdevs.classicPeripherals.block.tower.TowerHeadBlock;
import me.alexdevs.classicPeripherals.block.tower.TowerSegmentBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredBlock<TowerBaseBlock> TOWER_BASE = register("tower_base", () -> new TowerBaseBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.STONE)
            .noOcclusion()
            .isValidSpawn(ModBlocks::never)
            .isRedstoneConductor(ModBlocks::never)
            .isSuffocating(ModBlocks::never)
            .isViewBlocking(ModBlocks::never)
    ), true);

    public static final DeferredBlock<TowerSegmentBlock> TOWER_SEGMENT = register("tower_segment", () -> new TowerSegmentBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.STONE)
            .noOcclusion()
            .isValidSpawn(ModBlocks::never)
            .isRedstoneConductor(ModBlocks::never)
            .isSuffocating(ModBlocks::never)
            .isViewBlocking(ModBlocks::never)
    ), true);

    public static final DeferredBlock<TowerHeadBlock> TOWER_HEAD = register("tower_head", () -> new TowerHeadBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.GOLD)
            .noOcclusion()
            .isValidSpawn(ModBlocks::never)
            .isRedstoneConductor(ModBlocks::never)
            .isSuffocating(ModBlocks::never)
            .isViewBlocking(ModBlocks::never)
    ), true);

    public static final DeferredBlock<AntennaBlock> ANTENNA = register("antenna", () -> new AntennaBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.GOLD)
            .noOcclusion()
            .isValidSpawn(ModBlocks::never)
            .isRedstoneConductor(ModBlocks::never)
            .isSuffocating(ModBlocks::never)
            .isViewBlocking(ModBlocks::never)
    ), true);

    public static final DeferredBlock<NfcReaderBlock> NFC_READER = register("nfc_reader", () -> new NfcReaderBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.STONE)
            .isValidSpawn(ModBlocks::never)
            .isRedstoneConductor(ModBlocks::never)
    ), true);

    public static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> block, boolean shouldRegisterItem) {
        var registeredBlock = ClassicPeripherals.BLOCKS.register(name, block);

        if (shouldRegisterItem) {
            ClassicPeripherals.ITEMS.registerSimpleBlockItem(name, registeredBlock);
        }

        return registeredBlock;
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
