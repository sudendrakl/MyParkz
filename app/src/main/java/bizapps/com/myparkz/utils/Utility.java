package bizapps.com.myparkz.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by sudendra.kamble on 09/01/16.
 */
public class Utility {

  public static void hideKeyboard(Activity context) {
    try {
      InputMethodManager imm =
          (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
    } catch (Exception e) {
    }
  }
}
