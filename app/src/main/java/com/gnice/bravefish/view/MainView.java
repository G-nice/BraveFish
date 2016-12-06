package com.gnice.bravefish.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Message;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.gnice.bravefish.R;
import com.gnice.bravefish.constant.ConstantUtil;
import com.gnice.bravefish.factory.GameObjectFactory;
import com.gnice.bravefish.objects.BigFish;
import com.gnice.bravefish.objects.EnemyFish;
import com.gnice.bravefish.objects.GameObject;
import com.gnice.bravefish.objects.LittleFish;
import com.gnice.bravefish.objects.MiddleFish;
import com.gnice.bravefish.objects.MyFish;
import com.gnice.bravefish.objects.SmallFish;
import com.gnice.bravefish.sounds.GameSoundPool;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainView extends BaseView {
//    private int missileCount;        // 导弹的数量
//    private int bigFishScore = 30000;    // 大鱼分数
//    private int middleFishScore = 30000;    // 中型鱼的积分
//    private int smallFishScore = 30000;    // 中型鱼的积分
//    private int littleFishScore = 30000;    // 中型鱼的积分
//    private int bigPlaneScore;        // 大型敌机的积分
//    private int bossPlaneScore;        // boss型敌机的积分

//    private int missileScore;        // 导弹的积分
//    private int bulletScore;        // 子弹的积分
    private int sumScore = 0;            // 游戏总得分
    private int speedTime;            // 游戏速度的倍数
    private float bg_x;                // 图片的坐标
    private float bg_x2;
    private float play_bt_w;
    private float play_bt_h;
    // private float missile_bt_y;
    private boolean isPlay;            // 标记游戏运行状态
    private boolean isTouchPlane;    // 判断玩家是否按下屏幕
    private Bitmap background;        // 背景图片
//    private Bitmap background2;    // 背景图片
    private Bitmap playButton;        // 开始/暂停游戏的按钮图片
//    private Bitmap missile_bt;        // 导弹按钮图标
    private MyFish myFish;        // 玩家鱼
//    private BossPlane bossPlane;    // boss飞机
    private List<EnemyFish> enemyFish;
//    private MissileGoods missileGoods;
//    private BulletGoods bulletGoods;
    private GameObjectFactory factory;


    public MainView(Context context, GameSoundPool sounds) {
        super(context, sounds);
        isPlay = true;
        speedTime = 1;
        factory = new GameObjectFactory();                          //工厂类
        enemyFish = new ArrayList<EnemyFish>();
        myFish = (MyFish) factory.createMyFish(getResources());//生产玩家鱼
        myFish.setMainView(this);

        for (int i = 0; i < LittleFish.sumCount; i++) {
            //生产微型鱼
            LittleFish littleFish = (LittleFish) factory.createLittleFish(getResources());
            enemyFish.add(littleFish);
        }
        for (int i = 0; i < SmallFish.sumCount; i++) {
            //生产小型鱼
            SmallFish smallFish = (SmallFish) factory.createSmallFish(getResources());
            enemyFish.add(smallFish);
        }
        for (int i = 0; i < MiddleFish.sumCount; i++) {
            //生产中型鱼
            MiddleFish middleFish = (MiddleFish) factory.createMiddleFish(getResources());
            enemyFish.add(middleFish);
        }
        for (int i = 0; i < BigFish.sumCount; i++) {
            //生产大型鱼
            BigFish bigFish = (BigFish) factory.createBigFish(getResources());
            enemyFish.add(bigFish);
        }
        thread = new Thread(this);
    }

    // 视图改变的方法
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        super.surfaceChanged(arg0, arg1, arg2, arg3);
    }

    // 视图创建的方法
    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        super.surfaceCreated(arg0);
        initBitmap(); // 初始化图片资源
        for (GameObject obj : enemyFish) {
            obj.setScreenWH(screen_width, screen_height);
        }
        myFish.setScreenWH(screen_width, screen_height);
        myFish.setAlive(true);
        if (thread.isAlive()) {
            thread.start();
        } else {
            thread = new Thread(this);
            thread.start();
        }
    }

    // 视图销毁的方法
    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        super.surfaceDestroyed(arg0);
        release();
    }

    // 响应触屏事件的方法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            isTouchPlane = false;
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
//            final float tan;
//            if (x != myFish.getMiddle_x()) {
//                tan = (y - myFish.getMiddle_y()) / (x - myFish.getMiddle_x());
//            } else {
//                tan = 0;
//            }

            if (x > 10 && x < 10 + play_bt_w && y > 10 && y < 10 + play_bt_h) {
                if (isPlay) {
                    isPlay = false;
                } else {
                    isPlay = true;
                    synchronized (thread) {
                        thread.notify();
                    }
                }
                return true;
            }
            //判断玩家鱼是否被按下
            else if (x > myFish.getObject_x() && x < myFish.getObject_x() + myFish.getObject_width()
                    && y > myFish.getObject_y() && y < myFish.getObject_y() + myFish.getObject_height()) {
                if (isPlay) {
                    isTouchPlane = true;
                }
                return true;
            } else {
//                直接向点击方向游动
//                myFish.setMiddle_x(myFish.getMiddle_x() + (angle > 0 ? -1 : 1) * myFish.getSpeed());
//                myFish.setMiddle_y(myFish.getMiddle_y() + angle * myFish.getSpeed());

                final float x_tmp = x;
                final float y_tmp = y;

                new Thread(new Runnable() {
                    public void run() {
//                        double angle = (float) Math.tanh(tan);
                        double distance = Math.sqrt((double) Math.pow(x_tmp - myFish.getMiddle_x(), 2) + Math.pow(y_tmp - myFish.getMiddle_y(), 2));
                        int step = (int)Math.round(distance / myFish.getSpeed()) + 1;
                        float step_x = (x_tmp - myFish.getMiddle_x()) / step;
                        float step_y = (y_tmp - myFish.getMiddle_y()) / step;
                        for (int i = step; i > 0; i--) {
                            myFish.setMiddle_x(myFish.getMiddle_x() + step_x);
                            myFish.setMiddle_y(myFish.getMiddle_y() + step_y);
//                            Log.i("thread ", "sleep");
                            SystemClock.sleep(5);
                        }

                        /*
                        while (x_tmp != myFish.getMiddle_x() || y_tmp != myFish.getMiddle_y()) {
                            if (Math.abs(myFish.getMiddle_x() - x_tmp) <= myFish.getSpeed()) {
                                myFish.setMiddle_x(x_tmp);
                            } else {
                                //                        myFish.setMiddle_x(myFish.getMiddle_x() + (x >= myFish.getMiddle_x() ? 1 : -1) * myFish.getSpeed() );
                                myFish.setMiddle_x(myFish.getMiddle_x() + (x_tmp >= myFish.getMiddle_x() ? 1 : -1) * myFish.getSpeed());
                            }
                            if (Math.abs(myFish.getMiddle_y() - y_tmp) <= myFish.getSpeed()) {
                                myFish.setMiddle_y(y_tmp);
                            } else {
                                //                        myFish.setMiddle_y(myFish.getMiddle_y() + (y >= myFish.getMiddle_y() ? 1 : -1) * myFish.getSpeed());
                                myFish.setMiddle_y(myFish.getMiddle_y() + (y_tmp >= myFish.getMiddle_y() ? 1 : -1) * myFish.getSpeed());
                            }
                            Log.i("thread ", "sleep");
                            SystemClock.sleep(5);
                        }
                        */
                    }
                }).start();
/*
                Log.i("touch ", "click");
                while (x != myFish.getMiddle_x() || y != myFish.getMiddle_y()) {
                    if (Math.abs(myFish.getMiddle_x() - x) <= myFish.getSpeed()) {
                        myFish.setMiddle_x(x);
                    } else {
//                        myFish.setMiddle_x(myFish.getMiddle_x() + (x >= myFish.getMiddle_x() ? 1 : -1) * myFish.getSpeed() );
                        myFish.setMiddle_x(myFish.getMiddle_x() + (x >= myFish.getMiddle_x() ? 1 : -1) * myFish.getSpeed());
                    }
                    if (Math.abs(myFish.getMiddle_y() - y) <= myFish.getSpeed()) {
                        myFish.setMiddle_y(y);
                    } else {
//                        myFish.setMiddle_y(myFish.getMiddle_y() + (y >= myFish.getMiddle_y() ? 1 : -1) * myFish.getSpeed());
                        myFish.setMiddle_y(myFish.getMiddle_y() + (y >= myFish.getMiddle_y() ? 1 : -1) * myFish.getSpeed());
                    }
                }
*/
                isTouchPlane = false;
                return false;
            }
        }
        //响应手指在屏幕移动的事件
        else if (event.getAction() == MotionEvent.ACTION_MOVE && event.getPointerCount() == 1) {
            //判断触摸点是否为玩家的飞机
            if (isTouchPlane) {
                float x = event.getX();
                float y = event.getY();
                if (x > myFish.getMiddle_x() + 20) {
                    if (myFish.getMiddle_x() + myFish.getSpeed() <= screen_width) {
                        myFish.setMiddle_x(myFish.getMiddle_x() + myFish.getSpeed());
                    }
                } else if (x < myFish.getMiddle_x() - 20) {
                    if (myFish.getMiddle_x() - myFish.getSpeed() >= 0) {
                        myFish.setMiddle_x(myFish.getMiddle_x() - myFish.getSpeed());
                    }
                }
                if (y > myFish.getMiddle_y() + 20) {
                    if (myFish.getMiddle_y() + myFish.getSpeed() <= screen_height) {
                        myFish.setMiddle_y(myFish.getMiddle_y() + myFish.getSpeed());
                    }
                } else if (y < myFish.getMiddle_y() - 20) {
                    if (myFish.getMiddle_y() - myFish.getSpeed() >= 0) {
                        myFish.setMiddle_y(myFish.getMiddle_y() - myFish.getSpeed());
                    }
                }
                return true;
            }
        }
        return false;
    }

    // 初始化图片资源方法
    @Override
    public void initBitmap() {
        playButton = BitmapFactory.decodeResource(getResources(), R.drawable.play);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        scalex = screen_width / background.getWidth();
        scaley = screen_height / background.getHeight();
        play_bt_w = playButton.getWidth();
        play_bt_h = playButton.getHeight() / 2;
        bg_x = 0;
        bg_x2 = bg_x - screen_width;
    }

    //初始化游戏对象
    public void initObject() {
        for (EnemyFish obj : enemyFish) {
            //初始化微型鱼
            if (obj instanceof LittleFish) {
                if (!obj.isAlive()) {
                    obj.initial(speedTime, 0, 0);
                    break;
                }
            }
            //初始化小型鱼
            if (obj instanceof SmallFish) {
                if (!obj.isAlive()) {
                    obj.initial(speedTime, 0, 0);
                    break;
                }
            }
            //初始化中型鱼
            if (obj instanceof MiddleFish) {
//                if (middleFishScore > 10000) {
                    if (!obj.isAlive()) {
                        obj.initial(speedTime, 0, 0);
                        break;
                    }
//                }
            }
            //初始化大型鱼
            else if (obj instanceof BigFish) {
//                if (bigFishScore >= 25000) {
                    if (!obj.isAlive()) {
                        obj.initial(speedTime, 0, 0);
                        break;
                    }
//                }
            }
        }

        //提升等级
        if (sumScore >= speedTime * 10000 && speedTime < 6) {
            speedTime++;
        }
    }

    // 释放图片资源的方法
    @Override
    public void release() {
        for (GameObject obj : enemyFish) {
            obj.release();
        }
        myFish.release();

        if (!playButton.isRecycled()) {
            playButton.recycle();
        }
        if (!background.isRecycled()) {
            background.recycle();
        }
    }

    // 绘图方法
    @Override
    public void drawSelf() {
        try {
            canvas = sfh.lockCanvas();
            canvas.drawColor(Color.BLACK); // 绘制背景色
            canvas.save();
            // 计算背景图片与屏幕的比例
            canvas.scale(scalex, scaley, 0, 0);
            canvas.drawBitmap(background, bg_x, 0, paint);   // 绘制背景图
//            canvas.drawBitmap(background2, bg_x2,  0, paint); // 绘制背景图
            canvas.restore();
            //绘制按钮
            canvas.save();
            canvas.clipRect(10, 10, 10 + play_bt_w, 10 + play_bt_h);
            if (isPlay) {
                canvas.drawBitmap(playButton, 10, 10, paint);
            } else {
                canvas.drawBitmap(playButton, 10, 10 - play_bt_h, paint);
            }
            canvas.restore();

            //绘制敌鱼
            for (EnemyFish obj : enemyFish) {
                if (obj.isAlive()) {
                    obj.drawSelf(canvas);
                    //检测大鱼是否与玩家的鱼碰撞
                    if (obj.isCanCollide() && myFish.isAlive()) {
                        if (obj.isCollide(myFish)) {
                            if (myFish.getWeight() <= obj.getWeight()) {
                                //  被吃掉
//                                Log.i("Collide: ", "die");
                                myFish.setAlive(false);
                            } else {
                                //   吃别人
//                                Log.i("Collide: ", "eat others");
                                addGameScore(obj.getScore());
                                myFish.addWeight(sumScore);
                                obj.setAlive(false);
                            }
                        }
                    }
                }
            }
            if (!myFish.isAlive()) {
                threadFlag = false;
                sounds.playSound(4, 0);            //鱼被吃掉的音效
            }
            myFish.drawSelf(canvas);    //绘制玩家的飞机
//            myFish.shoot(canvas, enemyFish);
//            sounds.playSound(1, 0);      //子弹音效


            //绘制积分文字
            paint.setTextSize(30);
            paint.setColor(Color.rgb(235, 161, 1));
            canvas.drawText("积分:" + String.valueOf(sumScore), 30 + play_bt_w, 40, paint);        //绘制文字
            canvas.drawText("等级 X " + String.valueOf(speedTime), screen_width - 150, 40, paint); //绘制文字
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);
        }
    }

    // 背景移动的逻辑函数
    public void viewLogic() {
        if (bg_x > bg_x2) {
            bg_x += 10;
            bg_x2 = bg_x - background.getWidth();
        } else {
            bg_x2 += 10;
            bg_x = bg_x2 - background.getWidth();
        }
        if (bg_x >= background.getWidth()) {
            bg_x = bg_x2 - background.getWidth();
        } else if (bg_x2 >= background.getWidth()) {
            bg_x2 = bg_x - background.getWidth();
        }
    }


    // 增加游戏分数的方法
    public void addGameScore(int score) {
//        littleFishScore += score;    // 中型敌机的积分
//        smallFishScore += score;    // 中型敌机的积分
//        middleFishScore += score;    // 中型敌机的积分
//        bigFishScore += score;        // 大型敌机的积分
//        bossFishScore += score;    // boss型敌机的积分
        sumScore += score;            // 游戏总得分
    }

    // 播放音效
    public void playSound(int key) {
        sounds.playSound(key, 0);
    }

    // 线程运行的方法
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (threadFlag) {
            long startTime = System.currentTimeMillis();
            initObject();
            drawSelf();
//            viewLogic();        //背景移动的逻辑
            long endTime = System.currentTimeMillis();
            if (!isPlay) {
                synchronized (thread) {
                    try {
                        thread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                if (endTime - startTime < 100)
                    sleep(100 - (endTime - startTime));
            } catch (InterruptedException err) {
                err.printStackTrace();
            }
        }
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Message message = new Message();
        message.what = ConstantUtil.TO_END_VIEW;
        message.arg1 = Integer.valueOf(sumScore);
        mainActivity.getHandler().sendMessage(message);
    }

}
