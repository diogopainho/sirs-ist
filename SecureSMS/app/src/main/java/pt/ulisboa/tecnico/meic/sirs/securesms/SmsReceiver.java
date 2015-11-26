package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.nio.channels.SelectableChannel;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class SmsReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        String smsBody = null;
        String address = null;
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                smsBody = smsMessage.getMessageBody().toString();
                address = smsMessage.getOriginatingAddress();

                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";
            }
            Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();

            //Guarda a mensagem decifrada
            Message_Model receivedMessage = new Message_Model(address, smsBody, false);
            receivedMessage.save();

            Log.d("ADDRESS: ", address);

            //SmsListActivity inst = SmsListActivity.getInstance();
            //inst.onResume();
        }

    }


    public String decypherMessage(String receivedMessage, String sender){

        Contact_Model contact = new Select().from(Contact_Model.class).where("Phone_Number=?", sender).executeSingle();
        PublicKey publicKey = bytesToPublicKey(contact.getPublicKey());

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

    public PublicKey bytesToPublicKey(byte[] pubkeybytes){

        PublicKey pub = null;
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubkeybytes);
        KeyFactory keyFacPub = null;

        try {
            keyFacPub = KeyFactory.getInstance("RSA");
            pub = keyFacPub.generatePublic(pubSpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return pub;
    }
}
