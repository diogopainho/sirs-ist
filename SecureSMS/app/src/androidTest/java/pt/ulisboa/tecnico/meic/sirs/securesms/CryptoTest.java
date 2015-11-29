package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.util.Base64;
import android.util.Log;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.Cipher;

import pt.ulisboa.tecnico.meic.sirs.securesms.FirstLoginActivity;

/**
 * Created by ruimams on 28/11/2015.
 */
public class CryptoTest extends TestCase {
    protected KeyPair source;
    protected KeyPair destination;
    protected final String plainText = "hello world";

    protected void setUp() {
        this.source = CryptoHelper.generateKeyPair();
        this.destination = CryptoHelper.generateKeyPair();
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

            byte[] signature = CryptoHelper.sign(content, source.getPrivate());
            boolean validSignature = CryptoHelper.verify(content, signature, source.getPublic());

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

            byte[] cipheredBytes = CryptoHelper.encrypt(plainBytes, source.getPublic());
            byte[] uncipheredBytes = CryptoHelper.decrypt(cipheredBytes, source.getPrivate());

            Assert.assertTrue(Arrays.equals(plainBytes, uncipheredBytes));
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    public void testCryptoImplementationInMethods() {
        KeyPair me;
        KeyPair other;

        try {
            // Sending side
            me = source;
            other = destination;
            byte[] plainBytes = plainText.getBytes();
            PlainMessage plainMessage = new PlainMessage(plainBytes);
            SecureMessage secureMessage = new SecureMessage(plainMessage, other.getPublic(), me.getPrivate());

            // Receiving side
            me = destination;
            other = source;
            PlainMessage receivedPlainMessage = secureMessage.toPlain(me.getPrivate(), other.getPublic());

            Assert.assertNotNull(receivedPlainMessage);
            Assert.assertTrue(Arrays.equals(plainBytes, receivedPlainMessage.getContent()));
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    public void testSecureMessageParse() {
        KeyPair me;
        KeyPair other;

        try {
            // Sending side
            me = source;
            other = destination;
            byte[] plainBytes = plainText.getBytes();
            PlainMessage plainMessage = new PlainMessage(plainBytes);
            SecureMessage secureMessage = new SecureMessage(plainMessage, other.getPublic(), me.getPrivate());

            // Sent via communication channel
            byte[] sentBytes = secureMessage.doFinal();

            // Receiving side
            SecureMessage receivedSecureMessage = SecureMessage.parse(sentBytes);

            Assert.assertTrue(secureMessage.compare(receivedSecureMessage));
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    public void testAsymmetricCryptographyBytesOnly() {
        KeyPair me;
        KeyPair other;

        try {
            // Sending side
            me = source;
            other = destination;
            byte[] plainBytes = plainText.getBytes();
            PlainMessage plainMessage = new PlainMessage(plainBytes);
            SecureMessage secureMessage = new SecureMessage(plainMessage, other.getPublic(), me.getPrivate());

            // Sent via communication channel
            byte[] sentBytes = secureMessage.doFinal();

            // Receiving side
            me = destination;
            other = source;
            SecureMessage receivedSecureMessage = SecureMessage.parse(sentBytes);
            PlainMessage receivedPlainMessage = receivedSecureMessage.toPlain(me.getPrivate(), other.getPublic());

            Assert.assertNotNull(receivedPlainMessage);
            Assert.assertTrue(Arrays.equals(plainBytes, receivedPlainMessage.getContent()));
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    public void testAsymmetricCryptographyBase64() {
        KeyPair me;
        KeyPair other;

        try {
            // Sending side
            me = source;
            other = destination;
            byte[] plainBytes = plainText.getBytes();
            PlainMessage plainMessage = new PlainMessage(plainBytes);
            SecureMessage secureMessage = new SecureMessage(plainMessage, other.getPublic(), me.getPrivate());
            byte[] cipheredBytes = secureMessage.doFinal();

            // Sent via communication channel
            String cipheredText = Base64.encodeToString(cipheredBytes, Base64.DEFAULT);
            Log.d("CryptoTest", "testAsymmetricCryptographyBase64: cipheredText: " + cipheredText);

            // Receiving side
            me = destination;
            other = source;
            byte[] receivedCipheredBytes = Base64.decode(cipheredText, Base64.DEFAULT);
            SecureMessage receivedSecureMessage = SecureMessage.parse(receivedCipheredBytes);
            PlainMessage receivedPlainMessage = receivedSecureMessage.toPlain(me.getPrivate(), other.getPublic());

            Assert.assertNotNull(receivedPlainMessage);
            Assert.assertTrue(Arrays.equals(plainBytes, receivedPlainMessage.getContent()));
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
