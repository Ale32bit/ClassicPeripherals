package me.alexdevs.classicPeripherals.core;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.jspecify.annotations.NonNull;

import javax.crypto.BadPaddingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

public class Crypto {
    private static final Charset CHARSET = StandardCharsets.ISO_8859_1;

    public static String toStr(byte[] bytes, boolean hex) {
        if (hex) {
            return HexFormat.of().formatHex(bytes);
        }

        return new String(bytes, CHARSET);
    }

    private static String digest(Digest digest, String data, boolean hex) {
        digest.update(data.getBytes(CHARSET), 0, data.length());
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return toStr(hash, hex);
    }

    public static String md5(String data, boolean hex) {
        return digest(
                new MD5Digest(),
                data, hex
        );
    }

    public static String sha1(String data, boolean hex) {
        return digest(
                new SHA1Digest(),
                data, hex
        );
    }

    public static String sha256(String data, boolean hex) {
        return digest(
                new SHA256Digest(),
                data, hex
        );
    }

    public static String sha512(String data, boolean hex) {
        return digest(
                new SHA512Digest(),
                data, hex
        );
    }

    private static String hmac(Digest digest, String key, String data, boolean hex) {
        var hmac = new HMac(digest);
        hmac.init(new KeyParameter(key.getBytes(CHARSET)));
        hmac.update(data.getBytes(CHARSET), 0, key.length());
        byte[] hash = new byte[hmac.getMacSize()];
        hmac.doFinal(hash, 0);
        return toStr(hash, hex);
    }

    public static String md5Hmac(String key, String data, boolean hex) {
        return hmac(
                new MD5Digest(),
                key, data, hex
        );
    }

    public static String sha1Hmac(String key, String data, boolean hex) {
        return hmac(
                new SHA1Digest(),
                key, data, hex
        );
    }

    public static String sha256Hmac(String key, String data, boolean hex) {
        return hmac(
                new SHA256Digest(),
                key, data, hex
        );
    }

    public static String sha512Hmac(String key, String data, boolean hex) {
        return hmac(
                new SHA512Digest(),
                key, data, hex
        );
    }

    public static double secureRandom() {
        var random = new SecureRandom();
        return random.nextDouble();
    }

    public static double secureRandom(double max) {
        var random = new SecureRandom();
        if (max == 0d) {
            // mimics the behavior of the Lua random function
            return random.nextDouble(Long.MIN_VALUE, Long.MAX_VALUE);
        }
        return random.nextDouble(max);
    }

    public static double secureRandom(double min, double max) {
        var random = new SecureRandom();
        return random.nextDouble(min, max);
    }

    public static byte[] secureRandomBuffer(int length) {
        var random = new SecureRandom();
        var bytes = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }

    public static String generatePrivateKey() {
        var seed = secureRandomBuffer(32);
        return toStr(seed, false);
    }

    public static String derivePublicKey(String key) {
        byte[] seed = key.getBytes(CHARSET);

        var privateKey = new Ed25519PrivateKeyParameters(seed, 0);

        Ed25519PublicKeyParameters publicKey = privateKey.generatePublicKey();

        return toStr(publicKey.getEncoded(), false);
    }

    public static String sign(String message, String key) {
        byte[] seed = key.getBytes(CHARSET);
        byte[] msg = message.getBytes(CHARSET);

        var privateKey = new Ed25519PrivateKeyParameters(seed, 0);

        var signer = new Ed25519Signer();
        signer.init(true, privateKey);
        signer.update(msg, 0, msg.length);

        byte[] signature = signer.generateSignature();
        return toStr(signature, false);
    }

    public static boolean verify(
            String message,
            String signature,
            String publicKey) {

        byte[] msg = message.getBytes(CHARSET);
        byte[] sig = signature.getBytes(CHARSET);
        byte[] pub = publicKey.getBytes(CHARSET);

        var key = new Ed25519PublicKeyParameters(pub, 0);

        Ed25519Signer verifier = new Ed25519Signer();
        verifier.init(false, key);
        verifier.update(msg, 0, msg.length);

        return verifier.verifySignature(sig);
    }

    public static String deriveECDHPublicKey(String privateKey) {
        byte[] seed = privateKey.getBytes(CHARSET);

        var privKey = new X25519PrivateKeyParameters(seed, 0);
        var pubKey = privKey.generatePublicKey();

        return toStr(pubKey.getEncoded(), false);
    }

    public static String computeSharedSecret(String privateKey, String peerPublicKey) {
        byte[] privBytes = privateKey.getBytes(CHARSET);
        byte[] pubBytes = peerPublicKey.getBytes(CHARSET);

        var priv = new X25519PrivateKeyParameters(privBytes, 0);
        var pub = new X25519PublicKeyParameters(pubBytes, 0);

        byte[] secret = new byte[32];
        priv.generateSecret(pub, secret, 0);

        return toStr(secret, false);
    }

    public static String encodeBase64(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes(CHARSET));
    }

    public static String decodeBase64(String data) {
        return new String(Base64.getDecoder().decode(data), CHARSET);
    }

    public static String encryptAes(String data, String key, String iv) throws BadPaddingException {
        var keyBytes = key.getBytes(CHARSET);
        var ivBytes = iv.getBytes(CHARSET);
        var dataBytes = data.getBytes(CHARSET);

        var cipher = new PaddedBufferedBlockCipher(CBCBlockCipher.newInstance(AESEngine.newInstance()), new PKCS7Padding());
        cipher.init(true, new ParametersWithIV(new KeyParameter(keyBytes), ivBytes));

        return getString(dataBytes, cipher);
    }

    public static String decryptAes(String data, String key, String iv) throws BadPaddingException {
        var keyBytes = key.getBytes(CHARSET);
        var ivBytes = iv.getBytes(CHARSET);
        var dataBytes = data.getBytes(CHARSET);

        var cipher = new PaddedBufferedBlockCipher(CBCBlockCipher.newInstance(AESEngine.newInstance()), new PKCS7Padding());
        cipher.init(false, new ParametersWithIV(new KeyParameter(keyBytes), ivBytes));

        return getString(dataBytes, cipher);
    }

    @NonNull
    private static String getString(byte[] dataBytes, PaddedBufferedBlockCipher cipher) throws BadPaddingException {
        var output = new byte[cipher.getOutputSize(dataBytes.length)];
        int len = cipher.processBytes(dataBytes, 0, dataBytes.length, output, 0);
        try {
            len += cipher.doFinal(output, len);
        } catch (InvalidCipherTextException e) {
            throw new BadPaddingException(e.getMessage());
        }

        return new String(output, 0, len, CHARSET);
    }

    private Crypto() {
    }
}
