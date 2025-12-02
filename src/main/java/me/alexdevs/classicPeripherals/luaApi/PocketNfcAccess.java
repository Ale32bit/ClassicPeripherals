package me.alexdevs.classicPeripherals.luaApi;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PocketNfcAccess {
    private static final ConcurrentHashMap<Integer, String> nfcData = new ConcurrentHashMap<>();

    public static void set(int id, @Nullable String data) {
        if (data == null) {
            nfcData.remove(id);
            return;
        }

        var maxDataSize = ClassicPeripherals.CONFIG.nfcMaxDataSize;

        nfcData.put(id, data.substring(0, Math.min(data.length(), maxDataSize)));
    }

    public static Optional<String> get(int id) {
        return Optional.ofNullable(nfcData.getOrDefault(id, null));
    }

    public static Optional<String> pop(int id) {
        var data = Optional.ofNullable(nfcData.getOrDefault(id, null));
        nfcData.remove(id);
        return data;
    }

    public static void clear() {
        nfcData.clear();
    }
}
