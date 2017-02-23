package com.minhuizhu.mynewchess.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.minhuizhu.mynewchess.R;

/**
 * Created by zhuminh on 2017/2/21.
 */

public class SettingDialog extends Dialog {

    private Slider strength;
    private TextView strenghtShow;
    private Slider music;
    private TextView musicShow;
    private Slider.OnValueChangedListener musicSliderListener;
    private Slider.OnValueChangedListener strengthSliderListener;
    private int musicValue;
    private int strengthValue;

    public void setMusicSliderListener(Slider.OnValueChangedListener musicSliderListener) {
        this.musicSliderListener = musicSliderListener;
    }

    public void setStrengthSliderListener(Slider.OnValueChangedListener strengthSliderListener) {
        this.strengthSliderListener = strengthSliderListener;
    }

    public SettingDialog(Context context, int musicValue, int strengthValue) {
        this(context, R.style.MyDialog);
        this.musicValue = musicValue;
        this.strengthValue = strengthValue;
    }

    public SettingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        strength = (Slider) findViewById(R.id.strength);
        strenghtShow = (TextView) findViewById(R.id.strength_show);
        music = (Slider) findViewById(R.id.music);
        musicShow = (TextView) findViewById(R.id.music_show);
        strength.setOnValueChangedListener(new Slider.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                if (strengthSliderListener != null) {
                    strengthSliderListener.onValueChanged(value);
                }
                strenghtShow.setText(getLevel(value));

            }
        });

        music.setOnValueChangedListener(new Slider.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                if (musicSliderListener != null) {
                    musicSliderListener.onValueChanged(value);
                }
                musicShow.setText(String.valueOf(value));
            }
        });
        strength.setValue(strengthValue);
        music.setValue(musicValue);
        strenghtShow.setText(getLevel(strengthValue));
        musicShow.setText(String.valueOf(musicValue) );

    }

    private String getLevel(int value) {
        switch (value) {
            case 0:
                return "初级";
            case 1:
                return "中级";
            case 2:
                return "高级";
            default:
                return "初级";
        }

    }


}
