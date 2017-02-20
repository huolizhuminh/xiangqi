package com.minhuizhu.mynewchess;

import android.content.Context;
import android.content.res.AssetManager;

import com.minhuizhu.mynewchess.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by zhuminh on 2017/2/2.
 */
public class Position {
    public static final byte[] IN_BOARD = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
            0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0,};              //用于判断是否在棋盘范围内

    public static final byte[] IN_FORT = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0,};//帅和士的范围

    public static final byte[] LEGAL_SPAN = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 2, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};  //将帅士象的每一步的跳跃范围

    public static final byte[] KNIGHT_PIN = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -16, 0, -16, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16, 0, 16, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};//马走的每一步的准确信
    public static final short[][] PIECE_VALUE = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 9, 9, 11, 13, 11, 9, 9, 9, 0,
                    0, 0, 0, 0, 0, 0, 19, 24, 34, 42, 44, 42, 34, 24, 19, 0, 0,
                    0, 0, 0, 0, 0, 19, 24, 32, 37, 37, 37, 32, 24, 19, 0, 0, 0,
                    0, 0, 0, 0, 19, 23, 27, 29, 30, 29, 27, 23, 19, 0, 0, 0, 0,
                    0, 0, 0, 14, 18, 20, 27, 29, 27, 20, 18, 14, 0, 0, 0, 0, 0,
                    0, 0, 7, 0, 13, 0, 16, 0, 13, 0, 7, 0, 0, 0, 0, 0, 0, 0, 7,
                    0, 7, 0, 15, 0, 7, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                    1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 15, 11, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 20, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 18, 0, 0, 20, 23, 20, 0, 0, 18, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20,
                    20, 0, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 20, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 18, 0, 0, 20, 23, 20, 0, 0, 18, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20,
                    20, 0, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 90, 90, 90, 96, 90, 96, 90, 90,
                    90, 0, 0, 0, 0, 0, 0, 0, 90, 96, 103, 97, 94, 97, 103, 96,
                    90, 0, 0, 0, 0, 0, 0, 0, 92, 98, 99, 103, 99, 103, 99, 98,
                    92, 0, 0, 0, 0, 0, 0, 0, 93, 108, 100, 107, 100, 107, 100,
                    108, 93, 0, 0, 0, 0, 0, 0, 0, 90, 100, 99, 103, 104, 103,
                    99, 100, 90, 0, 0, 0, 0, 0, 0, 0, 90, 98, 101, 102, 103,
                    102, 101, 98, 90, 0, 0, 0, 0, 0, 0, 0, 92, 94, 98, 95, 98,
                    95, 98, 94, 92, 0, 0, 0, 0, 0, 0, 0, 93, 92, 94, 95, 92,
                    95, 94, 92, 93, 0, 0, 0, 0, 0, 0, 0, 85, 90, 92, 93, 78,
                    93, 92, 90, 85, 0, 0, 0, 0, 0, 0, 0, 88, 85, 90, 88, 90,
                    88, 90, 85, 88, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//马
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 206, 208, 207, 213, 214, 213,
                    207, 208, 206, 0, 0, 0, 0, 0, 0, 0, 206, 212, 209, 216,
                    233, 216, 209, 212, 206, 0, 0, 0, 0, 0, 0, 0, 206, 208,
                    207, 214, 216, 214, 207, 208, 206, 0, 0, 0, 0, 0, 0, 0,
                    206, 213, 213, 216, 216, 216, 213, 213, 206, 0, 0, 0, 0, 0,
                    0, 0, 208, 211, 211, 214, 215, 214, 211, 211, 208, 0, 0, 0,
                    0, 0, 0, 0, 208, 212, 212, 214, 215, 214, 212, 212, 208, 0,
                    0, 0, 0, 0, 0, 0, 204, 209, 204, 212, 214, 212, 204, 209,
                    204, 0, 0, 0, 0, 0, 0, 0, 198, 208, 204, 212, 212, 212,
                    204, 208, 198, 0, 0, 0, 0, 0, 0, 0, 200, 208, 206, 212,
                    200, 212, 206, 208, 200, 0, 0, 0, 0, 0, 0, 0, 194, 206,
                    204, 212, 200, 212, 204, 206, 194, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0},//车
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 100, 96, 91, 90, 91, 96,
                    100, 100, 0, 0, 0, 0, 0, 0, 0, 98, 98, 96, 92, 89, 92, 96,
                    98, 98, 0, 0, 0, 0, 0, 0, 0, 97, 97, 96, 91, 92, 91, 96,
                    97, 97, 0, 0, 0, 0, 0, 0, 0, 96, 99, 99, 98, 100, 98, 99,
                    99, 96, 0, 0, 0, 0, 0, 0, 0, 96, 96, 96, 96, 100, 96, 96,
                    96, 96, 0, 0, 0, 0, 0, 0, 0, 95, 96, 99, 96, 100, 96, 99,
                    96, 95, 0, 0, 0, 0, 0, 0, 0, 96, 96, 96, 96, 96, 96, 96,
                    96, 96, 0, 0, 0, 0, 0, 0, 0, 97, 96, 100, 99, 101, 99, 100,
                    96, 97, 0, 0, 0, 0, 0, 0, 0, 96, 97, 98, 98, 98, 98, 98,
                    97, 96, 0, 0, 0, 0, 0, 0, 0, 96, 96, 97, 99, 99, 99, 97,
                    96, 96, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//炮
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 9, 9, 11, 13, 11, 9, 9, 9, 0,
                    0, 0, 0, 0, 0, 0, 19, 24, 34, 42, 44, 42, 34, 24, 19, 0, 0,
                    0, 0, 0, 0, 0, 19, 24, 32, 37, 37, 37, 32, 24, 19, 0, 0, 0,
                    0, 0, 0, 0, 19, 23, 27, 29, 30, 29, 27, 23, 19, 0, 0, 0, 0,
                    0, 0, 0, 14, 18, 20, 27, 29, 27, 20, 18, 14, 0, 0, 0, 0, 0,
                    0, 0, 7, 0, 13, 0, 16, 0, 13, 0, 7, 0, 0, 0, 0, 0, 0, 0, 7,
                    0, 7, 0, 15, 0, 7, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                    1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 15, 11, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0},};//兵
    public static final String[] STARTUP_FEN = {
            "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w - - 0 1",
            "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/R1BAKABNR w - - 0 1",
            "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/R1BAKAB1R w - - 0 1",
            "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/9/1C5C1/9/RN2K2NR w - - 0 1",};
    public static final int[] KING_DELTA = {-16, -1, 1, 16};
    public static final int[] ADVISOR_DELTA = {-17, -15, 15, 17};
    public static final int[][] KNIGHT_DELTA = {{-33, -31}, {-18, 14},
            {-14, 18}, {31, 33}};
    public static final int[][] KNIGHT_CHECK_DELTA = {{-33, -18},
            {-31, -14}, {14, 31}, {18, 33}};
    public static final int[] MVV_VALUE = {50, 10, 10, 30, 40, 30, 20, 0};
    public static int PreGen_zobristKeyPlayer;
    public static int PreGen_zobristLockPlayer;
    public static int[][] PreGen_zobristKeyTable = new int[14][256];
    public static int[][] PreGen_zobristLockTable = new int[14][256];

    public static final int MAX_MOVE_NUM = 256;
    public static final int MAX_GEN_MOVES = 128;
    public static final int MAX_BOOK_SIZE = 16384;
    //保存棋谱的数据
    public static int bookSize = 0;
    public static int[] bookLock = new int[MAX_BOOK_SIZE];
    public static short[] bookMove = new short[MAX_BOOK_SIZE];
    public static short[] bookValue = new short[MAX_BOOK_SIZE];
    //用于判断当前是红方(0)下还是黑方(1)下
    public int sdPlayer;
    public byte[] squares = new byte[256];  //用于记录棋子的最新位置
    //判断局面的标志
    public int zobristKey;
    public int zobristLock;
    public int vlWhite, vlBlack;
    public int moveNum, distance;
    public static final int MATE_VALUE = 10000;
    public static final int BAN_VALUE = MATE_VALUE - 100;
    public static final int WIN_VALUE = MATE_VALUE - 200;
    public static final int NULL_SAFE_MARGIN = 400;
    public static final int NULL_OKAY_MARGIN = 200;
    public static final int DRAW_VALUE = 20;
    public static final int ADVANCED_VALUE = 3;


    public static final int PIECE_KING = 0;
    public static final int PIECE_ADVISOR = 1;
    public static final int PIECE_BISHOP = 2;
    public static final int PIECE_KNIGHT = 3;
    public static final int PIECE_ROOK = 4;
    public static final int PIECE_CANNON = 5;
    public static final int PIECE_PAWN = 6;

    //棋子对于矩阵的上起点
    public static final int RANK_TOP = 3;
    public static final int RANK_BOTTOM = 12;
    //棋子对于矩阵的左起点
    public static final int FILE_LEFT = 3;
    public static final int FILE_RIGHT = 11;
    public static Random random = new Random();
    //用于记录 mvList：每一步所杀死的棋子 pcList：移动的棋子 keyList局面 chkList：将对方军
    public int[] mvList = new int[MAX_MOVE_NUM];
    public int[] pcList = new int[MAX_MOVE_NUM];
    public int[] keyList = new int[MAX_MOVE_NUM];
    public boolean[] chkList = new boolean[MAX_MOVE_NUM];

    public static void readBookData(Context context) {
        Util.RC4 rc4 = new Util.RC4(new byte[]{0});
        PreGen_zobristKeyPlayer = rc4.nextLong();
        rc4.nextLong(); // Skip ZobristLock0
        PreGen_zobristLockPlayer = rc4.nextLong();
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 256; j++) {
                PreGen_zobristKeyTable[i][j] = rc4.nextLong();
                rc4.nextLong(); // Skip ZobristLock0
                PreGen_zobristLockTable[i][j] = rc4.nextLong();
            }
        }
        AssetManager assetManager = context.getAssets();

        InputStream in = null;
        try {
            in = assetManager.open("BOOK.DAT");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ;
        if (in != null) {
            try {
                while (bookSize < MAX_BOOK_SIZE) {
                    bookLock[bookSize] = Util.readInt(in) >>> 1;
                    bookMove[bookSize] = (short) Util.readShort(in);
                    bookValue[bookSize] = (short) Util.readShort(in);
                    bookSize++;
                }
            } catch (Exception e) {
                // Exit "while" when IOException occurs
            }
            try {
                in.close();
            } catch (Exception e) {
                // Ignored
            }
        }
    }

    public void fromFen(String fen) {
        clearBoard();
        int y = RANK_TOP;
        int x = FILE_LEFT;
        int index = 0;
        if (index == fen.length()) {
            setIrrev();
            return;
        }
        char c = fen.charAt(index);
        while (c != ' ') {
            if (c == '/') {
                x = FILE_LEFT;
                y++;
                if (y > RANK_BOTTOM) {
                    break;
                }
            } else if (c >= '1' && c <= '9') {
                for (int k = 0; k < (c - '0'); k++) {
                    if (x >= FILE_RIGHT) {
                        break;
                    }
                    x++;
                }
            } else if (c >= 'A' && c <= 'Z') {
                if (x <= FILE_RIGHT) {
                    int pt = CHAR_TO_PIECE(c);
                    if (pt >= 0) {
                        addPiece(COORD_XY(x, y), pt + 8);
                    }
                    x++;
                }
            } else if (c >= 'a' && c <= 'z') {
                if (x <= FILE_RIGHT) {
                    int pt = CHAR_TO_PIECE((char) (c + 'A' - 'a'));
                    if (pt >= 0) {
                        addPiece(COORD_XY(x, y), pt + 16);
                    }
                    x++;
                }
            }
            index++;
            if (index == fen.length()) {
                setIrrev();
                return;
            }
            c = fen.charAt(index);
        }
        index++;
        if (index == fen.length()) {
            setIrrev();
            return;
        }
        if (sdPlayer == (fen.charAt(index) == 'b' ? 0 : 1)) {
            changeSide();
        }
        setIrrev();
    }

    private int CHAR_TO_PIECE(char c) {
        switch (c) {
            case 'K':
                return PIECE_KING;
            case 'A':
                return PIECE_ADVISOR;
            case 'B':
            case 'E':
                return PIECE_BISHOP;
            case 'H':
            case 'N':
                return PIECE_KNIGHT;
            case 'R':
                return PIECE_ROOK;
            case 'C':
                return PIECE_CANNON;
            case 'P':
                return PIECE_PAWN;
            default:
                return -1;
        }
    }

    public void clearBoard() {
        sdPlayer = 0;
        for (int sq = 0; sq < 256; sq++) {
            squares[sq] = 0;
        }
        zobristKey = zobristLock = 0;
        vlWhite = vlBlack = 0;
    }

    public void addPiece(int sq, int pc) {
        addPiece(sq, pc, false);
    }

    public void delPiece(int sq, int pc) {
        addPiece(sq, pc, true);
    }

    private void addPiece(int sq, int pc, boolean del) {
        int pcAdjust;
        squares[sq] = (byte) (del ? 0 : pc);
        if (pc < 16) {
            pcAdjust = pc - 8;
            vlWhite += del ? -PIECE_VALUE[pcAdjust][sq]
                    : PIECE_VALUE[pcAdjust][sq];
        } else {
            pcAdjust = pc - 16;
            vlBlack += del ? -PIECE_VALUE[pcAdjust][SQUARE_FLIP(sq)]
                    : PIECE_VALUE[pcAdjust][SQUARE_FLIP(sq)];
            pcAdjust += 7;
        }
        zobristKey ^= PreGen_zobristKeyTable[pcAdjust][sq];
        zobristLock ^= PreGen_zobristLockTable[pcAdjust][sq];
    }

    public void movePiece() {
        int sqSrc = SRC(mvList[moveNum]);
        int sqDst = DST(mvList[moveNum]);
        pcList[moveNum] = squares[sqDst];
        if (pcList[moveNum] > 0) {
            delPiece(sqDst, pcList[moveNum]);
        }
        int pc = squares[sqSrc];
        delPiece(sqSrc, pc);
        addPiece(sqDst, pc);
    }

    public void undoMovePiece() {
        int sqSrc = SRC(mvList[moveNum]);
        int sqDst = DST(mvList[moveNum]);
        int pc = squares[sqDst];
        delPiece(sqDst, pc);
        addPiece(sqSrc, pc);
        if (pcList[moveNum] > 0) {
            addPiece(sqDst, pcList[moveNum]);
        }
    }

    public void changeSide() {
        sdPlayer = 1 - sdPlayer;
        zobristKey ^= PreGen_zobristKeyPlayer;
        zobristLock ^= PreGen_zobristLockPlayer;
    }

    public boolean makeMove(int mv) {    //保存移动棋子的步骤到数组当中 移动棋子 吃掉棋子 等等
        keyList[moveNum] = zobristKey;
        mvList[moveNum] = mv;
        movePiece();
        if (checked()) {
            undoMovePiece();
            return false;
        }
        changeSide();
        chkList[moveNum] = checked();
        moveNum++;
        distance++;
        return true;
    }

    public void goBack() {
        if (moveNum < 3) {
            return;
        }
        undoMakeMove();
        undoMakeMove();
    }

    private boolean checked() {
        int pcSelfSide = SIDE_TAG(sdPlayer);
        int pcOppSide = OPP_SIDE_TAG(sdPlayer);
        for (int sqSrc = 0; sqSrc < 256; sqSrc++) {
            if (squares[sqSrc] != pcSelfSide + PIECE_KING) {
                continue;
            }
            //被兵杀死
            if (squares[SQUARE_FORWARD(sqSrc, sdPlayer)] == pcOppSide
                    + PIECE_PAWN) {
                return true;
            }
            for (int delta = -1; delta <= 1; delta += 2) {
                if (squares[sqSrc + delta] == pcOppSide + PIECE_PAWN) {
                    return true;
                }
            }
            //被马杀死
            for (int i = 0; i < 4; i++) {
                if (squares[sqSrc + ADVISOR_DELTA[i]] != 0) {
                    continue;
                }
                for (int j = 0; j < 2; j++) {
                    int pcDst = squares[sqSrc + KNIGHT_CHECK_DELTA[i][j]];
                    if (pcDst == pcOppSide + PIECE_KNIGHT) {
                        return true;
                    }
                }
            }
            for (int i = 0; i < 4; i++) {
                int delta = KING_DELTA[i];
                int sqDst = sqSrc + delta;
                //被车或者飞将将死
                while (IN_BOARD(sqDst)) {
                    int pcDst = squares[sqDst];
                    if (pcDst > 0) {
                        if (pcDst == pcOppSide + PIECE_ROOK
                                || pcDst == pcOppSide + PIECE_KING) {
                            return true;
                        }
                        break;
                    }
                    sqDst += delta;
                }
                sqDst += delta;
                //被炮将死    // TODO: 2017/2/3   好像逻辑有错误
                while (IN_BOARD(sqDst)) {
                    int pcDst = squares[sqDst];
                    if (pcDst > 0) {
                        if (pcDst == pcOppSide + PIECE_CANNON) {
                            return true;
                        }
                        break;
                    }
                    sqDst += delta;
                }
            }
            return false;
        }
        return false;
    }

    private int SQUARE_FORWARD(int sqSrc, int sdPlayer) {
        return sqSrc - 16 + (sdPlayer << 5);
    }

    private int OPP_SIDE_TAG(int sd) {
        return 16 - (sd << 3);
    }

    public void undoMakeMove() {
        moveNum--;
        distance--;
        changeSide();
        undoMovePiece();
    }

    public void setIrrev() {
        mvList[0] = pcList[0] = 0;
        chkList[0] = checked();
        moveNum = 1;
        distance = 0;
    }


    public static boolean IN_BOARD(int sq) {
        return IN_BOARD[sq] != 0;
    }

    public static int SRC(int mv) {
        return mv & 255;
    }

    public static int DST(int mv) {
        return mv >> 8;
    }

    public static int COORD_XY(int x, int y) {
        return x + (y << 4);
    }
