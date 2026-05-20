package me.alexdevs.classicPeripherals.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.core.Crypto;
import me.alexdevs.classicPeripherals.utils.LuaUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public abstract class AbstractCryptographicAcceleratorPeripheral implements IPeripheral {

    @Override
    public String getType() {
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
        var value = Crypto.secureRandom();

        if (args.count() == 0) { // 0.0 - 1.0
            return value;
        } else if (args.count() == 1) { // 0.0 - max
            var max = args.getInt(0);
            return Math.floor(value * max);
        } else { // min - max
            var min = args.getInt(0);
            var max = args.getInt(1);
            return Math.floor(value * max) + min;
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
        try {
            return Crypto.encryptAes(data, key, iv);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new LuaException(e.getMessage());
        }
    }

    @LuaFunction
    public final String decryptAes(String data, String key, String iv) throws LuaException {
        try {
            return Crypto.decryptAes(data, key, iv);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new LuaException(e.getMessage());
        }
    }

    @LuaFunction
    public final String generatePrivateKey() {
        return Crypto.generatePrivateKey();
    }

    @LuaFunction
    public final String derivePublicKey(String privateKey) throws LuaException {
        LuaUtils.validateKey(privateKey);
        return Crypto.derivePublicKey(privateKey);
    }

    @LuaFunction
    public final String sign(String message, String privateKey) throws LuaException {
        LuaUtils.validateKey(privateKey);
        return Crypto.sign(message, privateKey);
    }

    @LuaFunction
    public final boolean verify(String message, String signature, String publicKey) throws LuaException {
        LuaUtils.validateKey(publicKey);
        LuaUtils.validateSignature(signature);
        return Crypto.verify(message, signature, publicKey);
    }

    @LuaFunction
    public final String deriveEcdhPublicKey(String privateKey) throws LuaException {
        LuaUtils.validateKey(privateKey);
        return Crypto.deriveECDHPublicKey(privateKey);
    }

    @LuaFunction
    public final String computeSharedSecret(String privateKey, String peerPublicKey) throws LuaException {
        LuaUtils.validateKey(privateKey);
        LuaUtils.validateKey(peerPublicKey);
        return Crypto.computeSharedSecret(privateKey, peerPublicKey);
    }
}
