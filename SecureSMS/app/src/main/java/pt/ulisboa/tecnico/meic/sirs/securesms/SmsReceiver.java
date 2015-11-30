package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.nio.channels.SelectableChannel;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";
    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Sms received");
        String smsBody = "";
        String address = null;
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            Log.d(TAG, "sms.length=" + sms.length);
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                smsBody += smsMessage.getMessageBody();
                address = smsMessage.getOriginatingAddress();

                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody;
            }
            Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();

            MyContact myContact = new Select().from(MyContact.class).executeSingle();
            PrivateKey myPrivateKey = KeyHelper.bytesToPrivateKey(myContact.getBytesPrivatekey());
            PublicKey sourcePublicKey = KeyHelper.bytesToPublicKey(myContact.getBytesPublickey());

            try {
                byte[] receivedCipheredBytes = SmsEncoding.decode(smsBody);
                SecureMessage receivedSecureMessage = SecureMessage.parse(receivedCipheredBytes);
                PlainMessage receivedPlainMessage = receivedSecureMessage.toPlain(myPrivateKey, sourcePublicKey);
                String plainText = new String(receivedPlainMessage.getContent());
                Log.d(TAG, "Received sms plainText: " + plainText);

                //Guarda a mensagem decifrada
                Message_Model receivedMessage = new Message_Model(address, plainText, false);
                receivedMessage.save();

                BusStation.getBus().post(new BusMessage(receivedMessage));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public String decypherMessage(String receivedMessage, String sender){

        Contact_Model contact = new Select().from(Contact_Model.class).where("Phone_Number=?", sender).executeSingle();
        PublicKey publicKey = KeyHelper.bytesToPublicKey(contact.getPublicKey());

        Cipher cipher = null;
        byte[] newPlainBytes = null;

        try {
            cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            newPlainBytes = cipher.doFinal(receivedMessage.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        String cleanMessage = new String(newPlainBytes);
        return cleanMessage;

    }
}
