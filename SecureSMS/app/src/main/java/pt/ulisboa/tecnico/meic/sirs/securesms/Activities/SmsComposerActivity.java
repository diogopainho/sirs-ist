package pt.ulisboa.tecnico.meic.sirs.securesms.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pt.ulisboa.tecnico.meic.sirs.securesms.R;
import pt.ulisboa.tecnico.meic.sirs.securesms.Sms.SmsExchange;

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

        SmsExchange.send(phoneNumber, message, getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), SmsListActivity.class);
        startActivity(intent);


    }
}

