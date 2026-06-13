package me.alexdevs.classicPeripherals.utils;

import dan200.computercraft.api.lua.LuaException;

public class CryptoUtils {
    public static void validateKey(String key) throws LuaException {
        if (key != null && key.length() != 32) {
            throw new LuaException("Invalid key length, expected 32 bytes.");
        }
    }

    public static void validateSignature(String key) throws LuaException {
        if (key != null && key.length() != 64) {
            throw new LuaException("Invalid signature length, expected 64 bytes.");
        }
    }
}
