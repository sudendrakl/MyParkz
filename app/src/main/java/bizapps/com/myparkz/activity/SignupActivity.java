package bizapps.com.myparkz.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import bizapps.com.myparkz.R;
import bizapps.com.myparkz.utils.BundleKeys;
import bizapps.com.myparkz.utils.ParseKeys;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {
  private static final String TAG = "SignupActivity";

  @InjectView(R.id.input_ph_no) EditText _phNoText;
  @InjectView(R.id.input_name) EditText _nameText;
  @InjectView(R.id.input_email) EditText _emailText;
  @InjectView(R.id.input_password) EditText _passwordText;
  @InjectView(R.id.input_dob) EditText _dobText;
  @InjectView(R.id.btn_signup) Button _signupButton;
  @InjectView(R.id.link_login) TextView _loginLink;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);
    ButterKnife.inject(this);

    _signupButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        signup();
      }
    });

    _loginLink.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        // Finish the registration screen and return to the Login activity
        finish();
      }
    });
    String phNo = getIntent().getStringExtra(BundleKeys.USER_NAME);
    _phNoText.setText(phNo);
  }

  public void signup() {
    Log.d(TAG, "Signup");

    if (!validate()) {
      onSignupFailed();
      return;
    }

    _signupButton.setEnabled(false);

    final ProgressDialog progressDialog =
        new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage("Creating Account...");
    progressDialog.show();

    String phNo = _phNoText.getText().toString();
    String name = _nameText.getText().toString();
    String email = _emailText.getText().toString();
    String password = _passwordText.getText().toString();
    String dob = _dobText.getText().toString();

    try {
      ParseConfig parseConfig = ParseConfig.get();
      String env = parseConfig.getString("Environment", null);

      ParseUser parseUser = new ParseUser();
      parseUser.setUsername(phNo);
      parseUser.setPassword(password);
      parseUser.setEmail(email);
      parseUser.put(ParseKeys.User.DISPLAY_NAME, name);
      if (!dob.isEmpty()) {
        parseUser.put(ParseKeys.User.DOB, dob);
      }

      // Call the Parse signup method
      parseUser.signUpInBackground(new SignUpCallback() {
        @Override public void done(ParseException e) {
          progressDialog.dismiss();
          // Handle the response
          if (e != null) {
            // Show the error message
            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
          } else {
            Log.i("MapsActivity", "Parse User Created");
            Snackbar.make(_passwordText, "Hurray You signed up", Snackbar.LENGTH_LONG).show();
            onSignupSuccess();
          }
        }
      });

      Log.i("MapsActivity", env);
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public void onSignupSuccess() {
    _signupButton.setEnabled(true);
    setResult(RESULT_OK, null);
    finish();
    Intent intent = new Intent(this, MapsActivity.class);
    startActivity(intent);
  }

  public void onSignupFailed() {
    Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

    _signupButton.setEnabled(true);
  }

  public boolean validate() {
    boolean valid = true;

    String phNo = _phNoText.getText().toString();
    String name = _nameText.getText().toString();
    String email = _emailText.getText().toString();
    String password = _passwordText.getText().toString();
    String dob = _dobText.getText().toString();

    if (phNo.isEmpty() || !Patterns.PHONE.matcher(phNo).matches()) {
      _phNoText.setError("not valid phone number");
      valid = false;
    } else {
      _phNoText.setError(null);
    }

    if (name.isEmpty() || name.length() < 3) {
      _nameText.setError("at least 3 characters");
      valid = false;
    } else {
      _nameText.setError(null);
    }

    if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      _emailText.setError("enter a valid email address");
      valid = false;
    } else {
      _emailText.setError(null);
    }

    if (password.isEmpty() || password.length() < 4 || password.length() > 16 || !password.matches(
        "(.*)([A-Z]+)(.*)") || !password.matches("(.*)([0-9]+)(.*)")) {
      _passwordText.setError(getString(R.string.password_error));
      valid = false;
    } else {
      _passwordText.setError(null);
    }

    return valid;
  }
}