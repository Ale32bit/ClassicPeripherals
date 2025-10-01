package me.alexdevs.ccNetworks;

import me.alexdevs.ccNetworks.block.ModBlocks;
import me.alexdevs.ccNetworks.item.ModItems;
import me.alexdevs.ccNetworks.peripherals.Peripherals;
import me.alexdevs.ccNetworks.tiles.ModBlockTiles;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CcNetworks implements ModInitializer {
    public static final String MOD_ID = "cc-networks";

    public static final ResourceKey<CreativeModeTab> CREATIVE_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(MOD_ID, "item_group"));
    public static final CreativeModeTab CREATIVE_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModBlocks.TOWER_HEAD))
            .title(Component.translatable("itemGroup.ccnetworks"))
            .build();

    @Override
    public void onInitialize() {
        ModBlocks.initialize();
        ModBlockTiles.initialize();
        ModItems.initialize();
        Peripherals.register();

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CREATIVE_TAB_KEY, CREATIVE_TAB);

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((tab, entries) -> {
            entries.accept(ModBlocks.TOWER_BASE);
            entries.accept(ModBlocks.TOWER_SEGMENT);
            entries.accept(ModBlocks.TOWER_HEAD);
            entries.accept(ModBlocks.ANTENNA);
            entries.accept(ModItems.COPPER_COIL);
        });
    }
}
