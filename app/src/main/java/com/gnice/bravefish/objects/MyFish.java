package com.gnice.bravefish.objects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.gnice.bravefish.R;
import com.gnice.bravefish.factory.GameObjectFactory;
import com.gnice.bravefish.interfaces.IMyFish;
import com.gnice.bravefish.view.MainView;

public class MyFish extends GameObject implements IMyFish {
    private float middle_x;             // 鱼的中心坐标
    private float middle_y;

    private float object_width_original;  // 用于读取素材
    private float object_height_original;

    // private long startTime;             // 开始的时间
    // private long endTime;             // 结束的时间
    // private boolean isChangeBullet;  // 标记更换了子弹
    private Bitmap myFish;             // 飞机飞行时的图片

    private MainView mainView;
    private GameObjectFactory factory;

    public MyFish(Resources resources) {
        super(resources);
        weight = 100;
        initBitmap();
        this.speed = 8;
        factory = new GameObjectFactory();

    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    // 设置屏幕宽度和高度
    @Override
    public void setScreenWH(float screen_width, float screen_height) {
        super.setScreenWH(screen_width, screen_height);
        // 初始化鱼在屏幕底部中间以及鱼中心位置
        object_x = screen_width / 2 - object_width / 2;
        object_y = screen_height - object_height;
        middle_x = object_x + object_width / 2;
        middle_y = object_y + object_height / 2;
    }

    // 初始化图片资源的
    @Override
    public void initBitmap() {
        myFish = BitmapFactory.decodeResource(resources, R.drawable.myfish);
        object_width = myFish.getWidth() / 5; // 获得每一帧位图的宽  // 资源图片为横向两帧并列
        object_width_original = object_width;
        object_height = myFish.getHeight();    // 获得每一帧位图的高
        object_height_original = object_height;
        scalex = scaley = (weight - 100) / 100 + 0.5F;
        object_width = object_width_original * scalex;
        object_height = object_height_original * scalex;
//        Log.i("scale 000", "" + scalex);
//        Log.i("weight 000", "" + weight);
    }

    // 对象的绘图方法
    @Override
    public void drawSelf(Canvas canvas) {
        if (isAlive) {
            int x = (int) (currentFrame * object_width_original); // 获得当前帧相对于位图的X坐标
            canvas.save();

//            if ((scalex - scaley) > 0.01 && scalex <= 6) {
            if (scalex != ((weight - 100) / 100 + 0.5F) && scalex <= 6) {
                scaley = scalex = (int) Math.floor((weight - 100) / 100 + 0.5F);
                Log.i("scale change", "" + scalex);
                object_width = object_width_original * scalex;
                object_height = object_height_original * scalex;
            }

            canvas.scale(scalex, scaley, middle_x, middle_y);
            canvas.scale(scalex, scaley, object_x, object_y);
//            canvas.clipRect(object_x, object_y, object_x + object_width, object_y + object_height);
            canvas.clipRect(object_x, object_y, object_x + object_width_original, object_y + object_height_original);
            canvas.drawBitmap(myFish, object_x - x, object_y, paint);
            canvas.restore();
            currentFrame++;
            if (currentFrame >= 3) {
                currentFrame = 0;
            }
        }
    }

    // 释放资源的方法
    @Override
    public void release() {
        if (!myFish.isRecycled()) {
            myFish.recycle();
        }
    }

    //getter和setter方法
    // public void setStartTime(long startTime) {
    //     this.startTime = startTime;
    // }

    @Override
    public float getMiddle_x() {
        return middle_x;
    }

    @Override
    public void setMiddle_x(float middle_x) {
        this.middle_x = middle_x;
        this.object_x = middle_x - object_width / 2;
    }

    @Override
    public float getMiddle_y() {
        return middle_y;
    }

    @Override
    public void setMiddle_y(float middle_y) {
        this.middle_y = middle_y;
        this.object_y = middle_y - object_height / 2;
    }
}
