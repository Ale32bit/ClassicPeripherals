package me.alexdevs.classicPeripherals.client;

import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.RenderType;

public class ClassicPeripheralsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.TOWER_BASE, ModBlocks.TOWER_SEGMENT, ModBlocks.TOWER_HEAD, ModBlocks.ANTENNA);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 0) {
                var tag = stack.getOrCreateTag();
                if (tag.contains("color")) {
                    return tag.getInt("color");
                }
            }

            return 0xFFFFFF;
        }, ModItems.NFC_CARD);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                var tag = stack.getOrCreateTag();
                if (tag.contains("color")) {
                    return tag.getInt("color");
                }
            }

            return 0xFFFFFF;
        }, ModItems.RFID_BADGE);
    }
}
