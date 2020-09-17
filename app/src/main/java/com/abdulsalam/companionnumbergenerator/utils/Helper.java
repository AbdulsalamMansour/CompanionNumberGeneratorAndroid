package com.abdulsalam.companionnumbergenerator.utils;

import android.app.Activity;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Helper {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);

        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =(InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
