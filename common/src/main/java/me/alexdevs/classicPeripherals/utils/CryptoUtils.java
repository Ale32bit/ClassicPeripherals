package me.alexdevs.classicPeripherals.utils;

import dan200.computercraft.api.lua.LuaException;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CryptoUtils {
    public static String assertKey(String key) throws LuaException {
        if (key != null && key.length() != 32) {
            throw new LuaException("Invalid key length, expected 32 bytes.");
        }
        return key;
    }

    public static String assertSignature(String key) throws LuaException {
        if (key != null && key.length() != 64) {
            throw new LuaException("Invalid signature length, expected 64 bytes.");
        }
        return key;
    }

    public static int assertLength(int index, int value, int... lengths) throws LuaException {
        for (int len : lengths) {
            if (value == len) {
                return value;
            }
        }

        var expectedValues = IntStream.of(lengths)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(", "));

        throw new LuaException("bad argument length #" + (index + 1) + " (expected " + expectedValues + ", got " + value + ")");
    }
}
