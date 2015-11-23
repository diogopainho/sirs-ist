package pt.ulisboa.tecnico.meic.sirs.securesms;


import android.telephony.SmsManager;
import android.util.Log;

import com.activeandroid.query.Select;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by diogopainho on 22/11/15.
 */
public class SmsSender {

    public SmsSender() {
    }

    public void sendSms(String phoneNumber, String message) {

        //Para guardar na base de dados
        Message_Model model = new Message_Model(phoneNumber, message, true);
        model.save();

        MyContact myContact = new Select().from(MyContact.class).executeSingle();

        PrivateKey privateKey = bytesToPrivateKey(myContact.getBytesPrivatekey());
        String cipheredMessage = cipherMessage(message, privateKey);

        SmsManager smsManager = SmsManager.getDefault();
        // smsManager.sendTextMessage(phoneNumber, null, cipheredMessage, null, null);

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