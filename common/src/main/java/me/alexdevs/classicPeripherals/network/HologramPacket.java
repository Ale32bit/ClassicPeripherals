package me.alexdevs.classicPeripherals.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record HologramPacket(BlockPos pos, int[] data) implements CustomPacketPayload {
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }
}
