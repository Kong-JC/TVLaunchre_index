package com.kong.tvlaunchre_index;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    ImageView iv_left, iv_center_one, iv_center_two, iv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_center_one = (ImageView) findViewById(R.id.iv_center_one);
        iv_center_two = (ImageView) findViewById(R.id.iv_center_two);
        iv_right = (ImageView) findViewById(R.id.iv_right);

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

    public static Animation zoomAnimation(float startScale, float endScale, int duration){
        ScaleAnimation anim = new ScaleAnimation(startScale, endScale, startScale, endScale,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true);
        anim.setDuration(duration);
        return anim;
    }

    MyDialog myDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                Toast.makeText(this, "iv_left", Toast.LENGTH_SHORT).show();

                myDialog = new MyDialog(this,R.style.dialog_custom);
//                myDialog.showDialog();

                break;
            case R.id.iv_center_one:
                Toast.makeText(this, "iv_center_one", Toast.LENGTH_SHORT).show();
                myDialog.setLayout(R.layout.dialog_one_layout);
                myDialog.show();
                break;
            case R.id.iv_center_two:
                Toast.makeText(this, "iv_center_two", Toast.LENGTH_SHORT).show();

                myDialog.

                break;
            case R.id.iv_right:
                Toast.makeText(this, "iv_right", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
