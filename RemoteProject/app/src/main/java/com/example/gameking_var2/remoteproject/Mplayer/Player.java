package com.example.gameking_var2.remoteproject.Mplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

import java.util.Timer;

/**
 * Created by 孔雀舞 on 2015/8/31.
 */
public class Player implements  OnCompletionListener,MediaPlayer.OnPreparedListener{

    public MediaPlayer mediaPlayer;
    private Timer mTimer = new Timer();
    private String videoUrl;
    private boolean pause;
    private int playPosition;

    public Player(String videoUrl) {
        this.videoUrl = videoUrl;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }

    }


    /**
     * 來電話了
     */
    public void callIsComing() {
        if (mediaPlayer.isPlaying()) {
            playPosition = mediaPlayer.getCurrentPosition();// 獲得當前播放位置
            mediaPlayer.stop();
        }
    }

    /**
     * 通話結束
     */
    public void callIsDown() {
        if (playPosition > 0) {
            playNet(playPosition);
            playPosition = 0;
        }
    }

    /**
     * 播放
     */
    public void play() {
        playNet(0);
    }

    /**
     * 重播
     */
    public void replay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);// 從開始位置開始播放音樂
        } else {
            playNet(0);
        }
    }

    /**
     * 暫停
     */
    public boolean pause() {
        if (mediaPlayer.isPlaying()) {// 如果正在播放
            mediaPlayer.pause();// 暫停
            pause = true;
        } else {
            if (pause) {// 如果處於暫停狀態
                mediaPlayer.start();// 繼續播放
                pause = false;
            }
        }
        return pause;
    }

    /**
     * 停止
     */
    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
/**
 * 通過onPrepared播放
 */
    public void onPrepared(MediaPlayer arg0) {
        arg0.start();
        Log.e("mediaPlayer", "onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        Log.e("mediaPlayer", "onCompletion");
    }


    /**
     * 播放音樂
     *
     * @param playPosition
     */
    private void playNet(int playPosition) {
        try {
            mediaPlayer.reset();// 把各項参數恢复到初始狀態
            /**
             * 通過MediaPlayer.setDataSource()
             * 的方法,將URL或文件路徑以字符串的方式傳入.使用setDataSource ()方法時,要注意以下三點:
             * 1.構建完成的MediaPlayer 必須實現Null 對像的檢查.
             * 2.必須實現接收IllegalArgumentException 與IOException
             * 等異常,在很多情況下,你所用的文件當下並不存在. 3.若使用URL 來播放在線媒體文件,該文件應該要能支持pragressive
             * 下載.
             */
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepare();// 進行緩沖
            mediaPlayer.setOnPreparedListener(new MyPreparedListener(
                    playPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final class MyPreparedListener implements
            MediaPlayer.OnPreparedListener {
        private int playPosition;

        public MyPreparedListener(int playPosition) {
            this.playPosition = playPosition;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();// 開始播放
            if (playPosition > 0) {
                mediaPlayer.seekTo(playPosition);
            }
        }
    }


}

