package com.wow.carlauncher.common.util;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ViewUtils {

    /**
     * 根据手机的分辨率�? px(像素) 的单�? 转成�? dp
     */
    public static float scale = -1;

    public static float px2dipf(Context context, float pxValue) {
        if (scale == -1) {
            scale = context.getResources().getDisplayMetrics().density;
        }
        return px2dipf(scale, pxValue);
    }

    /**
     * 根据手机的分辨率�? dp 的单�? 转成�? px(像素)
     */
    public static float dip2pxf(Context context, float dpValue) {
        if (scale == -1) {
            scale = context.getResources().getDisplayMetrics().density;
        }
        return dip2pxf(scale, dpValue);
    }

    public static float px2dipf(float scale, float pxValue) {
        return (pxValue / scale + 0.5f);
    }

    public static float dip2pxf(float scale, float dpValue) {
        return (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率�? px(像素) 的单�? 转成�? dp
     */
    public static int px2dipi(Context context, float pxValue) {
        if (scale == -1) {
            scale = context.getResources().getDisplayMetrics().density;
        }
        return px2dipi(scale, pxValue);
    }

    /**
     * 根据手机的分辨率�? dp 的单�? 转成�? px(像素)
     */
    public static int dip2pxi(Context context, float dpValue) {
        if (scale == -1) {
            scale = context.getResources().getDisplayMetrics().density;
        }
        return dip2pxi(scale, dpValue);
    }

    public static int px2dipi(float scale, float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2pxi(float scale, float dpValue) {
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getTypedValueResourceId(int resid, Context context) {
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(resid, tv, true);
        return tv.resourceId;
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将GridView改成单行横向布局
     */
    public static void changeGridView(int dataSize, Context context, GridView gv_record) {
        // item宽度
        int itemWidth = ViewUtils.dip2px(context, 100);
        // item之间的间隔
        int itemPaddingH = ViewUtils.dip2px(context, 1);
        // 计算GridView宽度
        int gridviewWidth = dataSize * (itemWidth + itemPaddingH);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gv_record.setLayoutParams(params);
        gv_record.setColumnWidth(itemWidth);
        gv_record.setHorizontalSpacing(itemPaddingH);
        gv_record.setStretchMode(GridView.NO_STRETCH);
        gv_record.setNumColumns(dataSize);
    }

    public static void backgroundAlpha(AppCompatActivity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    public static DisplayMetrics getDisplayMetriocs(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }

    public static void setBrightness(AppCompatActivity activity, int brightness) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        activity.getWindow().setAttributes(lp);
    }

    public static View getDeepViewByIndex(View view, int[] deeps) {
        View r;
        for (int i = 0; i < deeps.length; i++) {
            int deep = deeps[i];
            if (view instanceof ViewGroup) {
                view = ((ViewGroup) view).getChildAt(deep);
            }
            if (deep != deeps.length - 1) {
                if (view == null) {
                    return null;
                }
            }
        }
        r = view;
        return r;
    }

    public static View getViewByIds(View view, Object[] ids) {
        View r = view;
        for (int i = 0; i < ids.length; i++) {
            r = getViewById(r, ids[i]);
            if (r == null) {
                return null;
            }
        }
        return r;
    }

    public static View getViewById(View view, Object id) {
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View cc1 = vg.getChildAt(i);
                if (id instanceof Integer) {
                    if (id.equals(i)) {
                        return cc1;
                    }
                } else if (id instanceof String) {
                    if (cc1.toString().endsWith("id/" + id + "}")) {
                        return cc1;
                    }
                }
            }
        }
        return null;
    }

    public static void enterFullScreen(AppCompatActivity activity) {
        int flags = View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 让View全屏显示，Layout会被拉伸到StatusBar下面，不包含NavigationBar。
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION // 让View全屏显示，Layout会被拉伸到StatusBar和NavigationBar下面。
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // Activity全屏显示，且状态栏被隐藏覆盖掉。等同于（WindowManager.LayoutParams.FLAG_FULLSCREEN）。
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; // 隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 这个flag只有当设置了SYSTEM_UI_FLAG_HIDE_NAVIGATION才起作用。如果没有设置这个flag，
                // 任意的View相互动作都退出SYSTEM_UI_FLAG_HIDE_NAVIGATION模式。如果设置就不会退出。
                flags |= View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
        }

        activity.getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    public static void exitFullScreen(AppCompatActivity activity) {
        int flags = View.SYSTEM_UI_FLAG_VISIBLE;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 让View全屏显示，Layout会被拉伸到StatusBar下面，不包含NavigationBar。
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;// 让View全屏显示，Layout会被拉伸到StatusBar和NavigationBar下面
        }

        activity.getWindow().getDecorView().setSystemUiVisibility(flags);
    }
}
