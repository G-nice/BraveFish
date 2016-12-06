package com.gnice.bravefish.objects;

import android.content.res.Resources;
import android.graphics.Canvas;

// 非己鱼
public class EnemyFish extends GameObject {
    protected int score;                         // 对象的分值
    protected boolean isVisible;                 //	 对象是否为可见状态

    public EnemyFish(Resources resources) {
        super(resources);
        initBitmap();            // 初始化图片资源
    }

    //初始化数据
    @Override
    public void initial(int acceleratTemes, float middle_x, float middle_y) {
    }

    // 初始化图片资源
    @Override
    public void initBitmap() {

    }

    // 对象的绘图函数
    @Override
    public void drawSelf(Canvas canvas) {
        //判断敌机是否死亡状态

    }

    // 释放资源
    @Override
    public void release() {

    }

    // 对象的逻辑函数
    @Override
    public void logic() {
        //        更新物件位置
        if (object_x < screen_width) {
            //            行走speed距离
            object_x += speed;
        } else {
            //            走出屏幕
            isAlive = false;
        }
        if (object_x + object_width > 0) {
            //            从上边出现
            isVisible = true;
        } else {
            isVisible = false;
        }
    }


    // 检测碰撞
    // TODO: 2016/12/3 升级碰撞算法  不规则碰撞检测
    @Override
    public boolean isCollide(GameObject obj) {
        // 矩形1位于矩形2的左侧  1 自己 ||  2 别人 玩家鱼
        int adjust = 15;  // 边缘调整
        if (object_x <= obj.getObject_x() + adjust
                && object_x + object_width <= obj.getObject_x() + adjust) {
            return false;
        }
        // 矩形1位于矩形2的右侧
        else if (obj.getObject_x() <= object_x + adjust
                && obj.getObject_x() + obj.getObject_width() <= object_x) {
            return false;
        }
        // 矩形1位于矩形2的上方
        else if (object_y <= obj.getObject_y() + adjust
                && object_y + object_height <= obj.getObject_y() + adjust) {
            return false;
        }
        // 矩形1位于矩形2的下方
        else if (obj.getObject_y() <= object_y + adjust
                && obj.getObject_y() + obj.getObject_height() <= object_y) {
            return false;
        }
        return true;
    }

    // 判断能否被检测碰撞
    public boolean isCanCollide() {
        return isAlive && isVisible;
    }

    //getter和setter方法
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
