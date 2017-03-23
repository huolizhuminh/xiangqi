package com.minhuizhu.engine;

/**
 * Created by zhuminh on 2017/3/21.
 */

public class ChessAPI {
    static {
        System.loadLibrary("chess-engine");
    }

    public static native void clearBoard();
}
