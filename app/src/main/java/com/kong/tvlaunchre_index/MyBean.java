package com.kong.tvlaunchre_index;

import android.net.wifi.ScanResult;

/**
 * Created by Kong on 2017/4/11.
 */

public class MyBean {
    
    public int tag;
    public String left_text;
    public String right_text;
    public boolean isIcon;
    public ScanResult scanResult;

    public MyBean(int tag, String left_text, String right_text, boolean isIcon) {
        this.tag = tag;
        this.left_text = left_text;
        this.right_text = right_text;
        this.isIcon = isIcon;
    }

    @Override
    public String toString() {
        return "MyBean{" +
                "tag=" + tag +
                ", left_text='" + left_text + '\'' +
                ", right_text='" + right_text + '\'' +
                ", isIcon=" + isIcon +
                ", scanResult=" + scanResult +
                '}';
    }
}
