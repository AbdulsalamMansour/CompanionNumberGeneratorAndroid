package com.abdulsalam.companionnumbergenerator;

import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.abdulsalam.companionnumbergenerator.utils.Helper;
import com.abdulsalam.companionnumbergenerator.utils.WaitDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
        String insertedNumber = Helper.replaceArabicNumbers(mMobileNumberEditText.getText().toString());
        if (insertedNumber.isEmpty()) {
            Toast.makeText(this, R.string.msg_insert_mobile_number, Toast.LENGTH_LONG).show();
        } else if (insertedNumber.length() == 10 || insertedNumber.length() == 13 || insertedNumber.length() == 14) {
            validateNumberAndCallGenerator(insertedNumber);
        } else {
            Toast.makeText(this.getApplicationContext(), "Insert a valid number", Toast.LENGTH_LONG).show();
        }
    }

    private void validateNumberAndCallGenerator(String insertedNumber){
        switch (insertedNumber.length()) {
            case 10:
                if(insertedNumber.startsWith("05")) {
                    Toast.makeText(this.getApplicationContext(), "Valid 10 digits number", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this.getApplicationContext(), "Insert a valid number", Toast.LENGTH_LONG).show();
                }
                break;

            case 13:
                if(insertedNumber.startsWith("+97059")){
                    Toast.makeText(this.getApplicationContext(), "Valid 13 digits number", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this.getApplicationContext(), "Insert a valid number", Toast.LENGTH_LONG).show();
                }
                break;

            case 14:
                if(insertedNumber.startsWith("0097059")){
                    Toast.makeText(this.getApplicationContext(), "Valid 14 digits number", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this.getApplicationContext(), "Insert a valid number", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                Toast.makeText(this.getApplicationContext(), "Insert a valid number", Toast.LENGTH_LONG).show();
        }
    }


}