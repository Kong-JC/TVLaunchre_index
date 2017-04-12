package com.kong.tvlaunchre_index;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kong on 2017/4/11.
 */

public class SettingDialog extends Dialog {

    private static final String TAG = "SettingDialog";

    private Context mContext;
    private RecyclerView rv_dialog_recycler_view;
    private TextView tv_dialog_title;

    private RecyclerViewAdapter recyclerViewAdapter;

    private int openTag;
    private List<MyBean> myBeanList;
    private String dialogTitle;

    private int returnIndex = 0;

    public SettingDialog(@NonNull Context mContext, int openTag, String dialogTitle, List<MyBean> myBeanList) {
        super(mContext, R.style.dialog_custom);
        this.mContext = mContext;
        if (openTag != 0 && myBeanList != null) {
            this.openTag = openTag;
            this.myBeanList = myBeanList;
            this.dialogTitle = dialogTitle;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        setContentView(R.layout.setting_dialog_layout);

        initView();

        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 9 / 10; // 设置dialog宽度为屏幕的4/5
        lp.height = display.getHeight() * 9 / 10;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);// 点击Dialog外部消失

        if (openTag == 0 && myBeanList == null) {
            myBeanList = new ArrayList<>();
            myBeanList.add(new MyBean(1, "网络", "已连接", true));
            myBeanList.add(new MyBean(2, "显示", "", true));
            myBeanList.add(new MyBean(3, "高级", "", true));
            myBeanList.add(new MyBean(4, "其他", "", true));
        } else {
            tv_dialog_title.setText(dialogTitle);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(myBeanList);
        rv_dialog_recycler_view = (RecyclerView) getWindow().findViewById(R.id.rv_dialog_recycler_view);

        rv_dialog_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rv_dialog_recycler_view.setAdapter(recyclerViewAdapter);
        rv_dialog_recycler_view.setFocusable(true);
//        rv_dialog_recycler_view.setFocusableInTouchMode(true);

        rv_dialog_recycler_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (rv_dialog_recycler_view.getChildCount() > 0) {
                        rv_dialog_recycler_view.getChildAt(returnIndex).requestFocus();
                    }
                }
            }
        });

        recyclerViewAdapter.setAdapterOnClickListener(new RecyclerViewAdapter.AdapterOnClickListener() {
            @Override
            public void onClick(MyBean myBean, int position) {
//                Toast.makeText(mContext, "MyBean:" + myBean.toString(), Toast.LENGTH_SHORT).show();
                if (myBean.scanResult != null) {
//                    WifiManager manager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
//                    WifiConfiguration config = new WifiConfiguration();
//                    config.SSID = "\"WIFI_NAME\"";
//                    config.preSharedKey = "\"PASSWORD\"";
//                    config.hiddenSSID = true;
//                    config.status = WifiConfiguration.Status.ENABLED;
//                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//                    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//                    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//                    config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
//                    int netId = manager.addNetwork(config);
//                    boolean isConn = manager.enableNetwork(netId, true);
//                    Log.i(TAG, " -=-=-=-=- onClick: 是否连接成功:" + isConn);

                    InputDialog inputDialog = new InputDialog(mContext, myBean.scanResult.SSID, 2);
                    inputDialog.setScanResult(myBean.scanResult);
                    inputDialog.show();

                }
                loadNext(myBean);
            }
        });

    }

    private void initView() {
        tv_dialog_title = (TextView) getWindow().findViewById(R.id.tv_dialog_title);
    }

    private void loadNext(MyBean myBean) {
        switch (myBean.tag) {
            case 0:
                myBeanList = new ArrayList<>();

                if (Utils.getWiFiIsConn(mContext)) {
                    myBeanList.add(new MyBean(1, "网络", "已连接", true));
                } else if (Utils.getEthernetIsConn(mContext)) {
                    myBeanList.add(new MyBean(1, "网络", "已接入", true));
                }

                myBeanList.add(new MyBean(2, "显示", "", true));
                myBeanList.add(new MyBean(3, "高级", "", true));
                myBeanList.add(new MyBean(4, "其他", "", true));
                changeList("系统设置", myBeanList);
                break;
            case 1:
                myBeanList = new ArrayList<>();

                if (Utils.getWiFiIsConn(mContext)) {
                    myBeanList.add(new MyBean(11, "无线网络", Utils.getWiFiSSID(mContext), true));
                } else {
                    myBeanList.add(new MyBean(11, "无线网络", Utils.wifiIsOpen(mContext) ? "开启" : "关闭", true));
                }

                myBeanList.add(new MyBean(12, "有线网络", "", true));
                changeList("网络设置", myBeanList);
                break;
            case 2:
                myBeanList = new ArrayList<>();
                myBeanList.add(new MyBean(21, "屏幕缩放", "百分百", true));
                myBeanList.add(new MyBean(22, "屏幕方向", "横向", true));
                myBeanList.add(new MyBean(23, "最佳显示模式", "开", true));
                myBeanList.add(new MyBean(24, "自定义输出模式", "720p-60hz", true));
                changeList("显示设置", myBeanList);
                break;
            case 3:
                myBeanList = new ArrayList<>();
                myBeanList.add(new MyBean(31, "密码设置", "", true));
                myBeanList.add(new MyBean(32, "手机遥控", SPUtils.getBoolean(mContext, SPUtils.MOBILE_TELECONTROL) ? "开" : "关", true));
                myBeanList.add(new MyBean(33, "Google TV 遥控", SPUtils.getBoolean(mContext, SPUtils.GOOGLE_TV_TELECONTROL) ? "开" : "关", true));
                myBeanList.add(new MyBean(34, "自动切换数字音频输出", SPUtils.getBoolean(mContext, SPUtils.AUTOMATICALLY_SWITCH_THE_DIGITAL_AUDIO_OUTPUT) ? "开" : "关", true));
                String s = SPUtils.getString(mContext, SPUtils.SOUND_SET);
                if (s.equals("")) {
                    s = "PCM";
                }
                myBeanList.add(new MyBean(35, "声音设置", s, true));
                changeList("高级设置", myBeanList);
                break;
            case 4:
                myBeanList = new ArrayList<>();
                myBeanList.add(new MyBean(41, "型号", Utils.getModel(), false));
                myBeanList.add(new MyBean(42, "Android 版本", Utils.getRelease(), false));
                myBeanList.add(new MyBean(43, "版本号", Utils.getRomInfo(), false));
                myBeanList.add(new MyBean(44, "内核版本", Utils.getUUID(), false));
                changeList("其他设置", myBeanList);
                break;
            case 11:
                myBeanList = new ArrayList<>();

                myBeanList.add(new MyBean(111, "无线网络", Utils.wifiIsOpen(mContext) ? "开启" : "关闭", true));

                MyBean myBean_ = null;
                for (ScanResult scanResult : Utils.getWiFiSignalSourceList(mContext)) {
                    if (scanResult.level >= 0 && scanResult.level <= 50) {
                        myBean_ = new MyBean(112, scanResult.SSID, "强", true);
                        myBean_.scanResult = scanResult;
                    } else if (scanResult.level <= 0 && scanResult.level >= -50) {
                        myBean_ = new MyBean(112, scanResult.SSID, "中", true);
                        myBean_.scanResult = scanResult;
                    } else if (scanResult.level <= -51 && scanResult.level >= -200) {
                        myBean_ = new MyBean(112, scanResult.SSID, "弱", true);
                        myBean_.scanResult = scanResult;
                    }
                    myBeanList.add(myBean_);
                }

                changeList("无线网络", myBeanList);
                break;
            case 12:
                myBeanList = new ArrayList<>();
                myBeanList.add(new MyBean(121, "以太网", Utils.getEthernetIsConn(mContext) ? "已接入" : "未接入", false));
                myBeanList.add(new MyBean(122, "IP", Utils.getEthernetIPAddress(true), false));
                myBeanList.add(new MyBean(123, "MAC", Utils.getEthernetMac(), false));
                changeList("有线网络", myBeanList);
                break;
            case 1111:
                mContext.startActivity(new Intent(Settings.ACTION_SETTINGS));
                break;
            case 2222:
                new InputDialog(mContext, "默认打开网址", 2222).show();
                dismiss();
                break;
            case 31:
                new InputDialog(mContext, "设置新密码", 31).show();
                break;
            case 32:
//                new InputDialog(mContext,"设置新密码",31).show();
                changeItem(myBean);
//                if (myBean.right_text.equals("开")) {
//                    myBean.right_text = "关";
//                    SPUtils.putBoolean(mContext, SPUtils.MOBILE_TELECONTROL, false);
//                } else {
//                    myBean.right_text = "开";
//                    SPUtils.putBoolean(mContext, SPUtils.MOBILE_TELECONTROL, true);
//                }
//                returnIndex = recyclerViewAdapter.changeItem(myBean);
                break;
            case 33:
//                new InputDialog(mContext,"设置新密码",31).show();
                changeItem(myBean);
//                if (myBean.right_text.equals("开")) {
//                    myBean.right_text = "关";
//                    SPUtils.putBoolean(mContext, SPUtils.GOOGLE_TV_TELECONTROL, false);
//                } else {
//                    myBean.right_text = "开";
//                    SPUtils.putBoolean(mContext, SPUtils.GOOGLE_TV_TELECONTROL, true);
//                }
//                returnIndex = recyclerViewAdapter.changeItem(myBean);
                break;
            case 34:
//                new InputDialog(mContext,"设置新密码",31).show();
                changeItem(myBean);
//                if (myBean.right_text.equals("开")) {
//                    myBean.right_text = "关";
//                    SPUtils.putBoolean(mContext, SPUtils.AUTOMATICALLY_SWITCH_THE_DIGITAL_AUDIO_OUTPUT, false);
//                } else {
//                    myBean.right_text = "开";
//                    SPUtils.putBoolean(mContext, SPUtils.AUTOMATICALLY_SWITCH_THE_DIGITAL_AUDIO_OUTPUT, true);
//                }
//                returnIndex = recyclerViewAdapter.changeItem(myBean);
                break;
            case 35:
//                new InputDialog(mContext,"设置新密码",31).show();
                changeItem(myBean);
//                if (myBean.right_text.equals("开")) {
//                    myBean.right_text = "关";
//                    SPUtils.putBoolean(mContext, SPUtils.SOUND_SET, false);
//                } else {
//                    myBean.right_text = "开";
//                    SPUtils.putBoolean(mContext, SPUtils.SOUND_SET, true);
//                }
//                returnIndex = recyclerViewAdapter.changeItem(myBean);
                break;
        }
    }

    private void changeItem(MyBean myBean) {
        String key = "";
        switch (myBean.tag) {
            case 31:
                key = SPUtils.MOBILE_TELECONTROL;
                break;
            case 32:
                key = SPUtils.GOOGLE_TV_TELECONTROL;
                break;
            case 33:
                key = SPUtils.AUTOMATICALLY_SWITCH_THE_DIGITAL_AUDIO_OUTPUT;
                break;
            case 34:
                key = SPUtils.SOUND_SET;
                break;
        }
        switch (myBean.right_text) {
            case "开":
                myBean.right_text = "关";
                SPUtils.putBoolean(mContext, key, false);
                break;
            case "关":
                myBean.right_text = "开";
                SPUtils.putBoolean(mContext, key, true);
                break;
            case "HDMI":
                myBean.right_text = "PCM";
                SPUtils.putString(mContext, key, myBean.right_text);
                break;
            case "PCM":
                myBean.right_text = "HDMI";
                SPUtils.putString(mContext, key, myBean.right_text);
                break;
        }
//        if (myBean.right_text.equals("开")) {
//            myBean.right_text = "关";
//            SPUtils.putBoolean(mContext, key, false);
//        } else if (){
//            myBean.right_text = "开";
//            SPUtils.putBoolean(mContext, key, true);
//        }
//        if (myBean.right_text.equals("PCM")) {
//            myBean.right_text = "HDMI";
//            SPUtils.putString(mContext, key, myBean.right_text);
//        } else {
//            myBean.right_text = "PCM";
//            SPUtils.putString(mContext, key, myBean.right_text);
//        }
        returnIndex = recyclerViewAdapter.changeItem(myBean);
    }

    private void changeList(String title, List<MyBean> myBeanList) {
        tv_dialog_title.setText(title);
        recyclerViewAdapter.removeAllItem();
        for (int i = 0; i < myBeanList.size(); i++) {
            recyclerViewAdapter.addItem(myBeanList.get(i));
        }
        returnIndex = 0;
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int tag = recyclerViewAdapter.getMyBeanList().get(0).tag;
            char[] charArray = String.valueOf(tag).toCharArray();
            int t = Integer.valueOf(String.valueOf(charArray[0] + ""));
            if (charArray.length == 1) {
                if (t == 1) {
                    return super.onKeyDown(keyCode, event);
                }
            } else if (charArray.length == 2) {
                loadNext(new MyBean(0, "", "", false));
                returnIndex = t - 1;
            } else if (charArray.length == 3) {
                loadNext(new MyBean(t, "", "", false));
                returnIndex = Integer.valueOf(String.valueOf(charArray[1] + "")) - 1;
            } else if (tag == 1111) {
                return super.onKeyDown(keyCode, event);
            }
        }
//        return super.onKeyDown(keyCode, event);
        return false;
    }


}
