package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SmsComposerActivity extends AppCompatActivity {
    @InjectView(R.id.phone_number) EditText _phoneNumber;
    @InjectView(R.id.message) EditText _message;
    @InjectView(R.id.send_button) Button _sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_composer);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.send_button)
    public void sendMessage() {
        String phoneNumber = _phoneNumber.getText().toString();
        String message = _message.getText().toString();

        if (!phoneNumber.startsWith("+351")) {
            phoneNumber = "+351" + phoneNumber;
        }

        Contact_Model destinationContact = new Select()
                .from(Contact_Model.class)
                .where("Phone_Number=?", phoneNumber)
                .executeSingle();

        try {
            SecureSmsProtocol.send(message, destinationContact);

            //Guarda a sms em plain text na base de dados
            Message_Model model = new Message_Model(phoneNumber, message, true);
            model.save();

            Toast.makeText(getApplicationContext(), "Your sms has successfully sent!"+" "+phoneNumber+" "+ message, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Your sms has failed...", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        Intent intent = new Intent(getApplicationContext(), SmsListActivity.class);
        startActivity(intent);


    }
}

