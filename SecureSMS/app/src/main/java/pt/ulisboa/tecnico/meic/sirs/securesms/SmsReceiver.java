package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
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

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(SMS_RECEIVED)){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                //get sms objects
                Object[] pdus = (Objects[]) bundle.get("pdus");
                if(pdus.length == 0){
                    return;
                }
                //large message might be broken into many
                SmsMessage[] messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                for (int i=0; i<pdus.length; i++){
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sb.append(messages[i].getMessageBody());
                }
                String sender = messages[0].getOriginatingAddress();
                String message = sb.toString();

                //Guarda a mensagem decifrada
                Message_Model receivedMessage = new Message_Model(sender, decypherMessage(message, sender), false);
                receivedMessage.save();

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
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
