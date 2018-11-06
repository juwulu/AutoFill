package com.jwl.autoidentifyingcode;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SmsObserverActivity extends Activity {

    public static final int MSG_RECEIVED_CODE = 1;
    private static final int REQUEST_PERMISSION_CODE = 0;
    private EditText metValidateCode = null;
    private SmsObserver mObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, REQUEST_PERMISSION_CODE);
        }
        metValidateCode = (EditText) findViewById(R.id.et_code);
        mObserver = new SmsObserver(SmsObserverActivity.this, mHandler);
        Uri uri = Uri.parse("content://sms");
        //注册短信的监听
        getContentResolver().registerContentObserver(uri, true, mObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //解除注册短信的监听
        getContentResolver().unregisterContentObserver(mObserver);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_RECEIVED_CODE) {
                String code = (String) msg.obj;
                metValidateCode.setText(code);
            }
        }
    };
    /*高版本手动获取权限*/

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length != 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "您阻止了app读取您的短信，您可以自己手动输入验证码", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("TAG", "获取权限");
            }
        }
    }

}