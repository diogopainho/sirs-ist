package pt.ulisboa.tecnico.meic.sirs.securesms;

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


public class ConversationActivity extends AppCompatActivity {
    AdapterConversationList adapter_conversationList;
    static String phonenumber;


    @InjectView(R.id.message) EditText message;
    @InjectView(R.id.conversation_list) RecyclerView conversationlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        ButterKnife.inject(this);
        ArrayList<Message_Model> messages = new ArrayList<Message_Model>();
        adapter_conversationList = new AdapterConversationList(messages, getApplicationContext());

        conversationlist.setLayoutManager(new LinearLayoutManager(this));

        phonenumber = getIntent().getStringExtra("PHONE_NUMBER");
        Log.d("PHONE_NUMBER", phonenumber);

    }

    @OnClick(R.id.send)
    public void sendSms(){
        SmsSenderOld.sendSms(phonenumber, message.getText().toString(), getApplicationContext());
        message.setText("");
        refreshList();
    }

    public static List<Message_Model> getAll(){
        return new Select().from(Message_Model.class).where("PhoneNumber=?", phonenumber).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Message_Model> messages = (ArrayList<Message_Model>) getAll();
        adapter_conversationList = new AdapterConversationList(messages, getApplicationContext());
        conversationlist.setAdapter(adapter_conversationList);
        conversationlist.invalidate();
        conversationlist.scrollToPosition(messages.size()-1);
    }

    public void refreshList(){
        ArrayList<Message_Model> messages = (ArrayList<Message_Model>) getAll();
        adapter_conversationList = new AdapterConversationList(messages, getApplicationContext());
        conversationlist.setAdapter(adapter_conversationList);
        conversationlist.invalidate();
        conversationlist.scrollToPosition(messages.size()-1);
    }
}
