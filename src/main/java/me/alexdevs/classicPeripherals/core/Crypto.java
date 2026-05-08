package me.alexdevs.classicPeripherals.core;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.HexFormat;

public class Crypto {
    private static final Charset CHARSET = StandardCharsets.ISO_8859_1;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    static {
        // Register Bouncy Castle as a security provider
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String toStr(byte[] bytes, boolean hex) {
        if (hex) {
            return HexFormat.of().formatHex(bytes);
        }

        return new String(bytes, CHARSET);
    }

    public static String toStr(byte[] bytes) {
        return toStr(bytes, true);
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
        hmac.update(data.getBytes(CHARSET), 0, data.length());
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
        return SECURE_RANDOM.nextDouble();
    }

    public static byte[] secureRandomBuffer(int length) {
        var bytes = new byte[length];
        SECURE_RANDOM.nextBytes(bytes);
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

    public static String encodeBase64(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes(CHARSET));
    }

    public static String decodeBase64(String data) {
        return new String(Base64.getDecoder().decode(data), CHARSET);
    }

    public static String encryptAes(String data, String key, String iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        var keyBytes = key.getBytes(CHARSET);
        var ivBytes = iv.getBytes(CHARSET);
        var dataBytes = data.getBytes(CHARSET);

        var secretKey = new SecretKeySpec(keyBytes, "AES");
        var ivSpec = new IvParameterSpec(ivBytes);
        var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        var encryptedBytes = cipher.doFinal(dataBytes);

        return new String(encryptedBytes, CHARSET);
    }

    public static String decryptAes(String data, String key, String iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        var keyBytes = key.getBytes(CHARSET);
        var ivBytes = iv.getBytes(CHARSET);
        var dataBytes = data.getBytes(CHARSET);

        var secretKey = new SecretKeySpec(keyBytes, "AES");
        var ivSpec = new IvParameterSpec(ivBytes);
        var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        var encryptedBytes = cipher.doFinal(dataBytes);

        return new String(encryptedBytes, CHARSET);
    }

    private Crypto() {
    }
}
