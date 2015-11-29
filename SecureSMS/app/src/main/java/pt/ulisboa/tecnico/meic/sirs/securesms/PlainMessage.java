package pt.ulisboa.tecnico.meic.sirs.securesms;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by ruimams on 29/11/2015.
 */
public class PlainMessage {
    private byte[] content;

    public PlainMessage(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return this.content;
    }
}
