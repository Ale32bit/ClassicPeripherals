package me.alexdevs.ccNetworks.block;

import me.alexdevs.ccNetworks.CcNetworks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final TowerBaseBlock TOWER_BASE = register(new TowerBaseBlock(BlockBehaviour.Properties.of()), "tower_base", true);
    public static final TowerSegmentBlock TOWER_SEGMENT = register(new TowerSegmentBlock(BlockBehaviour.Properties.of()), "tower_segment", true);
    public static final TowerHeadBlock TOWER_HEAD = register(new TowerHeadBlock(BlockBehaviour.Properties.of()), "tower_head", true);

    public static <T extends Block> T register(T block, String name, boolean shouldRegisterItem) {
        // Register the block and its item.
        ResourceLocation id = new ResourceLocation(CcNetworks.MOD_ID, name);

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:air` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Properties());
            Registry.register(BuiltInRegistries.ITEM, id, (Item)blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    public static void initialize() {
    }
}
