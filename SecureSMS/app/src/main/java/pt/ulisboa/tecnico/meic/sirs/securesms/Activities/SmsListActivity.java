package pt.ulisboa.tecnico.meic.sirs.securesms.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.activeandroid.query.Select;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pt.ulisboa.tecnico.meic.sirs.securesms.AdapterSmsList;
import pt.ulisboa.tecnico.meic.sirs.securesms.BusMessage;
import pt.ulisboa.tecnico.meic.sirs.securesms.BusStation;
import pt.ulisboa.tecnico.meic.sirs.securesms.Models.MessageModel;
import pt.ulisboa.tecnico.meic.sirs.securesms.Models.UserModel;
import pt.ulisboa.tecnico.meic.sirs.securesms.R;

public class SmsListActivity extends AppCompatActivity {
    private static final String TAG = SmsListActivity.class.getSimpleName();
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
        ArrayList<MessageModel> messages = new ArrayList<MessageModel>();
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
        intent.putExtra("PUB_KEY", user.getPublicKeyBytes());

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

    public static List<MessageModel> getAll(){
        return new Select().from(MessageModel.class).orderBy("ID DESC").execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        BusStation.getBus().register(this);
        ArrayList<MessageModel> messages = (ArrayList<MessageModel>) getAll();
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
        Log.d(TAG, "busReceivedMessage");
        adapter_smsList.add(0, busmessage.getMessage_model());
        messagelist.setAdapter(adapter_smsList);
        messagelist.invalidate();
    }
}
