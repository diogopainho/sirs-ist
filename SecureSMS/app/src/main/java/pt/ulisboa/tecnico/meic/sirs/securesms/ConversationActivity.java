package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by diogopainho on 23/11/15.
 */
public class ConversationActivity extends AppCompatActivity {
    AdapterConversationList adapter_conversationList;

    @InjectView(R.id.conversation_list)
    RecyclerView conversationlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        ButterKnife.inject(this);
        ArrayList<Message_Model> messages = new ArrayList<Message_Model>();
        adapter_conversationList = new AdapterConversationList(messages);

        conversationlist.setLayoutManager(new LinearLayoutManager(this));

        getAll(getIntent().getStringExtra("SENDER"));

    }


    public static List<Message_Model> getAll(String sender){
        //TODO: Selecionar a ultima mensagem de cada contato
        return new Select().from(Message_Model.class).where("SentTo=?", sender).orderBy("Timestamp DESC").execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Message_Model> messages = (ArrayList<Message_Model>) getAll(getIntent().getStringExtra("SENDER"));
        adapter_conversationList = new AdapterConversationList(messages);
        conversationlist.setAdapter(adapter_conversationList);
        conversationlist.invalidate();
    }
}
