package me.alexdevs.ccNetworks.client;

import me.alexdevs.ccNetworks.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public class CcNetworksClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.TOWER_BASE, ModBlocks.TOWER_SEGMENT, ModBlocks.TOWER_HEAD, ModBlocks.ANTENNA);
    }
}
