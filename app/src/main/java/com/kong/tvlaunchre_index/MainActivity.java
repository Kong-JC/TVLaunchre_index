package com.kong.tvlaunchre_index;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    private ImageView iv_left, iv_center_one, iv_center_two, iv_right,iv_network_icon;
    private TextView tv_date, tv_time;

    private static Intent chrome_intent;
    private static Intent lottery_intent;


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
        chrome_intent = Utils.getAppIntent(this, "com.android.chrome");
        lottery_intent = Utils.getAppIntent(this, "com.example.lottery");
        Utils.autoConnWifi(this);
        
    }

    private void initView() {
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_center_one = (ImageView) findViewById(R.id.iv_center_one);
        iv_center_two = (ImageView) findViewById(R.id.iv_center_two);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_network_icon = (ImageView) findViewById(R.id.iv_network_icon);

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

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkStatusReceiver, filter);
        
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                String url = SPUtils.getString(this, SPUtils.ADVANCED_SETTING_URL_KEY);
                Uri content_url = Uri.parse(url);
                chrome_intent.setData(content_url);
                startActivity(chrome_intent);
                break;
            case R.id.iv_center_one:
                startActivity(lottery_intent);
                break;
            case R.id.iv_center_two:
                new SettingDialog(this, 0, null, null).show();
                break;
            case R.id.iv_right:
                if (SPUtils.getString(this,SPUtils.ADVANCED_SETTINGS_PASSWORD_KEY).length()==0) {
                    SPUtils.putString(this, SPUtils.ADVANCED_SETTINGS_PASSWORD_KEY,"000000");
                }
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

    BroadcastReceiver mNetworkStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo etherNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
                NetworkInfo mobileNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                Log.e(TAG, "mConnectionReceiver...wifiNetInfo=" + wifiNetInfo + "; etherNetInfo=" + etherNetInfo + "; mobileNetInfo=" + mobileNetInfo);
                //网络是否连接
                boolean isConnect = (wifiNetInfo != null && wifiNetInfo.isConnected())
                        || (etherNetInfo != null && etherNetInfo.isConnected())
                        || (mobileNetInfo != null && mobileNetInfo.isConnected());

                if (wifiNetInfo != null && wifiNetInfo.isConnected()) {
                    iv_network_icon.setImageResource(R.drawable.icon_wifi);
                } else if (etherNetInfo != null && etherNetInfo.isConnected()) {
                    iv_network_icon.setImageResource(R.drawable.icon_connected);
                } else if (mobileNetInfo != null && mobileNetInfo.isConnected()) {
                    iv_network_icon.setImageResource(R.drawable.icon_connected);
                } else {
                    iv_network_icon.setImageResource(R.drawable.icon_disconnected);
                }

            }
        }

    };

}
