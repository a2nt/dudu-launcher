package com.wow.carlauncher.ex.manage.appInfo;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by 10124 on 2018/5/11.
 */

public class InternalAppInfo extends AppInfo {
    public InternalAppInfo(String name, String packageName, int appMark, Class<? extends AppCompatActivity> LoadClazz) {
        super(name, packageName, appMark);
        this.loadClazz = LoadClazz;
    }

    public Class<? extends AppCompatActivity> loadClazz;
}
