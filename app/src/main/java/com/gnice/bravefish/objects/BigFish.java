package com.gnice.bravefish.objects;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.gnice.bravefish.R;

import java.util.Random;

public class BigFish extends EnemyFish {
    public static int sumCount = 2;             //	对象总的数量
    private static int currentCount = 0;     //	对象当前的数量
    private Bitmap bigFish; // 对象图片

    public BigFish(Resources resources) {
        super(resources);
        this.score = 200;        // 为对象设置分数
    }

    //初始化数据
    @Override
    public void initial(int acceleratTemes, float middle_x, float middle_y) {
        isAlive = true;
        weight = 200;
        Random ran = new Random();
        speed = ran.nextInt(2) + 4 * acceleratTemes;
        object_x = -object_width * (currentCount * 2 + 1);
        object_y = ran.nextInt((int) (screen_height - object_height));
        currentCount++;
        if (currentCount >= sumCount) {
            currentCount = 0;
        }
    }

    // 初始化图片资源
    @Override
    public void initBitmap() {
        bigFish = BitmapFactory.decodeResource(resources, R.drawable.bigfish);
        object_width = bigFish.getWidth() / 5;            //获得每一帧位图的宽
        object_height = bigFish.getHeight();        //获得每一帧位图的高
    }

    // 对象的绘图函数
    @Override
    public void drawSelf(Canvas canvas) {
        if (isAlive) {
            if (isVisible) {
                int x = (int) (currentFrame * object_width); // 获得当前帧相对于位图的X坐标
                canvas.save();
                canvas.clipRect(object_x, object_y, object_x + object_width, object_y + object_height);
                canvas.drawBitmap(bigFish, object_x - x, object_y, paint);
                canvas.restore();
                currentFrame++;
                if (currentFrame >= 3) {
                    currentFrame = 0;
//                    isAlive = false;
                }
            }
            logic();
        }
    }

    // 释放资源
    @Override
    public void release() {
        if (!bigFish.isRecycled()) {
            bigFish.recycle();
        }
    }

}
