package pt.ulisboa.tecnico.meic.sirs.securesms;


import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;


public class SmsSenderOld {
    private static final String TAG = "SmsSenderOld";

    public static void sendSms(String phoneNumber, String message, Context context) {
        phoneNumber = "+351"+phoneNumber;

        if(message != null){
            UserModel user = new Select().from(UserModel.class).executeSingle();
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

            Log.d(TAG, "Destination name: "+destinationContact.getName());
            Log.d(TAG, "Destination public key length: "+destinationContact.getPublicKeyBytes().length);
            PublicKey destinationPublicKey = KeyHelper.bytesToPublicKey(destinationContact.getPublicKeyBytes());
            PrivateKey myPrivateKey = KeyHelper.bytesToPrivateKey(user.getBytesPrivatekey());

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