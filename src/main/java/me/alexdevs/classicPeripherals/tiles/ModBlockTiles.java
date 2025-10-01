package me.alexdevs.classicPeripherals.tiles;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockTiles {
    public static final BlockEntityType<TowerBlockEntity> TOWER_BASE = register("tower_base", FabricBlockEntityTypeBuilder.create(TowerBlockEntity::new, ModBlocks.TOWER_BASE).build());
    public static final BlockEntityType<AntennaBlockEntity> ANTENNA = register("antenna", FabricBlockEntityTypeBuilder.create(AntennaBlockEntity::new, ModBlocks.ANTENNA).build());
    public static final BlockEntityType<NfcReaderBlockEntity> NFC_READER = register("nfc_reader", FabricBlockEntityTypeBuilder.create(NfcReaderBlockEntity::new, ModBlocks.NFC_READER).build());

    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(ClassicPeripherals.MOD_ID, path), blockEntityType);
    }

    public static void initialize() {
    }
}
