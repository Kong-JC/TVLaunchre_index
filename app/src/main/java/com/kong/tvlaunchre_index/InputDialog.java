package com.kong.tvlaunchre_index;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.kong.tvlaunchre_index.SPUtils.getString;

/**
 * Created by Kong on 2017/4/12.
 */

public class InputDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "InputDialog";

    private Context mContext;
    private TextView tv_title, tv_wifi_info;
    private EditText et_password, et_new_password;
    private Button btn_determine;
    private CheckBox cb_save_wifi;

    private String title;
    private int openTag;

    private ScanResult scanResult;

    private WifiConfiguration wifiConfiguration;
    private WifiManager wifiManager;

    public InputDialog(@NonNull Context context, String title, int openTag) {
        super(context, R.style.dialog_custom);
        this.mContext = context;
        this.title = title;
        if (openTag != 0) this.openTag = openTag;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        setContentView(R.layout.input_dialog_layout);

        initView();

        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 4 / 10; // 设置dialog宽度为屏幕的4/5
        lp.height = display.getHeight() * 4 / 10;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);// 点击Dialog外部消失
    }

    private void initView() {
        tv_title = (TextView) getWindow().findViewById(R.id.tv_title);
        et_password = (EditText) getWindow().findViewById(R.id.et_password);
        et_new_password = (EditText) getWindow().findViewById(R.id.et_new_password);
        btn_determine = (Button) getWindow().findViewById(R.id.btn_determine);
        cb_save_wifi = (CheckBox) getWindow().findViewById(R.id.cb_save_wifi);
        et_new_password.setVisibility(View.GONE);
        cb_save_wifi.setVisibility(View.GONE);
        btn_determine.setOnClickListener(this);
        tv_title.setText(title);

        if (openTag == 2222) {
            et_password.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            et_password.setHint("请输入指定网址");
            et_password.setText(SPUtils.getString(mContext, SPUtils.ADVANCED_SETTING_URL_KEY));
        } else if (openTag == 2) {
            et_password.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            cb_save_wifi.setVisibility(View.VISIBLE);
            cb_save_wifi.setOnClickListener(this);
            et_password.setHint("请输入密码");
            btn_determine.setText("连接");

            if (scanResult != null) {
                wifiConfiguration = new Gson().fromJson(SPUtils.getString(mContext, scanResult.SSID), WifiConfiguration.class);
                wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
//                Log.i(TAG, " -=-=-=-=- initView: ConnectionInfo:" + wifiManager.getConnectionInfo());
                boolean isAutoConn = SPUtils.getBoolean(mContext, scanResult.SSID + "-" + SPUtils.IS_SAVE_WIFI_KEY);
                if (isAutoConn) {
                    et_password.setText(wifiConfiguration.preSharedKey);
                    cb_save_wifi.setChecked(true);
                }
            }

            for (WifiConfiguration configuration : wifiManager.getConfiguredNetworks()) {
                Log.i(TAG, " -=-=-=-=- initView: configuration:" + configuration);
            }

            if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED && wifiManager.getConnectionInfo().getSSID().equals(scanResult.SSID)) {
                btn_determine.setText("断开");
            }

        } else if (openTag == 31) {
            et_password.setInputType(EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
            et_new_password.setInputType(EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
            et_new_password.setVisibility(View.VISIBLE);
            et_password.setHint("请输入旧密码");
            et_new_password.setHint("请输入新密码");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_determine:
                btnClick();
                break;
            case R.id.cb_save_wifi:
                break;
        }
    }

    private void btnClick() {
        switch (openTag) {
            case 0:
                if (et_password.getText().toString().length() != 0) {
                    if (et_password.getText().toString().equals(SPUtils.getString(mContext, SPUtils.ADVANCED_SETTINGS_PASSWORD_KEY))) {
                        List<MyBean> myBeanList = new ArrayList<>();
                        myBeanList.add(new MyBean(1111, "系统设置", "", true));
                        myBeanList.add(new MyBean(2222, "默认网址", getString(mContext, SPUtils.ADVANCED_SETTING_URL_KEY), true));
                        new SettingDialog(mContext, 1111, "高级设置", myBeanList).show();
                        dismiss();
                    } else {
                        Toast.makeText(mContext, "密码错误！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2222:
                SPUtils.putString(mContext, SPUtils.ADVANCED_SETTING_URL_KEY, et_password.getText().toString());
                dismiss();
                List<MyBean> myBeanList = new ArrayList<>();
                myBeanList.add(new MyBean(1111, "系统设置", "", true));
                myBeanList.add(new MyBean(2222, "默认网址", getString(mContext, SPUtils.ADVANCED_SETTING_URL_KEY), true));
                new SettingDialog(mContext, 1111, "高级设置", myBeanList).show();
                break;
            case 2:
                if (scanResult != null) {
                    // syy12345
                    wifiConfiguration = new Gson().fromJson(SPUtils.getString(mContext, scanResult.SSID), WifiConfiguration.class);
                    wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
//                    if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {

                    int netId = wifiManager.addNetwork(wifiConfiguration);

//                    Log.i(TAG, " -=-=-=-=- btnClick: configuration:" + wifiConfiguration.toString());
//                    Log.i(TAG, " -=-=-=-=- btnClick: getConnectionInfo:" + wifiManager.getConnectionInfo().toString());
//                    Log.i(TAG, " -=-=-=-=- btnClick: manager:" + wifiManager);
//                    Log.i(TAG, " -=-=-=-=- btnClick: manager:" + wifiManager.getWifiState());

                    if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                        boolean isDisconnect = Utils.disconnectWifi(mContext, netId);
                        Toast.makeText(mContext, isDisconnect ? "断开成功" : "断开失败", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean isConn = Utils.connWiFi(mContext, scanResult, et_password.getText().toString(), cb_save_wifi.isChecked());
                        if (!cb_save_wifi.isChecked()) {
                            SPUtils.putString(mContext, scanResult.SSID, "");
                            SPUtils.putBoolean(mContext, scanResult.SSID + "-" + SPUtils.IS_SAVE_WIFI_KEY, false);
                        }
                        Log.i(TAG, " -=-=-=-=- btnClick: 是否连接成功:" + isConn);
                    }

                }
                break;
            case 31:
                if (et_password.getText().toString().length() != 0) {
                    if (et_password.getText().toString().equals(SPUtils.getString(mContext, SPUtils.ADVANCED_SETTINGS_PASSWORD_KEY))) {
                        boolean isSuccess = SPUtils.putString(mContext, SPUtils.ADVANCED_SETTINGS_PASSWORD_KEY, et_new_password.getText() + "");
                        Toast.makeText(mContext, isSuccess ? "密码设置成功" : "密码设置失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "旧密码错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
                }
                dismiss();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (openTag == 2222 && et_password.getText().toString().equals(getString(mContext, SPUtils.ADVANCED_SETTING_URL_KEY))) {
                List<MyBean> myBeanList = new ArrayList<>();
                myBeanList.add(new MyBean(1111, "系统设置", "", true));
                myBeanList.add(new MyBean(2222, "默认网址", getString(mContext, SPUtils.ADVANCED_SETTING_URL_KEY), true));
                new SettingDialog(mContext, 11111, "高级设置", myBeanList).show();
                dismiss();
            }
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }

}
