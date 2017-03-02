package com.minhuizhu.mynewchess.ativity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.minhuizhu.mynewchess.ChessView;
import com.minhuizhu.mynewchess.Position;
import com.minhuizhu.mynewchess.R;
import com.minhuizhu.mynewchess.util.ACache;
import com.minhuizhu.mynewchess.util.MusicManager;
import com.minhuizhu.mynewchess.util.Util;
import com.minhuizhu.mynewchess.widget.SettingDialog;
import com.minhuizhu.mynewchess.widget.Slider;

public class PlayWithComputerActivity extends Activity {


    private static final int REMOVE_STEP = 1;

    private ChessView mChessView;


    private View newGame;
    private View advise;
    private View goBack;
    private View setting;
    private View exit;
    private TextView stepTv;

    static final int RS_DATA_LEN = 512;

    byte[] rsData = new byte[RS_DATA_LEN];
    //moveMode用以判斷所執顏色
    int moveMode, handicap, level, sound, music;
    private boolean started = false;
    private ACache acache;
    private static final String RS_DATA = "rs_data";
    public static Handler handler;
    private static final int THINKING_STEP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_with_computer);
        initView();
        initData();
        addListener();
    }

    private void addListener() {
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        advise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChessView.notifyStep();
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingDialog settingDialog = new SettingDialog(PlayWithComputerActivity.this, MusicManager.getInstance().getVolume(), mChessView.level);
                settingDialog.show();
                settingDialog.setMusicSliderListener(new Slider.OnValueChangedListener() {
                    @Override
                    public void onValueChanged(int value) {
                        MusicManager.getInstance().setVolume(value);
                    }
                });
                settingDialog.setStrengthSliderListener(new Slider.OnValueChangedListener() {
                    @Override
                    public void onValueChanged(int value) {
                        mChessView.level = value;
                        level = value;
                    }
                });
            }
        });
    }

    private void goBack() {
        mChessView.back();
    }

    private void initData() {
        acache = ACache.get(getApplication());
        Position.readBookData(this);
        startApp();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int code = msg.what;
                switch (code) {
                    case THINKING_STEP:
                        showStepResult((String) msg.obj);
                        break;
                    case REMOVE_STEP:
                        removeStep();
                        break;
                }
            }
        };
    }

    private void removeStep() {
        stepTv.setText("");
    }

    private void showStepResult(String message) {
        String initText = stepTv.getText().toString();
        stepTv.setText(initText + message);
    }

    private void startApp() {
        if (started) {
            return;
        }
        started = true;
        rsData = acache.getAsBinary(RS_DATA);
        if (rsData == null || rsData.length != RS_DATA_LEN) {
            initSquareData();
            initSettingData();

        }
        getInitDataFromRsData();
        initBackgroundData();
        mChessView.load(rsData, handicap, moveMode, level, true);
    }

    private void initSettingData() {
        if (rsData == null) {
            rsData = new byte[RS_DATA_LEN];
        }
        rsData[18] = 5;
        rsData[20] = 3;
    }

    private void initView() {
        mChessView = (ChessView) findViewById(R.id.chess_view);
        newGame = findViewById(R.id.new_game);
        advise = findViewById(R.id.advise);
        goBack = findViewById(R.id.go_back);
        setting = findViewById(R.id.setting);
        exit = findViewById(R.id.exit);
        stepTv = (TextView) findViewById(R.id.step);
    }

    @Override
    protected void onDestroy() {
        destroyApp(true);
        super.onDestroy();
    }

    private void newGame() {
        initSquareData();
        moveMode = 1 - moveMode;
        mChessView.load(rsData, handicap, moveMode, level, true);
    }

    private void initSquareData() {
        rsData = new byte[RS_DATA_LEN];
        for (int i = 256; i < RS_DATA_LEN; i++) {
            rsData[i] = 0;
        }
    }

    public void destroyApp(boolean unc) {
        rsData = new byte[RS_DATA_LEN];
        for (int i = 0; i < RS_DATA_LEN; i++) {
            rsData[i] = 0;
        }
        System.arraycopy(mChessView.getSquares(), 0, rsData, 256, 256);

        rsData[16] = (byte) moveMode;
        if (moveMode == 1) {
            rsData[0] = 2;
        }
        rsData[17] = (byte) handicap;
        rsData[18] = (byte) mChessView.level;
        rsData[19] = (byte) sound;
        rsData[20] = (byte) MusicManager.getInstance().getVolume();
        acache.put(RS_DATA, rsData);
        started = false;
    }

    private void initBackgroundData() {
        MusicManager.getInstance().setVolume(music);
    }

    public void getInitDataFromRsData() {
        //需要有設置按鈕
        moveMode = (byte) Util.MIN_MAX(0, rsData[16], 2);
        handicap = (byte) Util.MIN_MAX(0, rsData[17], 3);
        level = rsData[18];
        sound = (byte) Util.MIN_MAX(0, rsData[19], 5);
        music = (byte) Util.MIN_MAX(0, rsData[20], 5);
    }
}















































































