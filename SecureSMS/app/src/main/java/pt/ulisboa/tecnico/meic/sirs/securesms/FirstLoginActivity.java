package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FirstLoginActivity extends AppCompatActivity {
    private static final String TAG = "FirstLoginActivity";

    @InjectView(R.id.input_name)
    EditText _nameText;
    @InjectView(R.id.input_phone)
    EditText _phoneText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstlogin);
        ButterKnife.inject(this);


        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    @OnClick(R.id.btn_signup)
    public void signup() {

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

            _signupButton.setEnabled(true);
            return;
        }

        _signupButton.setEnabled(false);


        final ProgressDialog progressDialog = new ProgressDialog(FirstLoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String phonenumber = _phoneText.getText().toString();
        String password = _passwordText.getText().toString();

        //As chaves sao geradas no momento do first login e sao armazenadas na base de dados
        MyContact myContact = new MyContact(name, phonenumber, password, CryptoHelper.generateKeyPair());
        myContact.save();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        _signupButton.setEnabled(true);
                        finish();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);


    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String phone = _phoneText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (phone.isEmpty() || phone.length() != 9) {
            _phoneText.setError("enter a valid phone number address");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}