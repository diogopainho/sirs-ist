package pt.ulisboa.tecnico.meic.sirs.securesms.Activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.ulisboa.tecnico.meic.sirs.securesms.IntentIntegrator;
import pt.ulisboa.tecnico.meic.sirs.securesms.IntentResult;
import pt.ulisboa.tecnico.meic.sirs.securesms.Models.ContactModel;
import pt.ulisboa.tecnico.meic.sirs.securesms.R;


public class AddContactActivity extends Activity {
    private static final String TAG = AddContactActivity.class.getSimpleName();
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_method_activity);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.scannerQR)
    public void scanQR(View v) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    @OnClick(R.id.keybase)
    public void keyBase(){
        Toast toast = Toast.makeText(this, "TODO", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onActivityResult(int request, int result, Intent i) {
        IntentResult scan=IntentIntegrator.parseActivityResult(request,
                result,
                i);

        if (scan!=null) {
            addContact(scan.getContents());
            Intent intent = new Intent(this, ContactListActivity.class);
            startActivity(intent);
        }
    }

    public void addContact(String content){
        String[] parse = content.split(";");

        ContactModel newContact = new ContactModel(parse[0], parse[1], Base64.decode(parse[2], Base64.DEFAULT));
        newContact.save();
    }

}
