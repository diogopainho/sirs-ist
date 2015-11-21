package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SmsComposer extends AppCompatActivity {
    @InjectView(R.id.phone_number) EditText _phoneNumber;
    @InjectView(R.id.message) EditText _message;
    @InjectView(R.id.send_button) Button _sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_composer);
        ButterKnife.inject(this);

        _sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String phoneNumber = _phoneNumber.getText().toString();
                String message = _message.getText().toString();

                SmsManager smsManager = SmsManager.getDefault();
               // smsManager.sendTextMessage(phoneNumber, null, message, null, null);

               //Para guardar na base de dados
                Message_Model model = new Message_Model(phoneNumber, message);
                model.save();

            }
        });
    }
}
