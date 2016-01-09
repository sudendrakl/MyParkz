package bizapps.com.myparkz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import bizapps.com.myparkz.R;
import bizapps.com.myparkz.utils.BundleKeys;
import bizapps.com.myparkz.utils.ParseKeys;
import bizapps.com.myparkz.utils.Utility;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
  private static final String TAG = "LoginActivity";
  private static final int REQUEST_SIGNUP = 0;

  @InjectView(R.id.input_ph_no) EditText _phNoText;
  @InjectView(R.id.input_password) EditText _passwordText;
  @InjectView(R.id.ph_text_layout) TextInputLayout phTextInputLayout;
  @InjectView(R.id.passwd_text_layout) TextInputLayout passwdTextInputLayout;

  @InjectView(R.id.btn_login) Button _loginButton;
  @InjectView(R.id.link_signup) TextView _signupLink;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.inject(this);

    _loginButton.setOnClickListener(new View.OnClickListener() {

      @Override public void onClick(View v) {
        login();
      }
    });

    _signupLink.setOnClickListener(new View.OnClickListener() {

      @Override public void onClick(View v) {
        // Start the Signup activity
        signUP();
      }
    });

    //TODO: Read ph from TelephonyManager
    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    String phNo = telephonyManager.getLine1Number();
    if (!TextUtils.isEmpty(phNo)) {
      _phNoText.setText(phNo);
    }
  }

  public void login() {
    Utility.hideKeyboard(this);
    Log.d(TAG, "Login");
    String phNo = _phNoText.getText().toString();
    String password = _passwordText.getText().toString();

    if (validate(phNo, password)) {
      ParseUser.logInInBackground(phNo, password, new LogInCallback() {
        @Override public void done(ParseUser user, ParseException e) {
          // Handle the response
          if (user != null && e == null) {
            Snackbar.make(_phNoText,
                getString(R.string.login_welcome, user.getString(ParseKeys.User.DISPLAY_NAME)),
                Snackbar.LENGTH_LONG).show();
          } else {
            Snackbar.make(_phNoText, "Failed to sign in", Snackbar.LENGTH_LONG)
                .setAction("SignUp", new View.OnClickListener() {
                  @Override public void onClick(View v) {
                    signUP();
                  }
                })
                .show();
            Log.i(TAG, e.getMessage());
          }
        }
      });
    }
  }

  public void signUP() {
    Utility.hideKeyboard(this);
    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
    String phNo = _phNoText.getText().toString();
    intent.putExtra(BundleKeys.USER_NAME, phNo);
    startActivityForResult(intent, REQUEST_SIGNUP);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Utility.hideKeyboard(this);
    if (requestCode == REQUEST_SIGNUP) {
      if (resultCode == RESULT_OK) {

        // TODO: Implement successful signup logic here
        // By default we just finish the Activity and log them in automatically
        this.finish();
      }
    }
  }

  @Override public void onBackPressed() {
    // Disable going back to the SplashActivity
    moveTaskToBack(true);
  }

  public boolean validate(String phNo, String password) {
    boolean valid = true;

    if (phNo.isEmpty() || !Patterns.PHONE.matcher(phNo).matches()) {
      phTextInputLayout.setError("enter a valid ph no.");//+91 1234567890
      valid = false;
    } else {
      phTextInputLayout.setError(null);
    }

    if (password.isEmpty() || password.length() < 4 || password.length() > 16 || !password.matches(
        "(.*)([A-Z]+)(.*)") || !password.matches("(.*)([0-9]+)(.*)")) {
      passwdTextInputLayout.setError(getString(R.string.password_error));
      valid = false;
    } else {
      passwdTextInputLayout.setError(null);
    }

    return valid;
  }
}
