package com.kong.tvlaunchre_index;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private ImageView iv_left, iv_center_one, iv_center_two, iv_right;
    private TextView tv_date, tv_time;

    private static Intent intent;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    tv_date.setText(msg.obj + "");
                    break;
                case 1:
                    tv_time.setText(msg.obj + "");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        updateTime();
        intent = Utils.getAppIntent(this, "com.android.chrome");

    }

    private void initView() {
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_center_one = (ImageView) findViewById(R.id.iv_center_one);
        iv_center_two = (ImageView) findViewById(R.id.iv_center_two);
        iv_right = (ImageView) findViewById(R.id.iv_right);

        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_time = (TextView) findViewById(R.id.tv_time);

        iv_left.setOnClickListener(this);
        iv_center_one.setOnClickListener(this);
        iv_center_two.setOnClickListener(this);
        iv_right.setOnClickListener(this);

        iv_left.setImageResource(R.drawable.icon_app1);
        iv_center_one.setImageResource(R.drawable.icon_app2);
        iv_center_two.setImageResource(R.drawable.icon_app3);
        iv_right.setImageResource(R.drawable.icon_app4);

        onFocusChangeListener(iv_left);
        onFocusChangeListener(iv_center_one);
        onFocusChangeListener(iv_center_two);
        onFocusChangeListener(iv_right);

    }

    private void onFocusChangeListener(ImageView imageView) {
        imageView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.startAnimation(zoomAnimation(1.0f, 1.2f, 100));
                } else {
                    v.startAnimation(zoomAnimation(1.2f, 1.0f, 100));
                }
            }
        });
    }

    public static Animation zoomAnimation(float startScale, float endScale, int duration) {
        ScaleAnimation anim = new ScaleAnimation(startScale, endScale, startScale, endScale,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true);
        anim.setDuration(duration);
        return anim;
    }

//    SettingDialog settingDialog;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
//                Toast.makeText(this, "iv_left", Toast.LENGTH_SHORT).show();

                // LocalAppInfoBean{appLabel='Chrome', appIcon=android.graphics.drawable.BitmapDrawable@41868d38, 
                // intent=Intent { 
                //      act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] flg=0x10000000 pkg=com.android.chrome cmp=com.android.chrome/com.google.android.apps.chrome.Main 
                // }, 
                // pkgName='com.android.chrome', 
                // isAdd=false, listId=0}

                String url = SPUtils.getString(this, SPUtils.ADVANCED_SETTINGS_PASSWORD_KEY);
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);

                break;
            case R.id.iv_center_one:
//                Toast.makeText(this, "iv_center_one", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_center_two:
//                Toast.makeText(this, "iv_center_two", Toast.LENGTH_SHORT).show();
//                SettingDialog settingDialog = new SettingDialog(this, 0, null);
//                settingDialog.show();
                new SettingDialog(this, 0, null, null).show();
                break;
            case R.id.iv_right:
//                Toast.makeText(this, "iv_right", Toast.LENGTH_SHORT).show();
                new InputDialog(this, "高级设置", 0).show();
                break;
        }
    }

    /**
     * 更新时间
     */
    private void updateTime() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String time = "";
                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat format1 = new SimpleDateFormat("MM/dd");
                String s1 = format1.format(calendar.getTime());
                SimpleDateFormat format2 = new SimpleDateFormat("kk:mm");
                String s2 = format2.format(calendar.getTime());

                //一周的第几天
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                switch (dayOfWeek) {
                    case 1:
                        time = s1 + " 星期日";
                        break;
                    case 2:
                        time = s1 + " 星期一";
                        break;
                    case 3:
                        time = s1 + " 星期二";
                        break;
                    case 4:
                        time = s1 + " 星期三";
                        break;
                    case 5:
                        time = s1 + " 星期四";
                        break;
                    case 6:
                        time = s1 + " 星期五";
                        break;
                    case 7:
                        time = s1 + " 星期六";
                        break;
                }
                handler.obtainMessage(0, time).sendToTarget();
                handler.obtainMessage(1, s2).sendToTarget();
            }
        }, 0, 30 * 1000);

    }

}
