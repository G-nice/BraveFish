package com.gnice.bravefish.factory;


import android.content.res.Resources;

import com.gnice.bravefish.objects.BigFish;
import com.gnice.bravefish.objects.GameObject;
import com.gnice.bravefish.objects.LittleFish;
import com.gnice.bravefish.objects.MiddleFish;
import com.gnice.bravefish.objects.MyFish;
import com.gnice.bravefish.objects.SmallFish;

/*游戏对象的工厂类*/
public class GameObjectFactory {
    //创建小鱼的方法
    public GameObject createLittleFish(Resources resources) {
        return new LittleFish(resources);
    }

    //创建小鱼的方法
    public GameObject createSmallFish(Resources resources) {
        return new SmallFish(resources);
    }

    //创建中型鱼的方法
    public GameObject createMiddleFish(Resources resources) {
        return new MiddleFish(resources);
    }

    //创建大型鱼的方法
    public GameObject createBigFish(Resources resources) {
        return new BigFish(resources);
    }


    //创建玩家鱼的方法
    public GameObject createMyFish(Resources resources) {
        return new MyFish(resources);
    }
}
