package pt.ulisboa.tecnico.meic.sirs.securesms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class SecureMessage {
    private static final int INTEGRITY_LENGTH = 128;
    private byte[] cipheredContent;
    private byte[] integrityPart; // TODO: Rename?

    public SecureMessage(byte[] cipheredContent, byte[] integrityPart) {
        this.cipheredContent = cipheredContent;
        this.integrityPart = integrityPart;
    }

    public SecureMessage(PlainMessage plainMessage, Key confidentialityKey, PrivateKey signingKey) throws
            InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {

        this.cipheredContent = AsymCrypto.encrypt(plainMessage.getContent(), confidentialityKey);
        this.integrityPart = AsymCrypto.sign(cipheredContent, signingKey); // TODO: Rename var?
    }

    public PlainMessage toPlain(Key confidentialityKey, PublicKey verificationKey) throws
            InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException{

        PlainMessage plainMessage = null;

        if (AsymCrypto.verify(cipheredContent, integrityPart, verificationKey)) {
            plainMessage = new PlainMessage(AsymCrypto.decrypt(cipheredContent, confidentialityKey));
        }

        return plainMessage;
    }


    public byte[] doFinal() throws IOException { // TODO: Rename method?
        ByteArrayOutputStream finalMessage = new ByteArrayOutputStream();

        finalMessage.write(this.integrityPart);
        finalMessage.write(this.cipheredContent);

        return finalMessage.toByteArray();
    }

    public static SecureMessage parse(byte[] finalMessage) {
        return new SecureMessage(
                Arrays.copyOfRange(finalMessage, INTEGRITY_LENGTH, finalMessage.length),
                Arrays.copyOfRange(finalMessage, 0, INTEGRITY_LENGTH));
    }

    public boolean compare(SecureMessage other) {
        return Arrays.equals(this.cipheredContent, other.cipheredContent) &&
                Arrays.equals(this.integrityPart, other.integrityPart);
    }
}
