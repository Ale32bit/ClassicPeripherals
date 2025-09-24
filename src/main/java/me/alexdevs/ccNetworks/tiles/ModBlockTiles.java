package me.alexdevs.ccNetworks.tiles;

import me.alexdevs.ccNetworks.CcNetworks;
import me.alexdevs.ccNetworks.block.AntennaBlock;
import me.alexdevs.ccNetworks.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockTiles {
    public static final BlockEntityType<TowerBlockEntity> TOWER_BASE = register("tower_base", FabricBlockEntityTypeBuilder.create(TowerBlockEntity::new, ModBlocks.TOWER_BASE).build());
    public static final BlockEntityType<AntennaBlockEntity> ANTENNA = register("antenna", FabricBlockEntityTypeBuilder.create(AntennaBlockEntity::new, ModBlocks.ANTENNA).build());

    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(CcNetworks.MOD_ID, path), blockEntityType);
    }

    public static void initialize() {
    }
}
