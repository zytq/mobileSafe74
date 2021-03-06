package com.example.mobilesafe74.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.mobilesafe74.R;
import com.example.mobilesafe74.utils.ConstantValue;
import com.example.mobilesafe74.utils.SpUtils;

/**
 * Created by yueyue on 2017/1/17.
 */
public class Setup4Activity extends BaseSetupActivity {

    private CheckBox cb_box;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_setup4);

        //初始化UI
        initUI();
    }


    /**
     * 初始化UI
     */
    private void initUI() {
        //找到我们需要的组件
        cb_box = (CheckBox) findViewById(R.id.cb_box);

        //获取CheckBox之前的状态
        boolean open_security = SpUtils.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        cb_box.setChecked(open_security);

        if (open_security) {
            cb_box.setText("防盗功能已经开启");
        } else {
            cb_box.setText("你没有开启防盗保护");
        }

        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_box.setText("防盗功能已经开启");
                } else {
                    cb_box.setText("你没有开启防盗保护");
                }
                SpUtils.putBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, isChecked);
            }
        });


    }

    /**
     * 从Setup4Activity界面跳转到之前的Setup3Activity界面
     *
     * @param view
     */
    public void prePage(View view) {

        showPrePage();
    }

    /**
     * 从Setup4Activity界面跳转到SetupOverActivity界面
     *
     * @param view
     */
    public void nextPage(View view) {
        showNextPage();

    }

    @Override
    protected void showPrePage() {
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);


        //设置平移动画效果
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);

        finish();//销毁当前页面
    }

    @Override
    protected void showNextPage() {
        boolean open_security = SpUtils.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        //跳转之前对是否开启了防盗保护进行判断,如果开启了就可以跳转到SetupOver界面去
        if (open_security) {
            Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
            startActivity(intent);


            //设置平移动画效果
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);

            finish();//销毁当前页面


            SpUtils.putBoolean(this, ConstantValue.SETUP_OVER, true);

        } else {
            Toast.makeText(this, "请您手动开启防盗保护功能", Toast.LENGTH_SHORT).show();
        }

    }

}
