package me.alexdevs.classicPeripherals.block;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.antenna.AntennaBlock;
import me.alexdevs.classicPeripherals.block.tower.TowerBaseBlock;
import me.alexdevs.classicPeripherals.block.tower.TowerHeadBlock;
import me.alexdevs.classicPeripherals.block.tower.TowerSegmentBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {
    public static final TowerBaseBlock TOWER_BASE = register("tower_base", new TowerBaseBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.STONE)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
    ), true);

    public static final TowerSegmentBlock TOWER_SEGMENT = register("tower_segment", new TowerSegmentBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.STONE)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
    ), true);

    public static final TowerHeadBlock TOWER_HEAD = register("tower_head", new TowerHeadBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.GOLD)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
    ), true);

    public static final AntennaBlock ANTENNA = register("antenna", new AntennaBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.GOLD)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
    ), true);

    public static final NfcReaderBlock NFC_READER = register("nfc_reader", new NfcReaderBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.STONE)
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
    ), true);

    public static <T extends Block> T register(String name, T block, boolean shouldRegisterItem) {
        ResourceLocation id = new ResourceLocation(ClassicPeripherals.MOD_ID, name);

        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Properties());
            Registry.register(BuiltInRegistries.ITEM, id, (Item)blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    public static void initialize() {
    }
}
