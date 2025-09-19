package me.alexdevs.ccNetworks.tiles;

import me.alexdevs.ccNetworks.CcNetworks;
import me.alexdevs.ccNetworks.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockTiles {
    public static final BlockEntityType<TowerBaseBlockEntity> TOWER_BASE = register("tower_base", FabricBlockEntityTypeBuilder.create(TowerBaseBlockEntity::new, ModBlocks.TOWER_BASE).build());

    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(CcNetworks.MOD_ID, path), blockEntityType);
    }

    public static void initialize() {
    }
}
