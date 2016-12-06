package com.gnice.bravefish;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.gnice.bravefish.constant.ConstantUtil;
import com.gnice.bravefish.sounds.GameSoundPool;
import com.gnice.bravefish.view.EndView;
import com.gnice.bravefish.view.MainView;
import com.gnice.bravefish.view.ReadyView;

public class MainActivity extends Activity {
    private EndView endView;
    private MainView mainView;
    private ReadyView readyView;
    private GameSoundPool sounds;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ConstantUtil.TO_MAIN_VIEW) {
                //                转到主页面
                toMainView();
            } else if (msg.what == ConstantUtil.TO_END_VIEW) {
                //                gameover
                toEndView(msg.arg1);
            } else if (msg.what == ConstantUtil.END_GAME) {
                //                退出游戏
                endGame();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        // 短音效
        sounds = new GameSoundPool(this);
        sounds.initGameSound();
        readyView = new ReadyView(this, sounds);

        // 设置布局视图
        setContentView(readyView);
    }

    //显示游戏的主界面
    public void toMainView() {
        if (mainView == null) {
            mainView = new MainView(this, sounds);
        }
        // 设置布局视图
        setContentView(mainView);
        readyView = null;
        endView = null;
    }

    //显示游戏结束的界面
    public void toEndView(int score) {
        if (endView == null) {
            endView = new EndView(this, sounds);
            endView.setScore(score);
        }
        setContentView(endView);
        mainView = null;
    }

    //结束游戏
    public void endGame() {
        if (readyView != null) {
            readyView.setThreadFlag(false);
        } else if (mainView != null) {
            mainView.setThreadFlag(false);
        } else if (endView != null) {
            endView.setThreadFlag(false);
        }
        this.finish();
    }

    //getter和setter方法
    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    // TODO 是否需要处理按下 返回键 以及 HOME键

}
