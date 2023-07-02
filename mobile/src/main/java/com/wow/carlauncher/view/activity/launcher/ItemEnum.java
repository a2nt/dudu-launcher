package com.wow.carlauncher.view.activity.launcher;

import android.content.Context;
import android.view.View;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.activity.launcher.view.LAMapView;
import com.wow.carlauncher.view.activity.launcher.view.LMusicView;
import com.wow.carlauncher.view.activity.launcher.view.LObdView;
import com.wow.carlauncher.view.activity.launcher.view.LPersionView;
import com.wow.carlauncher.view.activity.launcher.view.LTaiyaView;
import com.wow.carlauncher.view.activity.launcher.view.LTimeView;
import com.wow.carlauncher.view.activity.launcher.view.LWeatherView;
import com.wow.carlauncher.view.activity.launcher.view.LDuduFmView;
import com.wow.carlauncher.view.activity.set.setItem.SetEnum;

public enum ItemEnum implements SetEnum {
    AMAP("高德导航", 1),
    MUSIC("音乐", 2),
    WEATHER("天气", 3),
    TAIYA("胎压", 4),
    OBD("车况", 5),
    TIME(String.valueOf(R.string.time_title), 6),
    DUDUFM("嘟嘟FM", 7),
    PERSION("个人中心", 8);
    private String name;
    private Integer id;


    ItemEnum(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static ItemEnum getById(Integer id) {
        switch (id) {
            case 1:
                return AMAP;
            case 2:
                return MUSIC;
            case 3:
                return WEATHER;
            case 4:
                return TIME;
            case 5:
                return TAIYA;
            case 6:
                return OBD;
            case 7:
                return DUDUFM;
            case 8:
                return PERSION;
        }
        throw new RuntimeException();
    }

    public static View createView(Context context, ItemEnum itemEnum) {
        switch (itemEnum) {
            case AMAP:
                return new LAMapView(context);
            case MUSIC:
                return new LMusicView(context);
            case WEATHER:
                return new LWeatherView(context);
            case TIME:
                return new LTimeView(context);
            case TAIYA:
                return new LTaiyaView(context);
            case OBD:
                return new LObdView(context);
            case DUDUFM:
                return new LDuduFmView(context);
            case PERSION:
                return new LPersionView(context);
        }
        return null;
    }
}
