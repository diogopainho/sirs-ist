package pt.ulisboa.tecnico.meic.sirs.securesms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


public class SymCrypto {
    private static final String SYMMETRIC_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 16;

    public static byte[] encrypt(byte[] plainBytes, SecretKey key) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, IOException, InvalidAlgorithmParameterException,
            InvalidParameterSpecException {

        Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] iv = cipher.getIV();
        byte[] cipheredBytes = cipher.doFinal(plainBytes);

        ByteArrayOutputStream cipheredComposition = new ByteArrayOutputStream();
        cipheredComposition.write(iv);
        cipheredComposition.write(cipheredBytes);

        return cipheredComposition.toByteArray();
    }

    public static byte[] decrypt(byte[] cipheredComposition, SecretKey key) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, IOException, InvalidAlgorithmParameterException,
            InvalidParameterSpecException {

        byte[] iv = Arrays.copyOfRange(cipheredComposition, 0, IV_LENGTH);
        byte[] cipheredData = Arrays.copyOfRange(cipheredComposition, IV_LENGTH, cipheredComposition.length);

        IvParameterSpec ivParameter = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameter);
        return cipher.doFinal(cipheredData);
    }
}
