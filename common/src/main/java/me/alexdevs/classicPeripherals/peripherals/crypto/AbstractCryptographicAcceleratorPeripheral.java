package me.alexdevs.classicPeripherals.peripherals.crypto;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.core.Crypto;
import me.alexdevs.classicPeripherals.utils.CryptoUtils;
import org.jspecify.annotations.NonNull;

import javax.crypto.BadPaddingException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public abstract class AbstractCryptographicAcceleratorPeripheral implements IPeripheral {

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
        return Crypto.decodeBase64(data);
    }

    @LuaFunction
    public final String encryptAes(String data, String key, String iv) throws LuaException {
        CryptoUtils.validateLength(1, key.length(), 16, 24, 32);
        CryptoUtils.validateLength(2, iv.length(), 16);

        try {
            return Crypto.encryptAes(data, key, iv);
        } catch (BadPaddingException e) {
            throw new LuaException(e.getMessage());
        }
    }

    @LuaFunction
    public final String decryptAes(String data, String key, String iv) throws LuaException {
        CryptoUtils.validateLength(1, key.length(), 16, 24, 32);
        CryptoUtils.validateLength(2, iv.length(), 16);
        
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
        CryptoUtils.validateKey(privateKey);
        return Crypto.derivePublicKey(privateKey);
    }

    @LuaFunction
    public final String sign(String message, String privateKey) throws LuaException {
        CryptoUtils.validateKey(privateKey);
        return Crypto.sign(message, privateKey);
    }

    @LuaFunction
    public final boolean verify(String message, String signature, String publicKey) throws LuaException {
        CryptoUtils.validateKey(publicKey);
        CryptoUtils.validateSignature(signature);
        return Crypto.verify(message, signature, publicKey);
    }

    @LuaFunction
    public final String deriveEcdhPublicKey(String privateKey) throws LuaException {
        CryptoUtils.validateKey(privateKey);
        return Crypto.deriveECDHPublicKey(privateKey);
    }

    @LuaFunction
    public final String computeSharedSecret(String privateKey, String peerPublicKey) throws LuaException {
        CryptoUtils.validateKey(privateKey);
        CryptoUtils.validateKey(peerPublicKey);
        return Crypto.computeSharedSecret(privateKey, peerPublicKey);
    }
}
