package com.minhuizhu.mynewchess;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.minhuizhu.mynewchess.util.ACache;
import com.minhuizhu.mynewchess.util.Util;

public class MainActivity extends Activity {

    private static final String RS_DATA = "rs_data";
    private ChessView mChessView;
    private boolean started = false;
    static final String[] SOUND_NAME = {"click", "illegal", "move", "move2",
            "capture", "capture2", "check", "check2", "win", "draw", "loss",};

    static final int RS_DATA_LEN = 512;

    byte[] rsData = new byte[RS_DATA_LEN];
    //moveMode用以判斷所執顏色
    int moveMode, handicap, level, sound, music;
    private View newGame;
    private View advise;
    private View goBack;
    private View setting;
    private View exit;
    private View step;
    private ACache acache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

    private void goBack() {
        mChessView.back();
    }

    private void initData() {
        acache = ACache.get(getApplication());
        Position.readBookData(this);
        startApp();
    }

    private void initView() {
        mChessView = (ChessView) findViewById(R.id.chess_view);
        newGame = findViewById(R.id.new_game);
        advise = findViewById(R.id.advise);
        goBack = findViewById(R.id.go_back);
        setting = findViewById(R.id.setting);
        exit = findViewById(R.id.exit);
        step = findViewById(R.id.step);
    }


    private void startApp() {
        if (started) {
            return;
        }
        started = true;
        rsData = acache.getAsBinary(RS_DATA);
        if (rsData == null || rsData.length != RS_DATA_LEN) {
            initRsData();
            getInitDataFromRsData();
        }
        mChessView.load(rsData, handicap, moveMode, level, true);

    }

    @Override
    protected void onDestroy() {
        destroyApp(true);
        super.onDestroy();
    }

    private void newGame() {
        initRsData();
        moveMode = 1 - moveMode;
        mChessView.load(rsData, handicap, moveMode, level, true);
    }

    private void initRsData() {
        rsData = new byte[RS_DATA_LEN];
        for (int i = 0; i < RS_DATA_LEN; i++) {
            rsData[i] = 0;
        }
        rsData[19] = 3;
        rsData[20] = 2;
    }

    public void destroyApp(boolean unc) {
        rsData = new byte[RS_DATA_LEN];
        for (int i = 0; i < RS_DATA_LEN; i++) {
            rsData[i] = 0;
        }
        System.arraycopy(mChessView.getSquares(), 0, rsData, 256, 256);
        rsData[0] = 1;
        rsData[16] = (byte) moveMode;
        rsData[17] = (byte) handicap;
        rsData[18] = (byte) level;
        rsData[19] = (byte) sound;
        rsData[20] = (byte) music;
        acache.put(RS_DATA, rsData);
        started = false;
    }

    public void getInitDataFromRsData() {
        //需要有設置按鈕
        moveMode = (byte) Util.MIN_MAX(0, rsData[16], 2);
        handicap = (byte) Util.MIN_MAX(0, rsData[17], 3);
        level = (byte) Util.MIN_MAX(0, rsData[18], 2);
        sound = (byte) Util.MIN_MAX(0, rsData[19], 5);
        music = (byte) Util.MIN_MAX(0, rsData[20], 5);
    }
}















































































