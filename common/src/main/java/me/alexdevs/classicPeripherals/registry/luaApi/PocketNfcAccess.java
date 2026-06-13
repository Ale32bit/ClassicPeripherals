package me.alexdevs.classicPeripherals.registry.luaApi;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.core.Crypto;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PocketNfcAccess {
    private static final ConcurrentHashMap<Integer, String> nfcData = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, String> nfcKeys = new ConcurrentHashMap<>();

    public static void set(int id, @Nullable String data) {
        if (data == null) {
            nfcData.remove(id);
            return;
        }

        var maxDataSize = ClassicPeripherals.CONFIG.nfcMaxDataSize;

        nfcData.put(id, data.substring(0, Math.min(data.length(), maxDataSize)));
    }

    public static void setPrivateKey(int id, @Nullable String privateKey) {
        if (privateKey == null) {
            nfcKeys.remove(id);
            return;
        }

        if (privateKey.length() != 32) {
            throw new IllegalArgumentException("Invalid private key length");
        }

        nfcKeys.put(id, privateKey);
    }

    public static Optional<String> get(int id) {
        return Optional.ofNullable(nfcData.getOrDefault(id, null));
    }

    public static Optional<String> getPrivateKey(int id) {
        if (!nfcKeys.containsKey(id)) {
            return Optional.empty();
        }

        var privateKey = nfcKeys.get(id);
        return Optional.of(Crypto.derivePublicKey(privateKey));
    }

    public static Optional<String> pop(int id) {
        var data = Optional.ofNullable(nfcData.getOrDefault(id, null));
        nfcData.remove(id);
        return data;
    }

    public static void clear() {
        nfcData.clear();
        nfcKeys.clear();
    }
}
