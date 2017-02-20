package com.minhuizhu.mynewchess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.minhuizhu.mynewchess.util.Util;

/**
 * Created by zhuminh on 2017/2/2.
 */
public class ChessView extends View {
    private static final int PHASE_LOADING = 0;
    private static final int PHASE_WAITING = 1;
    private static final int PHASE_THINKING = 2;
    private static final int PHASE_EXITTING = 3;
    //COMPUTER_BLACK COMPUTER_RED 用于判断电脑执红还是执黑
    private static final int COMPUTER_BLACK = 0;
    private static final int COMPUTER_RED = 1;
    private static final int COMPUTER_NONE = 2;

    private static final int RESP_HUMAN_SINGLE = -2;
    private static final int RESP_HUMAN_BOTH = -1;
    private static final int RESP_CLICK = 0;
    private static final int RESP_ILLEGAL = 1;
    private static final int RESP_MOVE = 2;
    private static final int RESP_MOVE2 = 3;
    private static final int RESP_CAPTURE = 4;
    private static final int RESP_CAPTURE2 = 5;
    private static final int RESP_CHECK = 6;
    private static final int RESP_CHECK2 = 7;
    private static final int RESP_WIN = 8;
    private static final int RESP_DRAW = 9;
    private static final int RESP_LOSS = 10;
    private static final String TAGS = "ChessView";

    private Bitmap imgBackground, imgXQWLight/*,imgThinking*/;
    private static final String[] IMAGE_NAME = {null, null, null, null, null,
            null, null, null, "rk", "ra", "rb", "rn", "rr", "rc", "rp", null,
            "bk", "ba", "bb", "bn", "br", "bc", "bp", null,};
    private int widthBackground, heightBackground;

    static final int RS_DATA_LEN = 512;

    byte[] rsData = new byte[RS_DATA_LEN];

    byte[] retractData = new byte[RS_DATA_LEN];

    private Position pos = new Position();
    private Search search = new Search(pos, 12);
    private String message;
    private int cursorX, cursorY;
    private int sqSelected, mvLast;
    // Assume FullScreenMode = false
    private int normalWidth = getWidth();
    private int normalHeight = getHeight();

    volatile int phase = PHASE_LOADING;

    private boolean init = false;
    private Bitmap imgBoard, imgSelected, imgSelected2, imgCursor, imgCursor2;
    private Bitmap[] imgPieces = new Bitmap[24];
    private int squareSize, width, height, left, right, top, bottom;
    private Context context;
    private Paint paint = new Paint();
    public int moveMode, level;
    private final Handler handler;
    private byte[] currentSquares;

