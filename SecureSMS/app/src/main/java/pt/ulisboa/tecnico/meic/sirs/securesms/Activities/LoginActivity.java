package pt.ulisboa.tecnico.meic.sirs.securesms.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pt.ulisboa.tecnico.meic.sirs.securesms.Models.UserModel;
import pt.ulisboa.tecnico.meic.sirs.securesms.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_phone) EditText _phoneNumber;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);



        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FirstLoginActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

    }

    @OnClick(R.id.btn_login)
    public void login() {
        String phoneNumber = "+351"+_phoneNumber.getText().toString();
        String password = _passwordText.getText().toString();
        String mypassword = null;
        String myphonenumber = null;
        UserModel userModel = null;

        if (!validate()) {
            onLoginFailed();
            return;
        }

        if(phoneNumber != null && password != null){
            userModel = new Select()
                    .from(UserModel.class)
                    .where("PhoneNumber=?", phoneNumber)
                    .executeSingle();
        } else {
            _phoneNumber.setError("Preencha com o seu número");
            _passwordText.setError("Preencha com a sua password");
        }

        if(userModel != null) {
             mypassword = userModel.getPassword();
             myphonenumber = userModel.getPhoneNumber();
        } else {
            Toast.makeText(getBaseContext(), "Utilizador não autorizado", Toast.LENGTH_LONG).show();
        }




        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);


        if(password.equals(mypassword) && phoneNumber.equals(myphonenumber)){
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onLoginSuccess();
                            progressDialog.dismiss();
                        }
                    }, 3000);

            Intent intent = new Intent(getApplicationContext(), SmsListActivity.class);
            intent.putExtra("PHONE_NUMBER", myphonenumber);
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
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
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

        if (phoneNumber.isEmpty()) {
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