//用以切换执红还是之黑在矩阵中位置的变化
    public static int SQUARE_FLIP(int sq) {
        return 254 - sq;
    }

    public static int FILE_X(int sqDst) {
        return sqDst & 15;
    }

    public static int RANK_Y(int sqDst) {
        return sqDst >> 4;
    }

    public static int SIDE_TAG(int sdPlayer) {
        return 8 + (sdPlayer << 3);
    }

    public static int MOVE(int sqSrc, int sqDst) {
        return sqSrc + (sqDst << 8);
    }

    public boolean isMate() {
        int[] mvs = new int[MAX_GEN_MOVES];
        int moves = generateAllMoves(mvs);
        for (int i = 0; i < moves; i++) {
            if (makeMove(mvs[i])) {
                undoMakeMove();
                return false;
            }
        }
        return true;
    }

    public int generateAllMoves(int[] mvs) {
        return generateMoves(mvs, null);
    }

    public static boolean IN_FORT(int sq) {
        return IN_FORT[sq] != 0;
    }

    public int generateMoves(int[] mvs, int[] vls) {
        int moves = 0;
        int pcSelfSide = SIDE_TAG(sdPlayer);
        int pcOppSide = OPP_SIDE_TAG(sdPlayer);
        for (int sqSrc = 0; sqSrc < 256; sqSrc++) {
            int pcSrc = squares[sqSrc];
            if ((pcSrc & pcSelfSide) == 0) {
                continue;
            }
            switch (pcSrc - pcSelfSide) {
                case PIECE_KING:
                    for (int i = 0; i < 4; i++) {
                        int sqDst = sqSrc + KING_DELTA[i];
                        if (!IN_FORT(sqDst)) {
                            continue;
                        }
                        int pcDst = squares[sqDst];
                        if (vls == null) {
                            if ((pcDst & pcSelfSide) == 0) {
                                mvs[moves] = MOVE(sqSrc, sqDst);
                                moves++;
                            }
                        } else if ((pcDst & pcOppSide) != 0) {
                            mvs[moves] = MOVE(sqSrc, sqDst);
                            vls[moves] = MVV_LVA(pcDst, 5);
                            moves++;
                        }
                    }
                    break;
                case PIECE_ADVISOR:
                    for (int i = 0; i < 4; i++) {
                        int sqDst = sqSrc + ADVISOR_DELTA[i];
                        if (!IN_FORT(sqDst)) {
                            continue;
                        }
                        int pcDst = squares[sqDst];
                        if (vls == null) {
                            if ((pcDst & pcSelfSide) == 0) {
                                mvs[moves] = MOVE(sqSrc, sqDst);
                                moves++;
                            }
                        } else if ((pcDst & pcOppSide) != 0) {
                            mvs[moves] = MOVE(sqSrc, sqDst);
                            vls[moves] = MVV_LVA(pcDst, 1);
                            moves++;
                        }
                    }
                    break;
                case PIECE_BISHOP:
                    for (int i = 0; i < 4; i++) {
                        int sqDst = sqSrc + ADVISOR_DELTA[i];
                        if (!(IN_BOARD(sqDst) && HOME_HALF(sqDst, sdPlayer) && squares[sqDst] == 0)) {
                            continue;
                        }
                        sqDst += ADVISOR_DELTA[i];
                        int pcDst = squares[sqDst];
                        if (vls == null) {
                            if ((pcDst & pcSelfSide) == 0) {
                                mvs[moves] = MOVE(sqSrc, sqDst);
                                moves++;
                            }
                        } else if ((pcDst & pcOppSide) != 0) {
                            mvs[moves] = MOVE(sqSrc, sqDst);
                            vls[moves] = MVV_LVA(pcDst, 1);
                            moves++;
                        }
                    }
                    break;
                case PIECE_KNIGHT:
                    for (int i = 0; i < 4; i++) {
                        int sqDst = sqSrc + KING_DELTA[i];
                        if (squares[sqDst] > 0) {
                            continue;
                        }
                        for (int j = 0; j < 2; j++) {
                            sqDst = sqSrc + KNIGHT_DELTA[i][j];
                            if (!IN_BOARD(sqDst)) {
                                continue;
                            }
                            int pcDst = squares[sqDst];
                            if (vls == null) {
                                if ((pcDst & pcSelfSide) == 0) {
                                    mvs[moves] = MOVE(sqSrc, sqDst);
                                    moves++;
                                }
                            } else if ((pcDst & pcOppSide) != 0) {
                                mvs[moves] = MOVE(sqSrc, sqDst);
                                vls[moves] = MVV_LVA(pcDst, 1);
                                moves++;
                            }
                        }
                    }
                    break;
                case PIECE_ROOK:
                    for (int i = 0; i < 4; i++) {
                        int delta = KING_DELTA[i];
                        int sqDst = sqSrc + delta;
                        while (IN_BOARD(sqDst)) {
                            int pcDst = squares[sqDst];
                            if (pcDst == 0) {
                                if (vls == null) {
                                    mvs[moves] = MOVE(sqSrc, sqDst);
                                    moves++;
                                }
                            } else {
                                if ((pcDst & pcOppSide) != 0) {
                                    mvs[moves] = MOVE(sqSrc, sqDst);
                                    if (vls != null) {
                                        vls[moves] = MVV_LVA(pcDst, 4);
                                    }
                                    moves++;
                                }
                                break;
                            }
                            sqDst += delta;
                        }
                    }
                    break;
                case PIECE_CANNON:
                    for (int i = 0; i < 4; i++) {
                        int delta = KING_DELTA[i];
                        int sqDst = sqSrc + delta;
                        while (IN_BOARD(sqDst)) {
                            int pcDst = squares[sqDst];
                            if (pcDst == 0) {
                                if (vls == null) {
                                    mvs[moves] = MOVE(sqSrc, sqDst);
                                    moves++;
                                }
                            } else {
                                break;
                            }
                            sqDst += delta;
                        }
                        sqDst += delta;
                        while (IN_BOARD(sqDst)) {
                            int pcDst = squares[sqDst];
                            if (pcDst > 0) {
                                if ((pcDst & pcOppSide) != 0) {
                                    mvs[moves] = MOVE(sqSrc, sqDst);
                                    if (vls != null) {
                                        vls[moves] = MVV_LVA(pcDst, 4);
                                    }
                                    moves++;
                                }
                                break;
                            }
                            sqDst += delta;
                        }
                    }
                    break;
                case PIECE_PAWN:
                    int sqDst = SQUARE_FORWARD(sqSrc, sdPlayer);
                    if (IN_BOARD(sqDst)) {
                        int pcDst = squares[sqDst];
                        if (vls == null) {
                            if ((pcDst & pcSelfSide) == 0) {
                                mvs[moves] = MOVE(sqSrc, sqDst);
                                moves++;
                            }
                        } else if ((pcDst & pcOppSide) != 0) {
                            mvs[moves] = MOVE(sqSrc, sqDst);
                            vls[moves] = MVV_LVA(pcDst, 2);
                            moves++;
                        }
                    }
                    if (AWAY_HALF(sqSrc, sdPlayer)) {
                        for (int delta = -1; delta <= 1; delta += 2) {
                            sqDst = sqSrc + delta;
                            if (IN_BOARD(sqDst)) {
                                int pcDst = squares[sqDst];
                                if (vls == null) {
                                    if ((pcDst & pcSelfSide) == 0) {
                                        mvs[moves] = MOVE(sqSrc, sqDst);
                                        moves++;
                                    }
                                } else if ((pcDst & pcOppSide) != 0) {
                                    mvs[moves] = MOVE(sqSrc, sqDst);
                                    vls[moves] = MVV_LVA(pcDst, 2);
                                    moves++;
                                }
                            }
                        }
                    }
                    break;
            }
        }
        return moves;
    }

    public static int MVV_LVA(int pc, int lva) {
        return MVV_VALUE[pc & 7] - lva;
    }

    public static boolean AWAY_HALF(int sq, int sd) {
        return (sq & 0x80) == (sd << 7);
    }

    public int repStatus() {
        return repStatus(1);
    }

    public int repStatus(int recur_) {
        int recur = recur_;
        boolean selfSide = false;
        boolean perpCheck = true;
        boolean oppPerpCheck = true;
        int index = moveNum - 1;
        while (mvList[index] > 0 && pcList[index] == 0) {
            if (selfSide) {
                perpCheck = perpCheck && chkList[index];
                if (keyList[index] == zobristKey) {
                    recur--;
                    if (recur == 0) {
                        return 1 + (perpCheck ? 2 : 0) + (oppPerpCheck ? 4 : 0);
                    }
                }
            } else {
                oppPerpCheck = oppPerpCheck && chkList[index];
            }
            selfSide = !selfSide;
            index--;
        }
        return 0;
    }

    public int repValue(int vlRep) {
        int vlReturn = ((vlRep & 2) == 0 ? 0 : banValue())
                + ((vlRep & 4) == 0 ? 0 : -banValue());
        return vlReturn == 0 ? drawValue() : vlReturn;
    }
    public int banValue() {
        return distance - BAN_VALUE;
    }
    public static int BISHOP_PIN(int sqSrc, int sqDst) {//用于找到象挡脚的位置
        return (sqSrc + sqDst) >> 1;
    }

    //判断走法是否合法
    public boolean legalMove(int mv) {
        int sqSrc = SRC(mv);
        int pcSrc = squares[sqSrc];
        int pcSelfSide = SIDE_TAG(sdPlayer);
        if ((pcSrc & pcSelfSide) == 0) {
            return false;
        }

        int sqDst = DST(mv);
        int pcDst = squares[sqDst];
        if ((pcDst & pcSelfSide) != 0) {
            return false;
        }

        switch (pcSrc - pcSelfSide) {
            case PIECE_KING:  //帅
                return IN_FORT(sqDst) && KING_SPAN(sqSrc, sqDst);
            case PIECE_ADVISOR:   //士
                return IN_FORT(sqDst) && ADVISOR_SPAN(sqSrc, sqDst);
            case PIECE_BISHOP:   //象
                return SAME_HALF(sqSrc, sqDst) && BISHOP_SPAN(sqSrc, sqDst)
                        && squares[BISHOP_PIN(sqSrc, sqDst)] == 0;
            case PIECE_KNIGHT:  //马   马的走法的准确性：横日或者竖的日，不能挡脚
                int sqPin = KNIGHT_PIN(sqSrc, sqDst);
                return sqPin != sqSrc && squares[sqPin] == 0;
            case PIECE_ROOK:    //车
            case PIECE_CANNON:   //炮
                int delta;
                if (SAME_RANK(sqSrc, sqDst)) {
                    delta = (sqDst < sqSrc ? -1 : 1);
                } else if (SAME_FILE(sqSrc, sqDst)) {
                    delta = (sqDst < sqSrc ? -16 : 16);
                } else {
                    return false;
                }
                sqPin = sqSrc + delta;
                while (sqPin != sqDst && squares[sqPin] == 0) {
                    sqPin += delta;
                }
                if (sqPin == sqDst) {
                    return pcDst == 0 || pcSrc - pcSelfSide == PIECE_ROOK;
                }
                if (pcDst == 0 || pcSrc - pcSelfSide == PIECE_ROOK) {
                    return false;
                }
                sqPin += delta;
                while (sqPin != sqDst && squares[sqPin] == 0) {
                    sqPin += delta;
                }
                return sqPin == sqDst;
            case PIECE_PAWN:    //兵
                if (AWAY_HALF(sqDst, sdPlayer)
                        && (sqDst == sqSrc - 1 || sqDst == sqSrc + 1)) {
                    return true;
                }
                return sqDst == SQUARE_FORWARD(sqSrc, sdPlayer);
            default:
                return false;
        }
    }

    public static boolean SAME_FILE(int sqSrc, int sqDst) {
        return ((sqSrc ^ sqDst) & 0x0f) == 0;
    }

    public static boolean ADVISOR_SPAN(int sqSrc, int sqDst) {
        return LEGAL_SPAN[sqDst - sqSrc + 256] == 2;
    }

    public static boolean SAME_RANK(int sqSrc, int sqDst) {
        return ((sqSrc ^ sqDst) & 0xf0) == 0;
    }

    public static boolean BISHOP_SPAN(int sqSrc, int sqDst) {
        return LEGAL_SPAN[sqDst - sqSrc + 256] == 3;
    }

    public static int KNIGHT_PIN(int sqSrc, int sqDst) {  //用于判断马的走法的准确性
        return sqSrc + KNIGHT_PIN[sqDst - sqSrc + 256];
    }

    private boolean KING_SPAN(int sqSrc, int sqDst) {
        return LEGAL_SPAN[sqDst - sqSrc + 256] == 1;
    }

    public static boolean HOME_HALF(int sq, int sd) {
        return (sq & 0x80) != (sd << 7);
    }

    public static boolean SAME_HALF(int sqSrc, int sqDst) {
        return ((sqSrc ^ sqDst) & 0x80) == 0;
    }

    //是否有杀死棋子
    public boolean captured() {
        return pcList[moveNum - 1] > 0;
    }

    public boolean inCheck() {
        return chkList[moveNum - 1];
    }

    //根据定式中的数据获取下一步的走法
    public int bookMove() {
        if (bookSize == 0) {
            return 0;
        }
        boolean mirror = false;
        int lock = zobristLock >>> 1; // Convert into Unsigned
        int index = Util.binarySearch(lock, bookLock, 0, bookSize);
        if (index < 0) {
            mirror = true;
            lock = mirror().zobristLock >>> 1; // Convert into Unsigned
            index = Util.binarySearch(lock, bookLock, 0, bookSize);
        }
        if (index < 0) {
            return 0;
        }
        index--;
        while (index >= 0 && bookLock[index] == lock) {
            index--;
        }
        int[] mvs = new int[MAX_GEN_MOVES];
        int[] vls = new int[MAX_GEN_MOVES];
        int value = 0;
        int moves = 0;
        index++;
        while (index < bookSize && bookLock[index] == lock) {
            int mv = 0xffff & bookMove[index];
            mv = (mirror ? MIRROR_MOVE(mv) : mv);
            if (legalMove(mv)) {
                mvs[moves] = mv;
                vls[moves] = bookValue[index];
                value += vls[moves];
                moves++;
                if (moves == MAX_GEN_MOVES) {
                    break;
                }
            }
            index++;
        }
        if (value == 0) {
            return 0;
        }
        value = Math.abs(random.nextInt()) % value;
        for (index = 0; index < moves; index++) {
            value -= vls[index];
            if (value < 0) {
                break;
            }
        }
        return mvs[index];
    }

    private int MIRROR_MOVE(int mv) {
        return MOVE(MIRROR_SQUARE(SRC(mv)), MIRROR_SQUARE(DST(mv)));
    }

    private Position mirror() {
        Position pos = new Position();
        pos.clearBoard();
        for (int sq = 0; sq < 256; sq++) {
            int pc = squares[sq];
            if (pc > 0) {
                pos.addPiece(MIRROR_SQUARE(sq), pc);
            }
        }
        if (sdPlayer == 1) {
            pos.changeSide();
        }
        return pos;
    }

    private int MIRROR_SQUARE(int sq) {
        return COORD_XY(FILE_FLIP(FILE_X(sq)), RANK_Y(sq));
    }

    public static int FILE_FLIP(int x) {
        return 14 - x;
    }

    public int historyIndex(int mv) {
        return ((squares[SRC(mv)] - 8) << 8) + DST(mv);
    }


    public int mateValue() {
        return distance - MATE_VALUE;
    }


    public int evaluate() {
        int vl = (sdPlayer == 0 ? vlWhite - vlBlack : vlBlack - vlWhite)
                + ADVANCED_VALUE;
        return vl == drawValue() ? vl - 1 : vl;
    }

    public int drawValue() {
        return (distance & 1) == 0 ? -DRAW_VALUE : DRAW_VALUE;
    }

    public boolean nullOkay() {
        return (sdPlayer == 0 ? vlWhite : vlBlack) > NULL_OKAY_MARGIN;
    }

    public boolean nullSafe() {
        return (sdPlayer == 0 ? vlWhite : vlBlack) > NULL_SAFE_MARGIN;
    }


    public void nullMove() {
        keyList[moveNum] = zobristKey;
        changeSide();
        mvList[moveNum] = pcList[moveNum] = 0;
        chkList[moveNum] = false;
        moveNum++;
        distance++;
    }

    public void undoNullMove() {
        moveNum--;
        distance--;
        changeSide();
    }

    public int getLastMove() {
        return mvList[moveNum-1];
    }
}



































