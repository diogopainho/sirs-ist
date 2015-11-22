package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ContactListActivity extends AppCompatActivity {
    AdapterContactList adaptercontactlist;

    @InjectView(R.id.contact_list)
    RecyclerView contactlist;
    @InjectView(R.id.addcontact)
    ImageView addcontact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.inject(this);
        ArrayList<Contact_Model> contact = new ArrayList<Contact_Model>();
        adaptercontactlist = new AdapterContactList(contact);
        contactlist.setLayoutManager(new LinearLayoutManager(this));

    }

    @OnClick(R.id.addcontact)
    public void goToAddContact(){
        Intent intent = new Intent(getApplicationContext(), AddContactActivity.class);
        startActivity(intent);
    }

    public static List<Contact_Model> getAll(){
        return new Select().from(Contact_Model.class).orderBy("Name DESC").execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Contact_Model> contact = (ArrayList<Contact_Model>) getAll();
        adaptercontactlist = new AdapterContactList(contact);
        contactlist.setAdapter(adaptercontactlist);
        contactlist.invalidate();
    }
}
