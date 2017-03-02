package com.minhuizhu.mynewchess.ativity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.minhuizhu.mynewchess.R;
import com.minhuizhu.mynewchess.widget.DecorationChessView;

/**
 * Created by zhuminh on 2017/2/28.
 */

public class DecorationActivity extends Activity {

    private DecorationChessView decorationView;
    private View redFirst;
    private View blackFirst;
    private View beginGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoration);
        initView();
        addListener();
    }

    private void addListener() {
        blackFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decorationView.setMoveMode(0);
            }
        });
        redFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decorationView.setMoveMode(1);
            }
        });
    }

    private void initView() {
        decorationView = (DecorationChessView) findViewById(R.id.decoration_view);
        redFirst = findViewById(R.id.red_first);
        blackFirst = findViewById(R.id.black_first);
        beginGame = findViewById(R.id.begin_game);
        decorationView.load();
    }

}
