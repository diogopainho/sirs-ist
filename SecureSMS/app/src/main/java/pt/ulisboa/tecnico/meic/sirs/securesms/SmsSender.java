package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.telephony.SmsManager;

import java.util.ArrayList;

public class SmsSender {
    public static void send(String phoneNumber, String message) {
        if (!phoneNumber.startsWith("+351")) {
            phoneNumber = "+351" + phoneNumber;
        }

        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> multiPartSms = smsManager.divideMessage(message);

        smsManager.sendMultipartTextMessage(phoneNumber,
                null,
                multiPartSms,
                null,
                null);


    }
}
