package com.mrwang.ffmpegdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

/**
 * 注意:FFmpeg必须是3.2 版本,NDK必须是R12b的版本 否则编译不通过
 * 迈开了FFmpeg的第一步
 */
public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        final TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());

        final EditText editText = findViewById(R.id.edit);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tv.setText(avcodecinfo());
                //String trim = editText.getText().toString().trim();
                //tv.setText("字符串长度=" + getStringLength(trim));

                ScaleAnimation animation1 = new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation1.setInterpolator(new DecelerateInterpolator());
                animation1.setRepeatMode(Animation.REVERSE);
                animation1.setRepeatCount(1);
                animation1.setFillAfter(true);
                animation1.setDuration(1000);
//                animation1.setAnimationListener(new Animation.AnimationListener() {
//                });

//                ScaleAnimation animation2 = new ScaleAnimation(1.0f, 0.6f, 1.0f, 0.6f,
//                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                animation2.setInterpolator(new DecelerateInterpolator());
//                //animation2.setRepeatMode(Animation.REVERSE);
//                animation2.setDuration(1000);
//                animation2.setFillAfter(true);
//                animation2.setStartOffset(1000);
//
//                AnimationSet set=new AnimationSet(true);
//                set.addAnimation(animation1);
//                set.addAnimation(animation2);
//                set.setDuration(3000L);
                v.startAnimation(animation1);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {
                    if (getStringLength(s.toString()) > 12) {
                        editText.setText(substringWithIOS(s.toString(), 12));
                    }
                }
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native String avcodecinfo();

    public static int getStringLength(String input) {
        int resultLen = 0;
        int orignLen = input.length();
        String temp = null;
        for (int i = 0; i < orignLen; i++) {
            temp = input.substring(i, i + 1);
            try {
                if (temp.getBytes("utf-8").length == 3) {
                    resultLen += 2;
                } else {
                    resultLen++;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return resultLen;
    }

    public static String substringWithIOS(String input, int max) {
        int resultLen = 0;
        int orignLen = input.length();
        String temp;
        for (int i = 0; i < orignLen; i++) {
            temp = input.substring(i, i + 1);
            try {
                if (temp.getBytes("utf-8").length == 3) {
                    resultLen += 2;
                } else {
                    resultLen++;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (resultLen >= max) {
                return input.substring(0, i+1);
            }
        }
        return input;
    }
}
