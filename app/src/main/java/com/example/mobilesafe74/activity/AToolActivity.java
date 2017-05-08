package com.example.mobilesafe74.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mobilesafe74.R;
import com.example.mobilesafe74.engine.SmsBackUp;

import java.io.File;

/**
 * Created by yueyue on 2017/1/22.
 */
public class AToolActivity extends AppCompatActivity {

    private ProgressBar pb_bar;
    private TextView tv_commonnumber_query;
    private TextView tv_app_lock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_atool);

        initPhoneAddress();

        initSmsBackUp();

        //常用号码查询
        initCommonNumberQuery();

        initAppLock();
    }

    private void initAppLock() {
        tv_app_lock = (TextView) findViewById(R.id.tv_app_lock);
        tv_app_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), AppLockActivity.class));
            }
        });
    }


    private void initCommonNumberQuery() {
        tv_commonnumber_query = (TextView) findViewById(R.id.tv_commonnumber_query);
        tv_commonnumber_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CommonNumberQueryActivity.class));
            }
        });
    }


    /**
     * 短信备份
     */
    private void initSmsBackUp() {
        //找到我们需要的组件
        TextView tv_smsbackup = (TextView) findViewById(R.id.tv_smsbackup);
        pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
        tv_smsbackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSmsBackUpDialog();
            }
        });
    }

    /**
     * 短信备份
     */
    private void showSmsBackUpDialog() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setTitle("短信备份");
        //设置进度条的样式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog.show();


        new Thread(new Runnable() {
            @Override
            public void run() {
                final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "smsbackup.xml";
                SmsBackUp.backup(AToolActivity.this, path, new SmsBackUp.CallBack() {
                    @Override
                    public void setMax(int max) {
                        progressDialog.setMax(max);
                        pb_bar.setMax(max);
                    }

                    @Override
                    public void setProgress(int progress) {
                        progressDialog.setProgress(progress);
                        pb_bar.setProgress(progress);
                    }
                });

                progressDialog.dismiss();//备份完成之后关闭进度条
            }
        }) {
        }.start();
    }

    /**
     * 跳转到归属地查询的界面
     */
    private void initPhoneAddress() {
        //找到我们需要的组件
        TextView tv_address = (TextView) findViewById(R.id.tv_address);
        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AToolActivity.this, QureyAddressActivity.class);
                startActivity(intent);
            }
        });

    }
}