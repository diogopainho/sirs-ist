package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        SmsSender smsSender = new SmsSender();
        smsSender.sendSms(phoneNumber, message, getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), SmsListActivity.class);
        startActivity(intent);


    }
}

