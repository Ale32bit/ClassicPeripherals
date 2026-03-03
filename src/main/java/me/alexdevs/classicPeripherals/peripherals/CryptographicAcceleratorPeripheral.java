package me.alexdevs.classicPeripherals.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.core.Crypto;
import me.alexdevs.classicPeripherals.tiles.CryptographicAcceleratorBlockEntity;
import org.jspecify.annotations.Nullable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class CryptographicAcceleratorPeripheral implements IPeripheral {
    private final CryptographicAcceleratorBlockEntity cryptographicAccelerator;

    private final Crypto crypto = new Crypto();

    private final AttachedComputerSet computers = new AttachedComputerSet();

    public CryptographicAcceleratorPeripheral(CryptographicAcceleratorBlockEntity blockEntity) {
        this.cryptographicAccelerator = blockEntity;
    }

    @Override
    public String getType() {
        return "cryptographic_accelerator";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof CryptographicAcceleratorPeripheral o && cryptographicAccelerator == o.cryptographicAccelerator;
    }

    @Override
    public void attach(IComputerAccess computer) {
        computers.add(computer);
    }

    @Override
    public void detach(IComputerAccess computer) {
        computers.remove(computer);
    }

    @LuaFunction
    public final String md5(String data, Optional<Boolean> hex) {
        return crypto.md5(data, hex.orElse(true));
    }

    @LuaFunction
    public final String sha1(String data, Optional<Boolean> hex) {
        return crypto.sha1(data, hex.orElse(true));
    }

    @LuaFunction
    public final String sha256(String data, Optional<Boolean> hex) {
        return crypto.sha256(data, hex.orElse(true));
    }

    @LuaFunction
    public final String sha512(String data, Optional<Boolean> hex) {
        return crypto.sha512(data, hex.orElse(true));
    }

    @LuaFunction
    public final String hmacMd5(String key, String data, Optional<Boolean> hex) {
        return crypto.md5Hmac(key, data, hex.orElse(true));
    }

    @LuaFunction
    public final String hmacSha1(String key, String data, Optional<Boolean> hex) {
        return crypto.sha1Hmac(key, data, hex.orElse(true));
    }

    @LuaFunction
    public final String hmacSha256(String key, String data, Optional<Boolean> hex) {
        return crypto.sha256Hmac(key, data, hex.orElse(true));
    }

    @LuaFunction
    public final String hmacSha512(String key, String data, Optional<Boolean> hex) {
        return crypto.sha512Hmac(key, data, hex.orElse(true));
    }

    @LuaFunction
    public final double random(IArguments args) throws LuaException {
        var value = crypto.secureRandom();

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
        if(length > 0x7ffffff0) {
            throw new LuaException("buffer size limit exceeded");
        }

        var bytes = crypto.secureRandomBuffer(length);

        return new String(bytes, StandardCharsets.ISO_8859_1);
    }

    @LuaFunction
    public final String encodeBase64(String data) {
        return crypto.encodeBase64(data);
    }

    @LuaFunction
    public final String decodeBase64(String data) {
        return crypto.decodeBase64(data);
    }

    @LuaFunction
    public final String encryptAes(String data, String key, String iv) throws LuaException {
        try {
            return crypto.encryptAes(data, key, iv);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new LuaException(e.getMessage());
        }
    }

    @LuaFunction
    public final String decryptAes(String data, String key, String iv) throws LuaException {
        try {
            return crypto.decryptAes(data, key, iv);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new LuaException(e.getMessage());
        }
    }

    @LuaFunction
    public final String generatePrivateKey() {
        return crypto.generatePrivateKey();
    }

    @LuaFunction
    public final String derivePublicKey(String privateKey) {
        return crypto.derivePublicKey(privateKey);
    }

    @LuaFunction
    public final String sign(String message, String privateKey) {
        return crypto.sign(message, privateKey);
    }

    @LuaFunction
    public final boolean verify(String message, String signature, String publicKey) {
        return crypto.verify(message, signature, publicKey);
    }
}
