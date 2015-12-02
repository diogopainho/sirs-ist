package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.util.Base64;
import android.util.Log;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.security.KeyPair;
import java.util.Arrays;

import javax.crypto.Cipher;

import pt.ulisboa.tecnico.meic.sirs.securesms.Crypto.AsymCrypto;
import pt.ulisboa.tecnico.meic.sirs.securesms.Crypto.KeyHelper;

/**
 * Created by ruimams on 28/11/2015.
 */
public class CryptoTest extends TestCase {
    protected KeyPair source;
    protected KeyPair destination;
    protected final String plainText = "hello world";

    protected void setUp() {
        this.source = KeyHelper.generateKeyPair();
        this.destination = KeyHelper.generateKeyPair();
    }

    public void testHardcoded() {
        try {
            Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, this.source.getPublic());
            byte[] cipheredBytes = encryptCipher.doFinal(plainText.getBytes("UTF-8"));

            String cipheredText = Base64.encodeToString(cipheredBytes, Base64.DEFAULT);
            Log.d("CryptoTest", "testHardcoded: cipheredText: " + cipheredText);

            byte[] cipheredBytes2 = Base64.decode(cipheredText, Base64.DEFAULT);

            Assert.assertTrue(Arrays.equals(cipheredBytes, cipheredBytes2));

            Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, this.source.getPrivate());
            byte[] decryptedBytes = decryptCipher.doFinal(cipheredBytes2);
            String decryptedText = new String(decryptedBytes, "UTF-8");

            Assert.assertEquals(plainText, decryptedText);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    public void testSignatureVerification() {
        try {
            byte[] content = plainText.getBytes();

            byte[] signature = AsymCrypto.sign(content, source.getPrivate());
            boolean validSignature = AsymCrypto.verify(content, signature, source.getPublic());

            Assert.assertTrue(validSignature);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    public void testSimpleCrypto() {
        try {
            byte[] plainBytes = plainText.getBytes();

            byte[] cipheredBytes = AsymCrypto.encrypt(plainBytes, source.getPublic());
            byte[] uncipheredBytes = AsymCrypto.decrypt(cipheredBytes, source.getPrivate());

            Assert.assertTrue(Arrays.equals(plainBytes, uncipheredBytes));
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
