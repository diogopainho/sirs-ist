package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.util.Base64;

/**
 * Created by ruimams on 30/11/2015.
 */
public class SmsEncoding {
    public static String encode(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static byte[] decode(String encodedText) {
        return Base64.decode(encodedText, Base64.DEFAULT);
    }
}
