package bizapps.com.myparkz;

import android.app.Application;
import com.parse.Parse;

/**
 * Created by sudendra.kamble on 09/01/16.
 */
public class ParkzApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    // [Optional] Power your app with Local Datastore. For more info, go to
    // https://parse.com/docs/android/guide#local-datastore
    Parse.enableLocalDatastore(this);

    Parse.initialize(this);
  }
}
