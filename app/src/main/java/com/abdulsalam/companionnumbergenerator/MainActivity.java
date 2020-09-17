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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mMobileNumberEditText;
    Button mStoreNumberButton;

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

    @Override
    public void onClick(View v) {
        if (mStoreNumberButton.equals(v)) {
            onStoreNumberClick();
        }
    }

    void onStoreNumberClick() {
        Toast.makeText(this, mMobileNumberEditText.getText(), Toast.LENGTH_SHORT).show();
    }
}