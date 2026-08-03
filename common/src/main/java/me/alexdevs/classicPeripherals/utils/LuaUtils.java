package me.alexdevs.classicPeripherals.utils;

import dan200.computercraft.api.lua.LuaException;

public class LuaUtils {
    public static int assertRange(int index, int value, int min, int max) throws LuaException {
        if (value < min || value > max) {
            throw new LuaException("bad argument #" + (index + 1) + " (expected between " + min + " and " + max + ", got " + value + ")");
        }
        return value;
    }
}
