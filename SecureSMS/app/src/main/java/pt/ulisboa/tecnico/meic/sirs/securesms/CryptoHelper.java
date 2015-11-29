package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.util.Base64;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by ruimams on 28/11/2015.
 */
public class CryptoHelper {
    private static byte[] crypto(int opmode, byte[] input, Key key) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException {

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(opmode, key);

        return cipher.doFinal(input);
    }


    public static byte[] encrypt(byte[] plainBytes, Key key) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException {

        return crypto(Cipher.ENCRYPT_MODE, plainBytes, key);
    }

    public static byte[] encrypt(String plainText, Key key) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException {

        byte[] plainBytes = plainText.getBytes();
        return encrypt(plainBytes, key);
    }

    public static byte[] decrypt(byte[] cipheredBytes, Key key) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException {

        return crypto(Cipher.DECRYPT_MODE, cipheredBytes, key);
    }

    public static byte[] decrypt(String cipheredText, Key key) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException {

        byte[] cipheredBytes = cipheredText.getBytes();
        return decrypt(cipheredBytes, key);
    }


    public static byte[] hash(byte[] content) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(content);

        return md.digest();
    }

    public static byte[] sign(byte[] content, Key signingKey) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException {

        return encrypt(hash(content), signingKey);
    }

    public static boolean verify(byte[] content, byte[] signature, Key verificationKey) throws
            InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {

        return Arrays.equals(hash(content), decrypt(signature, verificationKey));
    }


    public static String encryptToBase64(String plainText, Key key) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException {

        return Base64.encodeToString(encrypt(plainText, key), Base64.DEFAULT);
    }

    public static String decryptFromBase64(String cipheredText, Key key) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException {

        byte[] cipheredBytes = Base64.decode(cipheredText, Base64.DEFAULT);
        return new String(decrypt(cipheredBytes, key));
    }


    public static KeyPair generateKeyPair() {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }
}
