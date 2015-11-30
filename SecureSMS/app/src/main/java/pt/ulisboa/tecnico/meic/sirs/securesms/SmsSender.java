package pt.ulisboa.tecnico.meic.sirs.securesms;


import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;



public class SmsSender {
    private static final String TAG = "SmsSender";

    public void sendSms(String phoneNumber, String message, Context context) {
        phoneNumber = "+351"+phoneNumber;

        if(message != null){
            MyContact myAccount = new Select().from(MyContact.class).executeSingle();
            Contact_Model destinationContact = new Select()
                    .from(Contact_Model.class)
                    .where("Phone_Number=?", phoneNumber)
                    .executeSingle();

            if (destinationContact == null) {
                Toast.makeText(context, "Sms sending failed because you don't have " + phoneNumber +
                        "in your contact list.\nMake sure to add the contact first",
                        Toast.LENGTH_LONG).show();
                return;
            }

            PublicKey destinationPublicKey = KeyHelper.bytesToPublicKey(destinationContact.getPublicKey());
            PrivateKey myPrivateKey = KeyHelper.bytesToPrivateKey(myAccount.getBytesPrivatekey());

            try {
                byte[] plainBytes = message.getBytes();
                PlainMessage plainMessage = new PlainMessage(plainBytes);
                SecureMessage secureMessage = new SecureMessage(plainMessage, destinationPublicKey, myPrivateKey);
                byte[] cipheredBytes = secureMessage.doFinal();
                String cipheredText = SmsEncoding.encode(cipheredBytes);


                SmsManager smsManager = SmsManager.getDefault();
                ArrayList<String> multiPartSms = smsManager.divideMessage(cipheredText);

                smsManager.sendMultipartTextMessage(phoneNumber,
                        null,
                        multiPartSms,
                        null,
                        null);

                //Guarda a sms em plain text na base de dados
                Message_Model model = new Message_Model(phoneNumber, message, true);
                model.save();

                Toast.makeText(context, "Your sms has successfully sent!"+" "+phoneNumber+" "+ message,
                        Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(context,"Your sms has failed...",
                        Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    }
}