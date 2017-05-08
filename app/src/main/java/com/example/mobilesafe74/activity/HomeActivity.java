package com.example.mobilesafe74.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe74.R;
import com.example.mobilesafe74.utils.ConstantValue;
import com.example.mobilesafe74.utils.MD5Utils;
import com.example.mobilesafe74.utils.SpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yueyue on 2017/1/12.
 */
public class HomeActivity extends AppCompatActivity {

    private GridView gv_home;
    private String[] mTitleStr;
    private int[] mDrawableIds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //初始化UI
        initUI();

        //初始化数据
        initData();

        //初始化数据库
        initDB();

        //生成快捷方式
        initShortCut();

    }

    /**
     *
     *
     *          <intent-filter>
     <action android:name="android.intent.action.HOME"/>

     <category android:name="android.intent.category.DEFAULT"/>
     </intent-filter>
     */
    private void initShortCut() {
       /* Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        //图表
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
                BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        //应用名称
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,"悦哥卫士1.0");
        //应用意图
        Intent shortCutIntent = new Intent("android.intent.action.HOME");
        shortCutIntent.addCategory("android.intent.category.DEFAULT");

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,shortCutIntent);


        sendBroadcast(intent);*/

       /* Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "黑马卫士");

        Intent shortCutIntent = new Intent("android.intent.action.HOME");
        shortCutIntent.addCategory("android.intent.category.DEFAULT");

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutIntent);

        sendBroadcast(intent);
*/
    }


    /**
     * 初始化数据库
     */
    private void initDB() {
        //复制数据库到files文件夹下
         initAddress("address.db");
         initAddress("commonnum.db");
         initAddress("antivirus.db");

    }

    /**
     * 把数据库拷贝到文件目录中去
     *
     * @param dbName 数据库的名字
     */
    private void initAddress(String dbName) {
        File files = getFilesDir();
        File file = new File(files, dbName);
        if (file.exists()) {
            return;
        }
        InputStream in = null;
        FileOutputStream fos = null;
        try {
            in = getAssets().open(dbName);
            byte[] buffer = new byte[1024];
            fos = new FileOutputStream(file);
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流,释放资源
            if (in != null && fos != null) {
                try {
                    in.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     * 初始化数据
     */
    private void initData() {
        mTitleStr = new String[]{"手机防盗", "通信卫士"
                , "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
        mDrawableIds = new int[]{R.drawable.home_safe,
                R.drawable.home_callmsgsafe,
                R.drawable.home_apps,
                R.drawable.home_taskmanager,
                R.drawable.home_netmanager,
                R.drawable.home_trojan,
                R.drawable.home_sysoptimize,
                R.drawable.home_tools,
                R.drawable.home_settings};

        gv_home.setAdapter(new MyAdapter());

        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showDialog();
                        break;
                    case 1:
                        //跳转到手机通信卫士的界面
                        startActivity(new Intent(HomeActivity.this,BlackNumberActivity.class));
                        break;
                    case 2:
                        //跳转到软件管理的界面
                        startActivity(new Intent(HomeActivity.this,AppManagerActivity.class));
                    case 3:
                        //跳转到软件管理的界面
                        startActivity(new Intent(HomeActivity.this,ProcessManagerActivity.class));
                        break;
                    case 5:
                        //跳转到软件管理的界面
                        startActivity(new Intent(HomeActivity.this,AnitVirusActivity.class));
                        break;
                    case 6:
  			//	         startActivity(new Intent(getApplicationContext(), CacheClearActivity.class));

                        break;
                    case 7:
                        //跳转大高级工具的界面
                        Intent intent1 = new Intent(HomeActivity.this, AToolActivity.class);
                        startActivity(intent1);
                        break;
                    case 8:
                        //跳转到设置界面
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });
    }

    /**
     * 点击手机防盗的时候,进入的界面
     */
    private void showDialog() {

        String pwd = SpUtils.getString(this, ConstantValue.MOBILE_SAFE_PWD, "");
        if (TextUtils.isEmpty(pwd)) {
            //1.0 初始化密码对话框
            showSetdialog();
        } else {
            //2.0 确认密码的对话框
            showConfirmdialog();

        }
    }

    /**
     * 确认密码的对话框
     */
    private void showConfirmdialog() {
        //设置密码的对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_confirm_pwd, null);
        dialog.setView(view);
        dialog.show();

        //找到我们需要的组件:两个点击的按钮
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        //设置按钮的响应事件
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //找到我们需要的组件:两个密码框
                EditText et_confirm_pwd = (EditText) view.findViewById(R.id.et_confirm_pwd);

                //获得两个密码框的内容
                String confirmPwd = et_confirm_pwd.getText().toString();

                //两个输入框都不为空的
                if (!TextUtils.isEmpty(confirmPwd)) {
                    String pwd = SpUtils.getString(HomeActivity.this, ConstantValue.MOBILE_SAFE_PWD, "");
                    //相等的情况下
                    if (pwd.equals(MD5Utils.encoder(confirmPwd))) {
                        Intent intent = new Intent(HomeActivity.this, SetupOverActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(HomeActivity.this, "密码输入不正确", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "密码输入怎么可能为空呢?傻蛋", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }

    /**
     * 设置密码的对话框
     */
    private void showSetdialog() {
        //设置密码的对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_set_pwd, null);
        dialog.setView(view);
        dialog.show();

        //找到我们需要的组件:两个点击的按钮
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);


        //设置按钮的响应事件
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //找到我们需要的组件:两个密码框
                EditText et_set_pwd = (EditText) view.findViewById(R.id.et_set_pwd);
                EditText et_confirm_pwd = (EditText) view.findViewById(R.id.et_confirm_pwd);

                //获得两个密码框的内容
                String pwd = et_set_pwd.getText().toString();
                String confirmPwd = et_confirm_pwd.getText().toString();

                //两个输入框都不为空的
                if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(confirmPwd)) {

                    //相等的情况下
                    if (pwd.equals(confirmPwd)) {
                        SpUtils.putString(HomeActivity.this,ConstantValue.MOBILE_SAFE_PWD, MD5Utils.encoder(pwd));

                        Intent intent = new Intent(HomeActivity.this, SetupOverActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(HomeActivity.this, "两个密码输入框的输入必须相等", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "密码输入框不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

    }

    /**
     * 初始化UI
     */
    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_home);
    }

    /**
     * 设置gridview的一个适配器,用于展示主界面的程序
     */

    class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mDrawableIds.length;
        }

        @Override
        public Object getItem(int position) {
            return mDrawableIds[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder = null;
            if (convertView == null) {
                view = View.inflate(HomeActivity.this, R.layout.gridview_item, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.iv_icon.setImageResource(mDrawableIds[position]);
//            viewHolder.iv_icon.setBackgroundResource(mDrawableIds[position]);
            viewHolder.tv_title.setText(mTitleStr[position]);

            return view;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_title;
        }
    }
}