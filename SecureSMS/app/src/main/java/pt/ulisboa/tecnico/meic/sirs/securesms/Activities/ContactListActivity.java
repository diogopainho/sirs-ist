package pt.ulisboa.tecnico.meic.sirs.securesms.Activities;

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
import pt.ulisboa.tecnico.meic.sirs.securesms.AdapterContactList;
import pt.ulisboa.tecnico.meic.sirs.securesms.Models.ContactModel;
import pt.ulisboa.tecnico.meic.sirs.securesms.R;

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
        ArrayList<ContactModel> contact = new ArrayList<ContactModel>();
        adaptercontactlist = new AdapterContactList(contact, getApplicationContext());
        contactlist.setLayoutManager(new LinearLayoutManager(this));

    }

    @OnClick(R.id.addcontact)
    public void goToAddMethod(){
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);

    }

    public static List<ContactModel> getAll(){
        return new Select().from(ContactModel.class).orderBy("Name DESC").execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<ContactModel> contact = (ArrayList<ContactModel>) getAll();
        adaptercontactlist = new AdapterContactList(contact, getApplicationContext());
        contactlist.setAdapter(adaptercontactlist);
        contactlist.invalidate();
    }

}
