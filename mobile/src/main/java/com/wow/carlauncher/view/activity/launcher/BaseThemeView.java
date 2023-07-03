package com.wow.carlauncher.view.activity.launcher;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import com.wow.carlauncher.ex.manage.skin.SkinManage;
import com.wow.carlauncher.view.base.BaseView;

/**
 * Created by 10124 on 2018/4/22.
 */

public abstract class BaseThemeView extends BaseView implements SkinManage.OnSkinChangeListener {
    public BaseThemeView(@NonNull Context context) {
        super(context);
    }

    public BaseThemeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected String skinName;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        changedSkin(SkinManage.self());
        SkinManage.self().registerSkinChangeListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SkinManage.self().unregisterSkinChangeListener(this);
    }

    @Override
    public void onSkinChanged(SkinManage manage) {
        if (!manage.getSkinMark().equals(skinName)) {
            skinName = manage.getSkinMark();
            changedSkin(manage);
        }
    }

    public void changedSkin(SkinManage manage) {

    }
}
