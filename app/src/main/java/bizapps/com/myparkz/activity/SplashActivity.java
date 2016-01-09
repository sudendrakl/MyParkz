package bizapps.com.myparkz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import bizapps.com.myparkz.R;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.parse.ParseUser;

public class SplashActivity extends AppCompatActivity {

  @InjectView(R.id.logo) TextView logo;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    ButterKnife.inject(this);

    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override public void run() {
        checkLogin();
      }
    }, 1000);


  }

  private void checkLogin() {
    if (ParseUser.getCurrentUser() != null) {
      // Start an intent for the logged in activity
      startActivity(new Intent(this, MapsActivity.class));
    } else {
      // Start and intent for the logged out activity
      startActivity(new Intent(this, LoginActivity.class));
    }

  }

}