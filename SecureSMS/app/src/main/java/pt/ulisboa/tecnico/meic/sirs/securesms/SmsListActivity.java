package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SmsListActivity extends AppCompatActivity {
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
        adapter_smsList = new AdapterSmsList(messages);

        messagelist.setLayoutManager(new LinearLayoutManager(this));

    }

    @OnClick(R.id.qrcode)
    public void goToGenerateQRCode(){
        MyContact mycontact = new Select().from(MyContact.class).executeSingle();
        Intent intent = new Intent(getApplicationContext(), GenerateQRCodeActivity.class);

        intent.putExtra("Name", mycontact.getName());
        intent.putExtra("PhoneNumber", mycontact.getPhoneNumber());
        intent.putExtra("PubKey", mycontact.getBytesPrivatekey());

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
        //TODO: Selecionar a ultima mensagem de cada contato
        return new Select().from(Message_Model.class).orderBy("Timestamp DESC").execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Message_Model> messages = (ArrayList<Message_Model>) getAll();
        adapter_smsList = new AdapterSmsList(messages);
        messagelist.setAdapter(adapter_smsList);
        messagelist.invalidate();
    }
}
