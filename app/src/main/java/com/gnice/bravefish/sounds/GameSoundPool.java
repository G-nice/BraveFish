package com.gnice.bravefish.sounds;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.gnice.bravefish.MainActivity;
import com.gnice.bravefish.R;

import java.util.HashMap;

public class GameSoundPool {
    private MainActivity mainActivity;
    private SoundPool soundPool;
    private HashMap<Integer, Integer> map;

    public GameSoundPool(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        map = new HashMap<Integer, Integer>();
        soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);  // 最多支持8个音效同时播放
    }

    public void initGameSound() {
        map.put(1, soundPool.load(mainActivity, R.raw.shoot, 1));
        map.put(2, soundPool.load(mainActivity, R.raw.explosion, 1));
        map.put(3, soundPool.load(mainActivity, R.raw.explosion2, 1));
        map.put(4, soundPool.load(mainActivity, R.raw.explosion3, 1));
        map.put(5, soundPool.load(mainActivity, R.raw.bigexplosion, 1));
        map.put(6, soundPool.load(mainActivity, R.raw.get_goods, 1));
        map.put(7, soundPool.load(mainActivity, R.raw.button, 1));
    }

    //播放音效
    public void playSound(int sound, int loop) {
        AudioManager am = (AudioManager) mainActivity.getSystemService(Context.AUDIO_SERVICE);
        float stramVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float stramMaxVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volume = stramVolumeCurrent / stramMaxVolumeCurrent;
        soundPool.play(map.get(sound), volume, volume, 1, loop, 1.0f);
    }
}
