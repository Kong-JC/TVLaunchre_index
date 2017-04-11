package com.kong.tvlaunchre_index;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kong on 2017/4/11.
 */

public class MyDialog extends Dialog {

    private static final String TAG = "MyDialog";

    private Context context;
    private RecyclerView rv_dialog_recycler_view;
    private TextView tv_dialog_title;

    private RecyclerViewAdapter recyclerViewAdapter;

    public MyDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
//        view = LayoutInflater.from(context).inflate(R.layout.dialog_layout,null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        setContentView(R.layout.dialog_layout);

        initView();

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 9 / 10; // 设置dialog宽度为屏幕的4/5
        lp.height = display.getHeight() * 9 / 10;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);// 点击Dialog外部消失

        final List<MyBean> myBeanList = new ArrayList<>();
        myBeanList.add(new MyBean(1, "网络", "已连接", true));
        myBeanList.add(new MyBean(2, "显示", "", true));
        myBeanList.add(new MyBean(3, "高级", "", true));
        myBeanList.add(new MyBean(4, "其他", "", true));

        recyclerViewAdapter = new RecyclerViewAdapter(myBeanList);
        rv_dialog_recycler_view = (RecyclerView) getWindow().findViewById(R.id.rv_dialog_recycler_view);

        rv_dialog_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv_dialog_recycler_view.setAdapter(recyclerViewAdapter);
        rv_dialog_recycler_view.setFocusable(true);

        rv_dialog_recycler_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (rv_dialog_recycler_view.getChildCount() > 0) {
                        rv_dialog_recycler_view.getChildAt(0).requestFocus();
                    }
                }
            }
        });

        recyclerViewAdapter.setAdapterOnClickListener(new RecyclerViewAdapter.AdapterOnClickListener() {
            @Override
            public void onClick(MyBean myBean, int position) {
                Toast.makeText(context, "MyBean:" + myBean.toString(), Toast.LENGTH_SHORT).show();
                loadNext(myBean.tag);
            }
        });

    }

    private void initView() {
        tv_dialog_title = (TextView) getWindow().findViewById(R.id.tv_dialog_title);
    }

    List<MyBean> myBeanList;

    private void loadNext(int tag) {
        switch (tag) {
            case 0:
                myBeanList = new ArrayList<>();
                myBeanList.add(new MyBean(1, "网络", "已连接", true));
                myBeanList.add(new MyBean(2, "显示", "", true));
                myBeanList.add(new MyBean(3, "高级", "", true));
                myBeanList.add(new MyBean(4, "其他", "", true));
                changeList("系统设置", myBeanList);
                break;
            case 1:
                myBeanList = new ArrayList<>();
                myBeanList.add(new MyBean(11, "无线网络", "WIFI-xxx", true));
                myBeanList.add(new MyBean(12, "有线网络", "", true));
                changeList("网络设置", myBeanList);
                break;
            case 2:
                myBeanList = new ArrayList<>();
                myBeanList.add(new MyBean(21, "屏幕缩放", "百分百", true));
                myBeanList.add(new MyBean(22, "屏幕方向", "横向", true));
                myBeanList.add(new MyBean(22, "最佳显示模式", "开关", true));
                myBeanList.add(new MyBean(22, "自定义输出模式", "720p-60hz", true));
                changeList("显示设置", myBeanList);
                break;
            case 3:
                myBeanList = new ArrayList<>();
                myBeanList.add(new MyBean(31, "密码设置", "弹出窗口设置", true));
                myBeanList.add(new MyBean(32, "手机遥控", "开关", true));
                myBeanList.add(new MyBean(32, "Google TV 遥控", "开关", true));
                myBeanList.add(new MyBean(32, "自动切换数字音频输出", "开关", true));
                myBeanList.add(new MyBean(32, "声音设置", "PCM | HDMI", true));
                changeList("高级设置", myBeanList);
                break;
            case 4:
                myBeanList = new ArrayList<>();
                myBeanList.add(new MyBean(31, "型号", "信息", true));
                myBeanList.add(new MyBean(32, "Android 版本", "信息", true));
                myBeanList.add(new MyBean(32, "版本号", "信息", true));
                myBeanList.add(new MyBean(32, "内核版本", "信息", true));
                changeList("其他设置", myBeanList);
                break;
            case 11:
                myBeanList = new ArrayList<>();

                myBeanList.add(new MyBean(111, "无线网络", Utils.wifiIsOpen(context) ? "开启" : "关闭", true));
                myBeanList.add(new MyBean(112, "WIFI - xxx - 01", "已链接", true));
                myBeanList.add(new MyBean(113, "WIFI - xxx - 01", "已保存", true));
                myBeanList.add(new MyBean(114, "WIFI - xxx - 01", "WPA2-PSK", true));
                changeList("无线网络", myBeanList);
                break;
            case 12:
                myBeanList = new ArrayList<>();
                myBeanList.add(new MyBean(121, "以太网", "已接入", true));
                myBeanList.add(new MyBean(122, "IP", "192.xxx.xxx.xxx", true));
                myBeanList.add(new MyBean(123, "MAC", "xx.xx.xx.xx.xx.xx", true));
                changeList("有线网络", myBeanList);
                break;
        }
    }

    private void changeList(String title, List<MyBean> myBeanList) {
        tv_dialog_title.setText(title);
        recyclerViewAdapter.removeAllItem();
        for (int i = 0; i < myBeanList.size(); i++) {
            recyclerViewAdapter.addItem(myBeanList.get(i), i);
        }
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
                loadNext(0);
            } else if (charArray.length == 3) {
                loadNext(t);
            }
        }
//        return super.onKeyDown(keyCode, event);
        return false;
    }


}
