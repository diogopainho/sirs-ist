package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by ruimams on 29/11/2015.
 */
public class SecureMessage {
    private static final int INTEGRITY_LENGTH = 128;
    private byte[] cipheredContent;
    private byte[] integrityPart; // TODO: Rename?

    public SecureMessage(byte[] cipheredContent, byte[] integrityPart) {
        this.cipheredContent = cipheredContent;
        this.integrityPart = integrityPart;
    }

    public SecureMessage(PlainMessage plainMessage, Key confidentialityKey, Key signingKey) throws
            InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {

        this.cipheredContent = CryptoHelper.encrypt(plainMessage.getContent(), confidentialityKey);
        this.integrityPart = CryptoHelper.sign(cipheredContent, signingKey); // TODO: Rename var?
    }

    public PlainMessage toPlain(Key confidentialityKey, Key verificationKey) throws
            InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException{

        PlainMessage plainMessage = null;

        if (CryptoHelper.verify(cipheredContent, integrityPart, verificationKey)) {
            plainMessage = new PlainMessage(CryptoHelper.decrypt(cipheredContent, confidentialityKey));
        }

        return plainMessage;
    }


    public byte[] doFinal() throws IOException { // TODO: Rename method?
        ByteArrayOutputStream finalMessage = new ByteArrayOutputStream();

        finalMessage.write(integrityPart);
        finalMessage.write(cipheredContent);

        return finalMessage.toByteArray();
    }

    public static SecureMessage parse(byte[] finalMessage) {
        return new SecureMessage(
                Arrays.copyOfRange(finalMessage, INTEGRITY_LENGTH, finalMessage.length),
                Arrays.copyOfRange(finalMessage, 0, INTEGRITY_LENGTH));
    }

    /**
     * Compares two instances of SecureMessage.
     * @param other
     * @return true if the two instances are equal, false if they're different
     */
    public boolean compare(SecureMessage other) {
        return Arrays.equals(this.cipheredContent, other.cipheredContent) &&
                Arrays.equals(this.integrityPart, other.integrityPart);
    }
}
