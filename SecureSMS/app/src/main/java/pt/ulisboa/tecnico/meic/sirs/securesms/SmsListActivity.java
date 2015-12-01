package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.activeandroid.query.Select;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SmsListActivity extends AppCompatActivity {
    private static SmsListActivity instance;
    AdapterSmsList adapter_smsList;

    @InjectView(R.id.message_list) RecyclerView messagelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.inject(this);
        ArrayList<Message_Model> messages = new ArrayList<Message_Model>();
        adapter_smsList = new AdapterSmsList(messages, getApplicationContext());

        messagelist.setLayoutManager(new LinearLayoutManager(this));

    }

    public static SmsListActivity getInstance(){
        return instance;
    }

    @OnClick(R.id.qrcode)
    public void goToGenerateQRCode(){
        UserModel user = new Select().from(UserModel.class).executeSingle();

        Intent intent = new Intent(getApplicationContext(), GenerateQRCodeActivity.class);

        intent.putExtra("NAME", user.getName());
        intent.putExtra("PHONE_NUMBER", user.getPhoneNumber());
        intent.putExtra("PUB_KEY", user.getBytesPublickey());

        startActivity(intent);
    }

    @OnClick(R.id.compose)
    public void goToCompose(){
        Intent intent = new Intent(getApplicationContext(), SmsComposerActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.contacts)
    public void goToContactList(){
        Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
        startActivity(intent);
    }

    public static List<Message_Model> getAll(){
        return new Select().from(Message_Model.class).orderBy("ID DESC").execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        BusStation.getBus().register(this);
        ArrayList<Message_Model> messages = (ArrayList<Message_Model>) getAll();
        adapter_smsList = new AdapterSmsList(messages, getApplicationContext());
        messagelist.setAdapter(adapter_smsList);
        messagelist.invalidate();
    }

    @Override
    public void onPause(){
        super.onPause();
        BusStation.getBus().unregister(this);

    }

    @Subscribe
    public void busReceivedMessage(BusMessage busmessage){

        ArrayList<Message_Model> messages = (ArrayList<Message_Model>) getAll();
        adapter_smsList = new AdapterSmsList(messages, getApplicationContext());
        adapter_smsList.add(messages.size()-1,busmessage.getMessage_model());
        messagelist.setAdapter(adapter_smsList);
        messagelist.invalidate();
    }
}
