package com.kong.tvlaunchre_index;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by Kong on 2017/4/11.
 */

public class MyDialog extends Dialog {

    private Context context;

    public MyDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public void setLayout(int layoutResId){
//        setContentView(R.layout.dialog_one_layout);
        setContentView(layoutResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
//        window.setWindowAnimations(R.style.bottom_menu_animation); // 添加动画效果
//        setContentView(R.layout.dialog_one_layout);

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 4 / 5; // 设置dialog宽度为屏幕的4/5
        lp.height = display.getHeight() * 4 / 5;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);// 点击Dialog外部消失

        //遍历控件id,添加点击事件
//        for (int id : listenedItems) {
//            findViewById(id).setOnClickListener(this);
//        }

    }

}
