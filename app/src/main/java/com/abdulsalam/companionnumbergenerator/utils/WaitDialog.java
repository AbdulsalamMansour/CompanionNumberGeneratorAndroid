package com.abdulsalam.companionnumbergenerator.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

import com.abdulsalam.companionnumbergenerator.R;

public class WaitDialog {

    private AlertDialog alertDialog = null;

    private Activity activity = null;

    public WaitDialog(Activity activity) {
        this.activity = activity;

        alertDialog = new AlertDialog.Builder(activity, R.style.AlertDialogStyle)
                .setCancelable(false)
                .setView(LayoutInflater.from(activity).inflate(R.layout.dialog_wait, null))
                .create();
    }

    public void showDialog() {
        if (activity == null) {
            return;
        }

        if (alertDialog != null && !alertDialog.isShowing()) {
            if (activity != null) {
                if (!(activity).isFinishing()) {
                    alertDialog.show();
                }
            }
        }
    }

    public void dismissDialog() {
        if (activity == null) {
            return;
        }

        if (alertDialog != null && alertDialog.isShowing()) {
            Context context = ((ContextWrapper) alertDialog.getContext()).getBaseContext();
            if (context instanceof Activity) {
                if (!((Activity) context).isFinishing()) {
                    alertDialog.dismiss();
                }
            } else {
                alertDialog.dismiss();
            }
        }
    }
}