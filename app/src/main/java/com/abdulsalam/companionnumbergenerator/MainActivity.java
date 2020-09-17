package com.abdulsalam.companionnumbergenerator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.abdulsalam.companionnumbergenerator.utils.Helper;
import com.abdulsalam.companionnumbergenerator.utils.WaitDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GenerationFinishedListener{
    EditText mMobileNumberEditText;
    Button mStoreNumberButton;
    WaitDialog mWaitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setupHidingSoftKeyboard(findViewById(R.id.activity_home));

    }

    private void initViews() {
        mMobileNumberEditText = findViewById(R.id.et_mobile_number);
        mStoreNumberButton = findViewById(R.id.btn_store_number);

        mStoreNumberButton.setOnClickListener(this);
    }

    public void setupHidingSoftKeyboard(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideSoftKeyboard(MainActivity.this);
                return false;
            }
        });
    }

    private void showWaitDialog() {
        if (mWaitDialog == null) {
            mWaitDialog = new WaitDialog(this);
        }
        mWaitDialog.showDialog();
    }

    private void hideWaitDialog() {
        if (mWaitDialog != null) {
            mWaitDialog.dismissDialog();
        }
    }

    @Override
    public void onClick(View v) {
        if (mStoreNumberButton.equals(v)) {
            onStoreNumberClick();
        }
    }

    void onStoreNumberClick() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_CONTACTS)) {
                Toast.makeText(this, R.string.msg_permission,
                        Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
                        1);
            }
        } else {
            String insertedNumber = Helper.replaceArabicNumbers(mMobileNumberEditText.getText().toString());
            if (insertedNumber.isEmpty()) {
                Toast.makeText(this, R.string.msg_insert_mobile_number, Toast.LENGTH_LONG).show();
            } else if (insertedNumber.length() == 10 || insertedNumber.length() == 13 || insertedNumber.length() == 14) {
                validateNumberAndCallGenerator(insertedNumber);
            } else {
                Toast.makeText(this.getApplicationContext(), R.string.msg_insert_valid_number, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void validateNumberAndCallGenerator(String insertedNumber){
        switch (insertedNumber.length()) {
            case 10:
                if(insertedNumber.startsWith("05")) {
                    showWaitDialog();
                    new CompanionGeneratorAsyncTask(this, this).execute(("+970" + insertedNumber.substring(1)));
                } else {
                    Toast.makeText(this.getApplicationContext(), R.string.msg_insert_valid_number, Toast.LENGTH_LONG).show();
                }
                break;

            case 13:
                if(insertedNumber.startsWith("+97059")){
                    showWaitDialog();
                    new CompanionGeneratorAsyncTask(this, this).execute(insertedNumber);
                } else {
                    Toast.makeText(this.getApplicationContext(), R.string.msg_insert_valid_number, Toast.LENGTH_LONG).show();
                }
                break;

            case 14:
                if(insertedNumber.startsWith("0097059")){
                    showWaitDialog();
                    new CompanionGeneratorAsyncTask(this, this).execute(("+" + insertedNumber.substring(2)));
                } else {
                    Toast.makeText(this.getApplicationContext(), R.string.msg_insert_valid_number, Toast.LENGTH_LONG).show();
                }
                break;

            default:
                Toast.makeText(this.getApplicationContext(), R.string.msg_insert_valid_number, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void didFinish() {
        hideWaitDialog();
    }
}