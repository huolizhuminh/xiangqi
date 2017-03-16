package com.minhuizhu.mynewchess.manager;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.minhuizhu.mynewchess.core.MyApplication;


/**
 * Created by zhuminh on 2017/2/21.
 */

public class MusicManager {
    private static final String BTN_DOWN = "btndown.wav";
    private static final String GO = "go.wav";
    private MediaPlayer player;
    private AssetManager assetManager;
    private MusicManager instance;
    private int volume = 3;
    private String TAGS = "MusicManager";

    public void setVolume(int volume) {
        this.volume = volume;
    }
    public int  getVolume() {
        return volume;
    }
    public static MusicManager getInstance() {
        return Internal.INSTANCE;
    }

    private static class Internal {
        private static final MusicManager INSTANCE = new MusicManager();
    }

    private MusicManager() {

        assetManager = MyApplication.getContext().getAssets();
    }

    public void playBtnDownMusic() {
        playMusic(BTN_DOWN);
    }

    public void playGoMusic() {
        playMusic(GO);
    }

    private void playMusic(String name) {
        try {
            player = new MediaPlayer();
            AssetFileDescriptor fileDescriptor = assetManager.openFd(name);
            player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            player.setVolume(volume, volume);
            player.prepare();
            player.start();
        } catch (Exception e) {
            Log.e(TAGS, "playMusicError");
        }

    }
}
