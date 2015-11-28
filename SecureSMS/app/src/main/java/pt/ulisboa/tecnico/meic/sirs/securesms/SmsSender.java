package pt.ulisboa.tecnico.meic.sirs.securesms;


import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;



public class SmsSender {


    public void sendSms(String phoneNumber, String message, Context context) {

        //Guarda a sms em plain text na base de dados
        Message_Model model = new Message_Model(phoneNumber, message, true);
        model.save();

        MyContact myContact = new Select().from(MyContact.class).executeSingle();

        PrivateKey privateKey = bytesToPrivateKey(myContact.getBytesPrivatekey());
        String cipheredMessage = cipherMessage(message, privateKey);

        try {
            // Get the default instance of the SmsManager
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,
                    null,
                    message,
                    null,
                    null);
            Toast.makeText(context, "Your sms has successfully sent!"+" "+phoneNumber+" "+ cipheredMessage,
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(context,"Your sms has failed...",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

    }


    public String cipherMessage(String message, PrivateKey privateKeyKey) {
        Cipher cipher = null;
        byte[] cipheredBytes = null;
        byte[] toCipherBytes = message.getBytes();

        try {

            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKeyKey);
            cipheredBytes = cipher.doFinal(toCipherBytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        String cipheredMessage = new String(cipheredBytes);

        return cipheredMessage;
    }

    public PrivateKey bytesToPrivateKey(byte[] privkeybytes) {
        KeyFactory keyFacPriv = null;
        PrivateKey priv = null;
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privkeybytes);

        try {
            keyFacPriv = KeyFactory.getInstance("RSA");
            priv = keyFacPriv.generatePrivate(privSpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return priv;
    }
}