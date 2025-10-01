package me.alexdevs.classicPeripherals.client;

import me.alexdevs.classicPeripherals.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public class ClassicPeripheralsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.TOWER_BASE, ModBlocks.TOWER_SEGMENT, ModBlocks.TOWER_HEAD, ModBlocks.ANTENNA);
    }
}
