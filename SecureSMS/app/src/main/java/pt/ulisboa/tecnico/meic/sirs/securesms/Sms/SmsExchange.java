package pt.ulisboa.tecnico.meic.sirs.securesms.Sms;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;

import pt.ulisboa.tecnico.meic.sirs.securesms.BusMessage;
import pt.ulisboa.tecnico.meic.sirs.securesms.BusStation;
import pt.ulisboa.tecnico.meic.sirs.securesms.MessageExchange.MessageExchangeProtocol;
import pt.ulisboa.tecnico.meic.sirs.securesms.Models.ContactModel;
import pt.ulisboa.tecnico.meic.sirs.securesms.Models.MessageModel;

public class SmsExchange {
    private static final String TAG = SmsExchange.class.getSimpleName();

    public static void send(String phoneNumber, String message, Context context) {
        if (!phoneNumber.startsWith("+351")) {
            phoneNumber = "+351" + phoneNumber;
        }

        ContactModel destinationContact = new Select()
                .from(ContactModel.class)
                .where("Phone_Number=?", phoneNumber)
                .executeSingle();

        try {
            SmsSender.send(destinationContact.getPhoneNumber(),
                    MessageExchangeProtocol.send(message, destinationContact, new SmsEncoding()));

            //Guarda a sms em plain text na base de dados
            MessageModel model = new MessageModel(phoneNumber, message, true);
            model.save();

            Toast.makeText(context, "Your sms was successfully sent!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Your sms has failed...", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static void receive(String senderNumber, String cipheredMessage, Context context) {
        ContactModel sender = new Select()
                .from(ContactModel.class)
                .where("Phone_Number=?", senderNumber)
                .executeSingle();

        try {
            String plainText = MessageExchangeProtocol.receive(cipheredMessage, sender, new SmsEncoding());
            Log.d(TAG, "Received sms plainText: " + plainText);

            //Guarda a mensagem decifrada
            MessageModel receivedMessage = new MessageModel(senderNumber, plainText, false);
            receivedMessage.save();

            BusStation.getBus().post(new BusMessage(receivedMessage));
        } catch (Exception ex) {
            Toast.makeText(context,
                    "There was an error processing the received message",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
