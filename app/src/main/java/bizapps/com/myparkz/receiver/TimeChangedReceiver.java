package bizapps.com.myparkz.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.util.Log;

/**
 * Created by sudendra.kamble on 15/11/15.
 */
public class TimeChangedReceiver extends BroadcastReceiver {
  @Override public void onReceive(Context context, Intent intent) {
    Log.d("TimeChanonReceive", intent.getAction());
  }
}
