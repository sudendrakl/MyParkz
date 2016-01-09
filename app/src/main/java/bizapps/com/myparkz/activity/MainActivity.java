package bizapps.com.myparkz.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import bizapps.com.myparkz.R;

/**
 * Created by sudendra.kamble on 15/11/15.
 */
public class MainActivity extends AppCompatActivity {

  static final int CAMERA = 99;
  static final int GALLERY = 100;

  View layout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    TextView tv = (TextView) findViewById(R.id.login_status);
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    boolean loginStatus = bundle.getBoolean("login_state");
    String phNo = bundle.getString("ph_no");
    tv.setText(String.format("Login status : %s\nPhone No. : %s", loginStatus, phNo));

    Button button = (Button) findViewById(R.id.camera_btn);
    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
      }
    });

    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALLERY);
      }
    });
    layout = findViewById(R.id.layout);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode == RESULT_OK) {
      switch (requestCode) {
        case CAMERA:
          Bundle extras = data.getExtras();
          Bitmap imageBitmap = (Bitmap) extras.get("data");
          layout.setBackground(new BitmapDrawable(imageBitmap));
          break;
        case GALLERY:
          try {
            Uri uri = data.getData();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            layout.setBackground(new BitmapDrawable(bitmap));
            bitmap.recycle();
            Intent intent = new Intent();
            intent.setAction("DOWNLOAD_COMPLETED");
            sendBroadcast(intent);
          } catch (Exception e){
            Log.e("MainActivity", e.getMessage());
          }
      }
    } else if(resultCode == RESULT_CANCELED) {
      Toast.makeText(this,"Camera Failed", Toast.LENGTH_LONG).show();
    }
  }
}
