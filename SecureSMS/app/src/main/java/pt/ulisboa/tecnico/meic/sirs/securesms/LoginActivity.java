package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_phone) EditText _phoneNumber;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;
    @InjectView(R.id.temp_skip) TextView _tempSkip;
    @InjectView(R.id.temp1_skip) TextView _temp1Skip;

    String myPhoneNumber =  "917245592";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), FirstLoginActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        _tempSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SmsComposer.class);
                startActivity(intent);
            }
        });

        _temp1Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SmsList.class);
                startActivity(intent);
            }
        });

    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        String phoneNumber = _phoneNumber.getText().toString();
        String password = _passwordText.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);


        if(phoneNumber.equals("917245592") && password.equals("123qwe")){
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            onLoginSuccess();
                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);

            //Adicionar aqui o Itent para a outra atividade
            Intent intent = new Intent(getApplicationContext(), SmsList.class);
            startActivity(intent);

        } else {
            _passwordText.setError("Wrong Password");
            onLoginFailed();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String phoneNumber = _phoneNumber.getText().toString();
        String password = _passwordText.getText().toString();

        if (phoneNumber.isEmpty() || phoneNumber.length() != 9) {
            _phoneNumber.setError("enter a valid phone number");
            valid = false;
        } else {
            _phoneNumber.setError(null);
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