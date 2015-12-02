package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;


public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";
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
            Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();

            Contact_Model sender = new Select()
                    .from(Contact_Model.class)
                    .where("Phone_Number=?", srcAddress)
                    .executeSingle();

            try {
                String plainText = SecureSmsProtocol.receive(smsBody, sender);
                Log.d(TAG, "Received sms plainText: " + plainText);

                //Guarda a mensagem decifrada
                Message_Model receivedMessage = new Message_Model(srcAddress, plainText, false);
                receivedMessage.save();

                BusStation.getBus().post(new BusMessage(receivedMessage));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

//            UserModel user = new Select().from(UserModel.class).executeSingle();
//            PrivateKey myPrivateKey = KeyHelper.bytesToPrivateKey(user.getBytesPrivatekey());
//            Contact_Model sender = new Select().from(Contact_Model.class).where("Phone_Number=?", srcAddress).executeSingle();
//            PublicKey senderPublicKey = KeyHelper.bytesToPublicKey(sender.getPublicKeyBytes());
//
//            try {
//                byte[] receivedCipheredBytes = SmsEncoding.decode(smsBody);
//                SecureMessage receivedSecureMessage = SecureMessage.parse(receivedCipheredBytes);
//                PlainMessage receivedPlainMessage = receivedSecureMessage.toPlain(myPrivateKey, senderPublicKey);
//                String plainText = new String(receivedPlainMessage.getContent());
//                Log.d(TAG, "Received sms plainText: " + plainText);
//
//                //Guarda a mensagem decifrada
//                Message_Model receivedMessage = new Message_Model(srcAddress, plainText, false);
//                receivedMessage.save();
//
//                BusStation.getBus().post(new BusMessage(receivedMessage));
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
        }
    }
}
