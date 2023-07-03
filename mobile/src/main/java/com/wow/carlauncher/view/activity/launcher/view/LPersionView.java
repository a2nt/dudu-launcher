package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.common.user.event.UEventRefreshLoginState;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.activity.launcher.BaseThemeView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LPersionView extends BaseThemeView {

    public LPersionView(@NonNull Context context) {
        super(context);
    }

    public LPersionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_person;
    }

    @Override
    protected void initView() {
        super.initView();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onEvent(new UEventRefreshLoginState().setLogin(AppContext.self().getLocalUser() != null));
    }

    @BindView(R.id.iv_user_pic)
    ImageView iv_user_pic;

    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UEventRefreshLoginState event) {
        if (event.isLogin()) {
            ImageManage.self().loadImage(AppContext.self().getLocalUser().getUserPic(), iv_user_pic);
            tv_nickname.setText(AppContext.self().getLocalUser().getNickname());
        } else {
            iv_user_pic.setImageResource(R.drawable.theme_music_dcover);
            tv_nickname.setText("点击登录");
        }
    }
}
