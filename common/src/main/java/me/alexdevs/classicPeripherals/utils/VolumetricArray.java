package me.alexdevs.classicPeripherals.utils;

import net.minecraft.core.Vec3i;

import java.util.Arrays;
import java.util.Objects;

public class VolumetricArray {
    private final int[] array;
    private final int size;

    public VolumetricArray(int size) {
        this.size = size;
        array = new int[size * size * size];
    }

    private int index(int x, int y, int z) {
        Objects.checkIndex(x,size); Objects.checkIndex(y,size); Objects.checkIndex(z,size);
        return (z * size * size) + (y * size) + x;
    }

    private int index(Vec3i vec) {
        return index(vec.getX(), vec.getY(), vec.getZ());
    }

    private Vec3i vec(int idx) {
        final int z = idx / (size * size);
        idx -= (z * size * size);
        final int y = idx / size;
        final int x = idx % size;
        return new Vec3i(x, y, z);
    }

    public int get(int x, int y, int z) {
        return array[index(x, y, z)];
    }

    public int get(Vec3i vec) {
        return array[index(vec)];
    }

    public void set(int x, int y, int z, int value) {
        array[index(x, y, z)] = value;
    }

    public void set(Vec3i vec, int value) {
        array[index(vec)] = value;
    }

    public void clear() {
        Arrays.fill(array, 0);
    }
}
