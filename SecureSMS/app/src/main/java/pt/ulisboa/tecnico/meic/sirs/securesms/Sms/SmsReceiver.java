package pt.ulisboa.tecnico.meic.sirs.securesms.Sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;


public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();
    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Sms received");
        String smsBody = "";
        String srcAddress = null;
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            Log.d(TAG, "sms.length=" + sms.length);
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                smsBody += smsMessage.getMessageBody();
                srcAddress = smsMessage.getOriginatingAddress();
                Log.d(TAG, "Received (partial) sms from: " + srcAddress);

                smsMessageStr += "SMS From: " + srcAddress + "\n";
                smsMessageStr += smsBody;
            }
            Log.d(TAG, smsMessageStr);

            SmsExchange.receive(srcAddress, smsBody, context);
        }
    }
}
