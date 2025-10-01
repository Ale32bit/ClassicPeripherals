package me.alexdevs.ccNetworks.block;

import me.alexdevs.ccNetworks.CcNetworks;
import me.alexdevs.ccNetworks.block.antenna.AntennaBlock;
import me.alexdevs.ccNetworks.block.tower.TowerBaseBlock;
import me.alexdevs.ccNetworks.block.tower.TowerHeadBlock;
import me.alexdevs.ccNetworks.block.tower.TowerSegmentBlock;
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
    public static final TowerBaseBlock TOWER_BASE = register(new TowerBaseBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.STONE)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
    ), "tower_base", true);

    public static final TowerSegmentBlock TOWER_SEGMENT = register(new TowerSegmentBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.STONE)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
    ), "tower_segment", true);

    public static final TowerHeadBlock TOWER_HEAD = register(new TowerHeadBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.GOLD)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
    ), "tower_head", true);

    public static final AntennaBlock ANTENNA = register(new AntennaBlock(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .mapColor(MapColor.GOLD)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
    ), "antenna", true);

    public static <T extends Block> T register(T block, String name, boolean shouldRegisterItem) {
        ResourceLocation id = new ResourceLocation(CcNetworks.MOD_ID, name);

        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Properties());
            Registry.register(BuiltInRegistries.ITEM, id, (Item)blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    public static void initialize() {
    }
}
