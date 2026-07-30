package me.alexdevs.classicPeripherals.peripherals.crypto;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.core.Compression;
import me.alexdevs.classicPeripherals.core.Crypto;
import me.alexdevs.classicPeripherals.utils.CryptoUtils;
import org.jspecify.annotations.NonNull;

import javax.crypto.BadPaddingException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.zip.DataFormatException;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public abstract class AbstractCryptographicAcceleratorPeripheral implements IPeripheral {
    public enum Blake3Modes {
        hash,
        keyed,
        derive,
    }

    @Override
    public @NonNull String getType() {
        return "cryptographic_accelerator";
    }

    @LuaFunction
    public final String md5(String data, Optional<Boolean> hex) {
        return Crypto.md5(data, hex.orElse(true));
    }

    @LuaFunction
    public final String sha1(String data, Optional<Boolean> hex) {
        return Crypto.sha1(data, hex.orElse(true));
    }

    @LuaFunction
    public final String sha256(String data, Optional<Boolean> hex) {
        return Crypto.sha256(data, hex.orElse(true));
    }

    @LuaFunction
    public final String sha512(String data, Optional<Boolean> hex) {
        return Crypto.sha512(data, hex.orElse(true));
    }

    @LuaFunction
    public final String blake3(IArguments args) throws LuaException {
        var data = args.getString(0);
        var hex = args.optBoolean(1, true);
        var mode = args.optEnum(2, Blake3Modes.class).orElse(Blake3Modes.hash);
        var outputSize = args.optInt(4, 32);

        if (outputSize <= 0) {
            throw new LuaException("output size must be greater than 0");
        }

        // for resource exhaustion reasons, we limit the output size to 4096.
        // it lags the thread a lot for very high values, and there is no reason to go higher so far.
        if (outputSize > 4096) {
            throw new LuaException("output size must be less than or equal to 4096");
        }

        return switch (mode) {
            case hash -> Crypto.blake3Hash(data, hex, outputSize);
            case keyed -> Crypto.blake3Keyed(data, CryptoUtils.assertKey(args.getString(3)), hex, outputSize);
            case derive -> Crypto.blake3Derive(data, args.getString(3), hex, outputSize);
        };
    }

    @LuaFunction
    public final String hmacMd5(String key, String data, Optional<Boolean> hex) {
        return Crypto.md5Hmac(key, data, hex.orElse(true));
    }

    @LuaFunction
    public final String hmacSha1(String key, String data, Optional<Boolean> hex) {
        return Crypto.sha1Hmac(key, data, hex.orElse(true));
    }

    @LuaFunction
    public final String hmacSha256(String key, String data, Optional<Boolean> hex) {
        return Crypto.sha256Hmac(key, data, hex.orElse(true));
    }

    @LuaFunction
    public final String hmacSha512(String key, String data, Optional<Boolean> hex) {
        return Crypto.sha512Hmac(key, data, hex.orElse(true));
    }

    @LuaFunction
    public final double random(IArguments args) throws LuaException {
        if (args.count() == 0) { // 0.0 - 1.0
            return Crypto.secureRandom();
        } else if (args.count() == 1) { // 0.0 - max
            var max = args.getFiniteDouble(0);

            if (max < 0) {
                throw new LuaException("bad argument #1 (interval is empty)");
            }

            return Crypto.secureRandom(max);
        } else { // min - max
            var min = args.getFiniteDouble(0);
            var max = args.getFiniteDouble(1);

            if (max - min <= 0) {
                throw new LuaException("bad argument #1 (interval is empty)");
            }

            return Crypto.secureRandom(min, max);
        }
    }

    @LuaFunction
    public final String randomBytes(int length) throws LuaException {
        if (length > 0x7ffffff0) {
            throw new LuaException("buffer size limit exceeded");
        }

        var bytes = Crypto.secureRandomBuffer(length);

        return new String(bytes, StandardCharsets.ISO_8859_1);
    }

    @LuaFunction
    public final String encodeBase64(String data) {
        return Crypto.encodeBase64(data);
    }

    @LuaFunction
    public final String decodeBase64(String data) {
        try {
            return Crypto.decodeBase64(data);
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

    @LuaFunction
    public final String encryptAes(String data, String key, String iv) throws LuaException {
        CryptoUtils.assertLength(1, key.length(), 16, 24, 32);
        CryptoUtils.assertLength(2, iv.length(), 16);

        try {
            return Crypto.encryptAes(data, key, iv);
        } catch (BadPaddingException e) {
            throw new LuaException(e.getMessage());
        }
    }

    @LuaFunction
    public final String decryptAes(String data, String key, String iv) throws LuaException {
        CryptoUtils.assertLength(1, key.length(), 16, 24, 32);
        CryptoUtils.assertLength(2, iv.length(), 16);

        try {
            return Crypto.decryptAes(data, key, iv);
        } catch (BadPaddingException e) {
            throw new LuaException(e.getMessage());
        }
    }

    @LuaFunction
    public final String generatePrivateKey() {
        return Crypto.generatePrivateKey();
    }

    @LuaFunction
    public final String derivePublicKey(String privateKey) throws LuaException {
        CryptoUtils.assertKey(privateKey);
        return Crypto.derivePublicKey(privateKey);
    }

    @LuaFunction
    public final String sign(String message, String privateKey) throws LuaException {
        CryptoUtils.assertKey(privateKey);
        return Crypto.sign(message, privateKey);
    }

    @LuaFunction
    public final boolean verify(String message, String signature, String publicKey) throws LuaException {
        CryptoUtils.assertKey(publicKey);
        CryptoUtils.assertSignature(signature);
        return Crypto.verify(message, signature, publicKey);
    }

    @LuaFunction
    public final String deriveEcdhPublicKey(String privateKey) throws LuaException {
        CryptoUtils.assertKey(privateKey);
        return Crypto.deriveECDHPublicKey(privateKey);
    }

    @LuaFunction
    public final String computeSharedSecret(String privateKey, String peerPublicKey) throws LuaException {
        CryptoUtils.assertKey(privateKey);
        CryptoUtils.assertKey(peerPublicKey);
        return Crypto.computeSharedSecret(privateKey, peerPublicKey);
    }

    @LuaFunction
    public final String deflate(String data) {
        return Compression.deflate(data);
    }

    @LuaFunction
    public final String inflate(String data) throws LuaException {
        try {
            return Compression.inflate(data);
        } catch (DataFormatException e) {
            throw new LuaException("invalid compressed data format");
        }
    }
}
