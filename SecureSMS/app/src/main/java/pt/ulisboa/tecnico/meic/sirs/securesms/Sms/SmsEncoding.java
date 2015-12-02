package pt.ulisboa.tecnico.meic.sirs.securesms.Sms;

import android.util.Base64;

import pt.ulisboa.tecnico.meic.sirs.securesms.MessageExchange.BinaryTextEncoding;

public class SmsEncoding implements BinaryTextEncoding {
    public String encode(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public byte[] decode(String encodedText) {
        return Base64.decode(encodedText, Base64.DEFAULT);
    }
}
