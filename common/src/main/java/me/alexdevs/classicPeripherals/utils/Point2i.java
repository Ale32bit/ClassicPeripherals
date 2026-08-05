package me.alexdevs.classicPeripherals.utils;

import net.minecraft.core.BlockPos;

public record Point2i(int x, int z) {
    public static Point2i of(BlockPos pos) {
        return new Point2i(pos.getX(), pos.getZ());
    }
}
