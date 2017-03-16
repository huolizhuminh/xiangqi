package com.minhuizhu.mynewchess.presenter;

import android.os.Message;
import android.util.Log;

import com.minhuizhu.mynewchess.ui.game.PlayWithComputerActivity;
import com.minhuizhu.common.util.WidgetUtil;

/**
 * Created by zhuminh on 2017/2/2.
 */
class HashItem {
    byte depth, flag;
    short vl;
    int mv, zobristLock;
}

public class Search {
    private static final int HASH_ALPHA = 1;
    private static final int HASH_BETA = 2;
    private static final int HASH_PV = 3;
    private static final int LIMIT_DEPTH = 64;
    private static final int NULL_DEPTH = 2;
    private static final int RANDOM_MASK = 7;
    private static final int MAX_GEN_MOVES = Position.MAX_GEN_MOVES;
    private static final int MATE_VALUE = Position.MATE_VALUE;
    private static final int BAN_VALUE = Position.BAN_VALUE;
    private static final int WIN_VALUE = Position.WIN_VALUE;
    private String TAGS = "Search";
    private int hashMask, mvResult, allNodes, allMillis;
    private HashItem[] hashTable;
    Position searchPosition;
    Position initPosition;
    int[] historyTable = new int[4096];
    int[][] mvKiller = new int[LIMIT_DEPTH][2];
    private boolean needSearch;

    public Search(Position pos, int hashLevel) {
        this.initPosition = pos;
        hashMask = (1 << hashLevel) - 1;
        hashTable = new HashItem[hashMask + 1];
        for (int i = 0; i <= hashMask; i++) {
            hashTable[i] = new HashItem();
        }
    }