    public ChessView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        imgBackground = BitmapFactory.decodeResource(getResources(),
                R.drawable.background);
        imgXQWLight = BitmapFactory.decodeResource(getResources(),
                R.drawable.xqwlight);
//		imgThinking = BitmapFactory.decodeResource(getResources(),
//				R.drawable.thinking);
        widthBackground = imgBackground.getWidth();
        heightBackground = imgBackground.getHeight();
        handler = new Handler();
        this.context = context;
    }

    public byte[] getSquares() {
        return pos.squares;
    }

    public void load(byte[] rsData, int handicap, int moveMode, int level) {
        load(rsData, handicap, moveMode, level, false);
    }

    public void load(byte[] rsData, int handicap, int moveMode, int level, boolean isInvalid) {
        this.moveMode = moveMode;
        this.level = level;
        this.rsData = rsData;
        initSelect();
        sqSelected = mvLast = 0;

        if (rsData[0] == 0) {
            pos.fromFen(Position.STARTUP_FEN[handicap]);
        } else {
            // Restore Record-Score Data
            pos.clearBoard();
            for (int sq = 0; sq < 256; sq++) {
                int pc = rsData[sq + 256];
                if (pc > 0) {
                    pos.addPiece(sq, pc);
                }
            }
            if (rsData[0] == 2) {
                pos.changeSide();
            }
            pos.setIrrev();
        }
        // Backup Retract Status
        System.arraycopy(rsData, 0, retractData, 0, RS_DATA_LEN);
        // Call "responseMove()" if Computer Moves First
        phase = PHASE_LOADING;
        if (isInvalid) {
            invalidate();
        }
        if (pos.sdPlayer == 0 ? moveMode == COMPUTER_RED
                : moveMode == COMPUTER_BLACK) {
            new Thread() {
                public void run() {
                    while (phase == PHASE_LOADING) {
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            // Ignored
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            responseMove();
                        }
                    });

                }
            }.start();
        }
    }

    private void initSelect() {
        cursorX = cursorY = -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (phase == PHASE_LOADING) {
            width = getWidth();
            height = getHeight();
            for (int i = 0; i < 10; i++) {      //确保棋盘大小是准确的
                if (width != normalWidth || height != normalHeight) {
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                width = getWidth();
                height = getHeight();
            }
            initBitmap();
        }
        drawChess(canvas);
        super.onDraw(canvas);
    }

    protected void drawChess(Canvas canvas) {
        Log.e(TAGS, "drawChess" + Thread.currentThread().getName() + "begin time " + SystemClock.currentThreadTimeMillis());

        drawBackground(canvas);
        drawBoard(canvas);
        drawEveryChess(canvas);
        drawLastMoveChess(canvas);
        Log.e(TAGS, "drawChess" + Thread.currentThread().getName() + "end time " + SystemClock.currentThreadTimeMillis());
    }

    private void drawLastMoveChess(Canvas canvas) {
        if (mvLast > 0) {
            int sqSrc = Position.SRC(mvLast);
            int sqDst = Position.DST(mvLast);
            drawSquare(canvas, (currentSquares[sqSrc] & 8) == 0 ? imgSelected
                    : imgSelected2, sqSrc);
            drawSquare(canvas, (currentSquares[sqDst] & 8) == 0 ? imgSelected
                    : imgSelected2, sqDst);
        } else if (sqSelected > 0) {
            drawSquare(canvas, (currentSquares[sqSelected] & 8) == 0 ? imgSelected
                    : imgSelected2, sqSelected);
        }
    }

    private void drawEveryChess(Canvas canvas) {
        for (int sq = 0; sq < 256; sq++) {  //绘制棋子
            if (Position.IN_BOARD(sq)) {
                int pc = currentSquares[sq];
                if (pc > 0) {
                    drawSquare(canvas, imgPieces[pc], sq);
                }
            }
        }
    }

    private void drawBoard(Canvas canvas) {
        Bitmap mBitmap = Bitmap.createScaledBitmap(imgBoard, right - left + 32,  //绘制棋盘
                bottom - top + 32, false);
        canvas.drawBitmap(mBitmap, left, 0, paint);
    }

    private void drawBackground(Canvas canvas) {
        for (int x = 0; x < width; x += widthBackground) {    //绘制象棋的背景
            for (int y = 0; y < height; y += heightBackground) {
                canvas.drawBitmap(imgBackground, x, y, paint);
            }
        }

    }

    private void initBitmap() {
        if (!init) {
            init = true;
            // "width" and "height" are Full-Screen values
            squareSize = Math.min(width / 9, height / 10);

            int boardWidth = squareSize * 9;
            int boardHeight = squareSize * 10;
            try {
                imgBoard = BitmapFactory.decodeResource(getResources(),
                        R.drawable.board);
                imgSelected = BitmapFactory.decodeResource(getResources(),
                        R.drawable.selected);
                imgSelected2 = BitmapFactory.decodeResource(getResources(),
                        R.drawable.selected2);
                imgCursor = BitmapFactory.decodeResource(getResources(),
                        R.drawable.cursor);
                imgCursor2 = BitmapFactory.decodeResource(getResources(),
                        R.drawable.cursor2);
                for (int pc = 0; pc < 24; pc++) {
                    if (IMAGE_NAME[pc] == null) {
                        imgPieces[pc] = null;
                    } else {
                        imgPieces[pc] = BitmapFactory.decodeResource(
                                getResources(),
                                getResources().getIdentifier(
                                        "" + IMAGE_NAME[pc], "drawable",
                                        context.getPackageName()));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            left = (width - boardWidth) / 2;
            top = (height - boardHeight) / 2;
            right = left + boardWidth - 32;
            bottom = top + boardHeight - 32;
        }
        phase = PHASE_WAITING;

    }

    private void drawSquare(Canvas canvas, Bitmap bitmap, int sq) {
        int sqFlipped = (moveMode == COMPUTER_RED ? Position.SQUARE_FLIP(sq)
                : sq);
        int sqX = left + (Position.FILE_X(sqFlipped) - Position.FILE_LEFT)
                * squareSize;
        int sqY = top + (Position.RANK_Y(sqFlipped) - Position.RANK_TOP)
                * squareSize;
        if (bitmap != null) {

            Bitmap mBitmap = Bitmap.createScaledBitmap(bitmap, squareSize, squareSize, true);
            canvas.drawBitmap(mBitmap, sqX, sqY, paint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            pointerPressed(event.getX(), event.getY());
        }
        return super.onTouchEvent(event);
    }

    protected void pointerPressed(float x, float y) {

        if (phase == PHASE_THINKING) {
            return;
        }
        cursorX = Util.MIN_MAX(0, ((int) x - left) / squareSize, 8);
        cursorY = Util.MIN_MAX(0, ((int) y - top) / squareSize, 9);
        clickSquare();

    }

    private void clickSquare() {
        int sq = Position.COORD_XY(cursorX + Position.FILE_LEFT, cursorY
                + Position.RANK_TOP);
        if (moveMode == COMPUTER_RED) {
            sq = Position.SQUARE_FLIP(sq);
        }
        int pc = pos.squares[sq];
        if ((pc & Position.SIDE_TAG(pos.sdPlayer)) != 0) {   //说明是同一边的棋子

            mvLast = 0;
            sqSelected = sq;
            invalidate();
        } else {
            if (sqSelected <= 0) {
                return;
            }
            boolean isValidMove = addMove(Position.MOVE(sqSelected, sq));

            if (isValidMove) {
                Log.e(TAGS, "startValid" + Thread.currentThread().getName() + "invalid begin" + SystemClock.currentThreadTimeMillis());
                invalidate();
                responseMove();
            }
        }
    }

    @Override
    public void invalidate() {
        if (currentSquares == null) {
            currentSquares = new byte[256];
        }
        //保存一份棋子位置的拷贝，防止线程冲突
        System.arraycopy(pos.squares, 0, currentSquares, 0, 256);
        super.invalidate();
    }

    /**
     * Player Move Result
     */
    private boolean getResult() {
        return getResult(moveMode == COMPUTER_NONE ? RESP_HUMAN_BOTH
                : RESP_HUMAN_SINGLE);
    }

    /**
     * Computer Move Result 判断是否已完棋
     */
    private boolean getResult(int response) {
        if (pos.isMate()) {
            message = (response < 0 ? "祝贺你取得胜利！" : "请再接再厉！");
            return true;
        }
        int vlRep = pos.repStatus(3);
        if (vlRep > 0) {
            vlRep = (response < 0 ? pos.repValue(vlRep) : -pos.repValue(vlRep));
            message = (vlRep > Position.WIN_VALUE ? "长打作负，请不要气馁！"
                    : vlRep < -Position.WIN_VALUE ? "电脑长打作负，祝贺你取得胜利！"
                    : "双方不变作和，辛苦了！");
            return true;
        }
        if (pos.moveNum > 100) {
            message = "超过自然限着作和，辛苦了！";
            return true;
        }
        if (response != RESP_HUMAN_SINGLE) {
            if (response >= 0) {
            }
            // Backup Retract Status
            System.arraycopy(rsData, 0, retractData, 0, RS_DATA_LEN);
            // Backup Record-Score Data
            rsData[0] = (byte) (pos.sdPlayer + 1);
            System.arraycopy(pos.squares, 0, rsData, 256, 256);
        }
        return false;
    }

    private boolean addMove(int mv) {
        if (pos.legalMove(mv)) {           //判断所下的子是否合法
            if (pos.makeMove(mv)) {
                //所选择的棋子归置为0
                sqSelected = 0;
                mvLast = mv;
                return true;
            }
        }
        return false;
    }

    public  void notifyStep() {
        phase = PHASE_THINKING;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAGS, Thread.currentThread().getName() + "begin thread");
                mvLast = search.searchMain(1000 << (level << 1));
                pos.makeMove(mvLast);
                final int response = pos.inCheck() ? RESP_CHECK2
                        : pos.captured() ? RESP_CAPTURE2 : RESP_MOVE2;
                phase = PHASE_WAITING;
                Log.e(TAGS, Thread.currentThread().getName() + "end thread");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                        if (getResult(response)) {
                            rsData[0] = 0;
                            phase = PHASE_EXITTING;
                        }else {
                            responseMove();
                        }
                    }
                });
            }
        }).start();
    }

    void responseMove() {
        if (getResult()) {
            rsData[0] = 0;
            phase = PHASE_EXITTING;
        }
        if (moveMode == COMPUTER_NONE) {
            return;
        }
        phase = PHASE_THINKING;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAGS, Thread.currentThread().getName() + "begin thread");
                mvLast = search.searchMain(1000 << (level << 1));
                pos.makeMove(mvLast);
                final int response = pos.inCheck() ? RESP_CHECK2
                        : pos.captured() ? RESP_CAPTURE2 : RESP_MOVE2;
                phase = PHASE_WAITING;
                Log.e(TAGS, Thread.currentThread().getName() + "end thread");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                        if (getResult(response)) {
                            rsData[0] = 0;
                            phase = PHASE_EXITTING;
                        }
                    }
                });
            }
        }).start();


    }

    void back() {
        if (phase == PHASE_THINKING) {
            return;
        } else {
            pos.goBack();
            mvLast = pos.getLastMove();
            invalidate();
        }
    }

    void retract(byte handicap) {
        // Restore Retract Status
        System.arraycopy(retractData, 0, rsData, 0, RS_DATA_LEN);
        load(rsData, handicap, moveMode, level);
        invalidate();

    }

    void about() {
        phase = PHASE_LOADING;
    }
}






























































































