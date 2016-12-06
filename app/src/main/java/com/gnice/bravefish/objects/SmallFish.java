package com.gnice.bravefish.objects;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.gnice.bravefish.R;

import java.util.Random;

public class SmallFish extends EnemyFish {
    public static int sumCount = 8;             //	对象总的数量
    private static int currentCount = 0;     //	对象当前的数量
    private Bitmap smallFish; // 对象图片

    public SmallFish(Resources resources) {
        super(resources);
        // TODO Auto-generated constructor stub
        this.score = 100;        // 为对象设置分数
    }

    //初始化数据
    @Override
    public void initial(int arg0, float arg1, float arg2) {
        isAlive = true;
        weight = 10;
        Random ran = new Random();
        speed = ran.nextInt(8) + 8 * arg0;
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
        Random random = new Random();
        int smallFishPic = random.nextInt(3);

        switch (smallFishPic) {
            case 0:
                smallFish = BitmapFactory.decodeResource(resources, R.drawable.smallfish1);
                break;
            case 1:
                smallFish = BitmapFactory.decodeResource(resources, R.drawable.smallfish2);
                break;
            case 2:
                smallFish = BitmapFactory.decodeResource(resources, R.drawable.smallfish3);
                break;
            default:
                smallFish = BitmapFactory.decodeResource(resources, R.drawable.smallfish1);
                break;
        }

        object_width = smallFish.getWidth() / 5;            //获得每一帧位图的宽
        object_height = smallFish.getHeight();        //获得每一帧位图的高
    }

    // 对象的绘图函数
    @Override
    public void drawSelf(Canvas canvas) {
        //判断敌机是否死亡状态
        if (isAlive) {
                if (isVisible) {
                    int x = (int) (currentFrame * object_width); // 获得当前帧相对于位图的X坐标
                    canvas.save();
                    canvas.clipRect(object_x, object_y, object_x + object_width, object_y + object_height);
                    canvas.drawBitmap(smallFish, object_x - x, object_y, paint);
                    canvas.restore();
                    currentFrame++;
                    if (currentFrame >= 3) {
                        currentFrame = 0;
//                        isAlive = false;
                    }
                }
                logic();
        }
    }

    // 释放资源
    @Override
    public void release() {
        if (!smallFish.isRecycled()) {
            smallFish.recycle();
        }
    }
}
