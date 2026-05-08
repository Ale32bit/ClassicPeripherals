package me.alexdevs.classicPeripherals.utils;

import dan200.computercraft.api.lua.LuaException;

public class LuaUtils {
    public static void validateKey(String privateKey) throws LuaException {
        if (privateKey != null && privateKey.length() != 32) {
            throw new LuaException("Invalid key length, expected 32 bytes.");
        }
    }
}