    public int searchMain(int level) {
        PlayWithComputerActivity.handler.sendEmptyMessage(1);
        final int millis;
        if (level < 0) {
            level = 0;
        } else if (level > 2) {
            level = 2;
        }
        if (level == 0) {
            millis = 1000;
        } else if (level == 1) {
            millis = 2500;
        } else {
            millis = 6000;
        }
        searchPosition = (Position) initPosition.deepCopy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                needSearch = true;
                searchMain(LIMIT_DEPTH, millis);
            }
        }).start();
        long t = System.currentTimeMillis();
        try {
            while (System.currentTimeMillis() - t < millis) {
                Log.i(TAGS, "System.currentTimeMillis() - t =" + (System.currentTimeMillis() - t) + "millis=" + millis);
                Thread.sleep(200);
            }
        } catch (Exception e) {

        }
        needSearch = false;
        return mvResult;

    }

    public int searchMain(int depth, int millis) {
        mvResult = searchPosition.bookMove();
        if (mvResult > 0) {
            searchPosition.makeMove(mvResult);
            if (searchPosition.repStatus(3) == 0) {
                searchPosition.undoMakeMove();
                return mvResult;
            }
            searchPosition.undoMakeMove();
        }
        for (int i = 0; i <= hashMask; i++) {
            HashItem hash = hashTable[i];
            hash.depth = hash.flag = 0;
            hash.vl = 0;
            hash.mv = hash.zobristLock = 0;
        }
        for (int i = 0; i < LIMIT_DEPTH; i++) {
            mvKiller[i][0] = mvKiller[i][1] = 0;
        }
        for (int i = 0; i < 4096; i++) {
            historyTable[i] = 0;
        }
        mvResult = 0;
        allNodes = 0;
        searchPosition.distance = 0;
        long t = System.currentTimeMillis();
        for (int i = 1; i <= depth; i++) {
            int vl = searchRoot(i);
            allMillis = (int) (System.currentTimeMillis() - t);
            Log.i(TAGS, "i=" + i + "allMillis==" + allMillis + "millis=" + millis);
            if (allMillis > millis) {
                Log.i(TAGS, "i=" + i + "allMillis==" + allMillis + "millis=" + millis);
                break;
            }
            if (vl > WIN_VALUE || vl < -WIN_VALUE) {
                break;
            }
            if (searchUnique(1 - WIN_VALUE, i)) {
                break;
            }
            sendMessage(i);

        }
        return mvResult;
    }

    private void sendMessage(int i) {
        Message message = Message.obtain();
        message.what = 0;
        String mssageObj = searchPosition.getMoveMessage(mvResult);
        message.obj = "层级" + i + "结果:" + mssageObj + "\n";
        PlayWithComputerActivity.handler.sendMessage(message);
    }

    public boolean searchUnique(int vlBeta, int depth) {
        SortItem sort = new SortItem(mvResult);
        sort.next();
        int mv;
        while ((mv = sort.next()) > 0) {
            if (!searchPosition.makeMove(mv)) {
                continue;
            }
            int vl = -searchFull(-vlBeta, 1 - vlBeta, searchPosition.inCheck() ? depth : depth - 1);
            searchPosition.undoMakeMove();
            if (vl >= vlBeta) {
                return false;
            }
        }
        return true;
    }

    private int searchRoot(int depth) {
        int vlBest = -MATE_VALUE;
        SortItem sort = new SortItem(mvResult);
        int mv;
        while ((mv = sort.next()) > 0) {
            if (!needSearch) {
                return vlBest;
            }
            if (!searchPosition.makeMove(mv)) {
                continue;
            }
            int newDepth = searchPosition.inCheck() ? depth : depth - 1;
            int vl;
            if (vlBest == -MATE_VALUE) {
                vl = -searchNoNull(-MATE_VALUE, MATE_VALUE, newDepth);
            } else {
                vl = -searchFull(-vlBest - 1, -vlBest, newDepth);
                if (vl > vlBest) {
                    vl = -searchNoNull(-MATE_VALUE, -vlBest, newDepth);
                }
            }
            searchPosition.undoMakeMove();
            if (vl > vlBest) {
                vlBest = vl;
                mvResult = mv;
                if (vlBest > -WIN_VALUE && vlBest < WIN_VALUE) {
                    vlBest += (Position.random.nextInt() & RANDOM_MASK) -
                            (Position.random.nextInt() & RANDOM_MASK);
                    vlBest = (vlBest == searchPosition.drawValue() ? vlBest - 1 : vlBest);
                }
            }
        }
        setBestMove(mvResult, depth);
        return vlBest;
    }

    private int searchNoNull(int vlAlpha, int vlBeta, int depth) {
        return searchFull(vlAlpha, vlBeta, depth, true);
    }

    private int searchFull(int vlAlpha, int vlBeta, int depth) {
        return searchFull(vlAlpha, vlBeta, depth, false);
    }

    private int searchFull(int vlAlpha_, int vlBeta, int depth, boolean noNull) {
        int vlAlpha = vlAlpha_;
        int vl;
        //直接搜索
        if (depth <= 0) {
            return searchQuiesc(vlAlpha, vlBeta);
        }
        allNodes++;
        vl = searchPosition.mateValue();
        if (vl >= vlBeta) {
            return vl;
        }
        int vlRep = searchPosition.repStatus();
        if (vlRep > 0) {
            return searchPosition.repValue(vlRep);
        }
        int[] mvHash = new int[1];
        //查询搜索分支历史
        vl = probeHash(vlAlpha, vlBeta, depth, mvHash);
        if (vl > -MATE_VALUE) {
            return vl;
        }
        if (searchPosition.distance == LIMIT_DEPTH) {
            return searchPosition.evaluate();
        }
        if (!noNull && !searchPosition.inCheck() && searchPosition.nullOkay()) {
            searchPosition.nullMove();
            vl = -searchNoNull(-vlBeta, 1 - vlBeta, depth - NULL_DEPTH - 1);
            searchPosition.undoNullMove();
            if (vl >= vlBeta && (searchPosition.nullSafe() || searchNoNull(vlAlpha, vlBeta, depth - NULL_DEPTH) >= vlBeta)) {
                return vl;
            }
        }
        int hashFlag = HASH_ALPHA;
        int vlBest = -MATE_VALUE;
        int mvBest = 0;
        SortItem sort = new SortItem(mvHash[0]);
        int mv;
        while ((mv = sort.next()) > 0) {
            if (!needSearch) {
                return vlBest;
            }
            if (!searchPosition.makeMove(mv)) {
                continue;
            }
            int newDepth = searchPosition.inCheck() || sort.singleReply ? depth : depth - 1;
            if (vlBest == -MATE_VALUE) {
                vl = -searchFull(-vlBeta, -vlAlpha, newDepth);
            } else {
                vl = -searchFull(-vlAlpha - 1, -vlAlpha, newDepth);
                if (vl > vlAlpha && vl < vlBeta) {
                    vl = -searchFull(-vlBeta, -vlAlpha, newDepth);
                }
            }
            searchPosition.undoMakeMove();
            if (vl > vlBest) {
                vlBest = vl;
                if (vl >= vlBeta) {
                    hashFlag = HASH_BETA;
                    mvBest = mv;
                    break;
                }
                if (vl > vlAlpha) {
                    Log.i(TAGS, "searchPosition.sdPlayer = " + searchPosition.sdPlayer);
                    vlAlpha = vl;
                    hashFlag = HASH_PV;
                    mvBest = mv;
                }
            }
        }
        if (vlBest == -MATE_VALUE) {
            return searchPosition.mateValue();
        }
        //保存历史
        recordHash(hashFlag, vlBest, depth, mvBest);
        if (mvBest > 0) {
            setBestMove(mvBest, depth);
        }
        return vlBest;
    }

    private int probeHash(int vlAlpha, int vlBeta, int depth, int[] mv) {
        HashItem hash = getHashItem();

        if (hash.zobristLock != searchPosition.zobristLock) {
            mv[0] = 0;
            return -MATE_VALUE;
        }
        mv[0] = hash.mv;
        boolean mate = false;
        if (hash.vl > WIN_VALUE) {
            if (hash.vl <= BAN_VALUE) {
                return -MATE_VALUE;
            }
            hash.vl -= searchPosition.distance;
            mate = true;
        } else if (hash.vl < -WIN_VALUE) {
            if (hash.vl >= -BAN_VALUE) {
                return -MATE_VALUE;
            }
            hash.vl += searchPosition.distance;
            mate = true;
        } else if (hash.vl == searchPosition.drawValue()) {
            return -MATE_VALUE;
        }
        if (hash.depth >= depth || mate) {
            if (hash.flag == HASH_BETA) {
                return (hash.vl >= vlBeta ? hash.vl : -MATE_VALUE);
            } else if (hash.flag == HASH_ALPHA) {
                return (hash.vl <= vlAlpha ? hash.vl : -MATE_VALUE);
            }
            return hash.vl;
        }
        return -MATE_VALUE;
    }

    //通过 zobristKey 获取记录中的局面
    private HashItem getHashItem() {
        return hashTable[searchPosition.zobristKey & hashMask];
    }

    private void recordHash(int flag, int vl, int depth, int mv) {
        HashItem hash = getHashItem();
        if (hash.depth > depth) {
            return;
        }
        hash.flag = (byte) flag;
        hash.depth = (byte) depth;
        if (vl > WIN_VALUE) {
            if (mv == 0 && vl <= BAN_VALUE) {
                return;
            }
            hash.vl = (short) (vl + searchPosition.distance);
        } else if (vl < -WIN_VALUE) {
            if (mv == 0 && vl >= -BAN_VALUE) {
                return;
            }
            hash.vl = (short) (vl - searchPosition.distance);
        } else if (vl == searchPosition.drawValue() && mv == 0) {
            return;
        } else {
            hash.vl = (short) vl;
        }
        hash.mv = mv;
        hash.zobristLock = searchPosition.zobristLock;
    }

    private int searchQuiesc(int vlAlpha_, int vlBeta) {
        int vlAlpha = vlAlpha_;
        allNodes++;
        //判断是否已被杀
        int vl = searchPosition.mateValue();
        if (vl >= vlBeta) {
            return vl;
        }
        //判断是否长作
        int vlRep = searchPosition.repStatus();
        if (vlRep > 0) {
            return searchPosition.repValue(vlRep);
        }
        //到达极限直接返回值
        if (searchPosition.distance == LIMIT_DEPTH) {
            return searchPosition.evaluate();
        }
        int vlBest = -MATE_VALUE;
        int genMoves;
        int[] mvs = new int[MAX_GEN_MOVES];
        //
        if (searchPosition.inCheck()) {
            genMoves = searchPosition.generateAllMoves(mvs);
            int[] vls = new int[MAX_GEN_MOVES];
            for (int i = 0; i < genMoves; i++) {
                vls[i] = historyTable[searchPosition.historyIndex(mvs[i])];
            }
            WidgetUtil.shellSort(mvs, vls, 0, genMoves);
        } else {
            vl = searchPosition.evaluate();
            if (vl > vlBest) {
                if (vl >= vlBeta) {
                    return vl;
                }
                vlBest = vl;
                vlAlpha = Math.max(vl, vlAlpha);
            }
            int[] vls = new int[MAX_GEN_MOVES];
            genMoves = searchPosition.generateMoves(mvs, vls);
            WidgetUtil.shellSort(mvs, vls, 0, genMoves);
            for (int i = 0; i < genMoves; i++) {
                if (vls[i] < 10 || (vls[i] < 20 && Position.HOME_HALF(Position.DST(mvs[i]), searchPosition.sdPlayer))) {
                    genMoves = i;
                    break;
                }
            }
        }
        for (int i = 0; i < genMoves; i++) {
            if (!searchPosition.makeMove(mvs[i])) {
                continue;
            }
            vl = -searchQuiesc(-vlBeta, -vlAlpha);
            searchPosition.undoMakeMove();
            if (vl > vlBest) {
                if (vl >= vlBeta) {
                    return vl;
                }
                vlBest = vl;
                vlAlpha = Math.max(vl, vlAlpha);
            }
        }
        return vlBest == -MATE_VALUE ? searchPosition.mateValue() : vlBest;
    }

    private class SortItem {
        private static final int PHASE_HASH = 0;
        private static final int PHASE_KILLER_1 = 1;
        private static final int PHASE_KILLER_2 = 2;
        private static final int PHASE_GEN_MOVES = 3;
        private static final int PHASE_REST = 4;

        private int index, moves, phase;
        private int mvHash, mvKiller1, mvKiller2;
        private int[] mvs, vls;

        boolean singleReply = false;

        SortItem(int mvHash) {
            if (!searchPosition.inCheck()) {
                phase = PHASE_HASH;
                this.mvHash = mvHash;
                mvKiller1 = mvKiller[searchPosition.distance][0];
                mvKiller2 = mvKiller[searchPosition.distance][1];
                return;
            }
            phase = PHASE_REST;
            this.mvHash = mvKiller1 = mvKiller2 = 0;
            mvs = new int[MAX_GEN_MOVES];
            vls = new int[MAX_GEN_MOVES];
            moves = 0;
            int[] mvsAll = new int[MAX_GEN_MOVES];
            int numAll = searchPosition.generateAllMoves(mvsAll);
            for (int i = 0; i < numAll; i++) {
                int mv = mvsAll[i];
                if (!searchPosition.makeMove(mv)) {
                    continue;
                }
                searchPosition.undoMakeMove();
                mvs[moves] = mv;
                vls[moves] = mv == mvHash ? Integer.MAX_VALUE : historyTable[searchPosition.historyIndex(mv)];
                moves++;
            }
            WidgetUtil.shellSort(mvs, vls, 0, moves);
            index = 0;
            singleReply = moves == 1;
        }

        int next() {
            if (phase == PHASE_HASH) {
                phase = PHASE_KILLER_1;
                if (mvHash > 0) {
                    return mvHash;
                }
            }
            if (phase == PHASE_KILLER_1) {
                phase = PHASE_KILLER_2;
                if (mvKiller1 != mvHash && mvKiller1 > 0 && searchPosition.legalMove(mvKiller1)) {
                    return mvKiller1;
                }
            }
            if (phase == PHASE_KILLER_2) {
                phase = PHASE_GEN_MOVES;
                if (mvKiller2 != mvHash && mvKiller2 > 0 && searchPosition.legalMove(mvKiller2)) {
                    return mvKiller2;
                }
            }
            if (phase == PHASE_GEN_MOVES) {
                phase = PHASE_REST;
                mvs = new int[MAX_GEN_MOVES];
                vls = new int[MAX_GEN_MOVES];
                moves = searchPosition.generateAllMoves(mvs);
                for (int i = 0; i < moves; i++) {
                    vls[i] = historyTable[searchPosition.historyIndex(mvs[i])];
                }
                WidgetUtil.shellSort(mvs, vls, 0, moves);
                index = 0;
            }
            while (index < moves) {
                int mv = mvs[index];
                index++;
                if (mv != mvHash && mv != mvKiller1 && mv != mvKiller2) {
                    return mv;
                }
            }
            return 0;
        }
    }

    //搜索到好的走法之后给好的走法加分
    private void setBestMove(int mv, int depth) {
        historyTable[searchPosition.historyIndex(mv)] += depth * depth;
        int[] killers = mvKiller[searchPosition.distance];
        if (killers[0] != mv) {
            killers[1] = killers[0];
            killers[0] = mv;
        }
    }
}
