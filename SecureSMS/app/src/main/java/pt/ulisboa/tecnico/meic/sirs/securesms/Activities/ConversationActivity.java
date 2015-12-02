package pt.ulisboa.tecnico.meic.sirs.securesms.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pt.ulisboa.tecnico.meic.sirs.securesms.AdapterConversationList;
import pt.ulisboa.tecnico.meic.sirs.securesms.Models.MessageModel;
import pt.ulisboa.tecnico.meic.sirs.securesms.R;
import pt.ulisboa.tecnico.meic.sirs.securesms.Sms.SmsExchange;


public class ConversationActivity extends AppCompatActivity {
    AdapterConversationList adapter_conversationList;
    static String phoneNumber;


    @InjectView(R.id.message) EditText message;
    @InjectView(R.id.conversation_list) RecyclerView conversationlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        ButterKnife.inject(this);
        ArrayList<MessageModel> messages = new ArrayList<MessageModel>();
        adapter_conversationList = new AdapterConversationList(messages, getApplicationContext());

        conversationlist.setLayoutManager(new LinearLayoutManager(this));

        phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        Log.d("PHONE_NUMBER", phoneNumber);

    }

    @OnClick(R.id.send)
    public void sendSms(){
        SmsExchange.send(phoneNumber, message.getText().toString(), getApplicationContext());

        message.setText("");
        refreshList();
    }

    public static List<MessageModel> getAll(){
        return new Select().from(MessageModel.class).where("PhoneNumber=?", phoneNumber).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<MessageModel> messages = (ArrayList<MessageModel>) getAll();
        adapter_conversationList = new AdapterConversationList(messages, getApplicationContext());
        conversationlist.setAdapter(adapter_conversationList);
        conversationlist.invalidate();
        conversationlist.scrollToPosition(messages.size()-1);
    }

    public void refreshList(){
        ArrayList<MessageModel> messages = (ArrayList<MessageModel>) getAll();
        adapter_conversationList = new AdapterConversationList(messages, getApplicationContext());
        conversationlist.setAdapter(adapter_conversationList);
        conversationlist.invalidate();
        conversationlist.scrollToPosition(messages.size()-1);
    }
}
