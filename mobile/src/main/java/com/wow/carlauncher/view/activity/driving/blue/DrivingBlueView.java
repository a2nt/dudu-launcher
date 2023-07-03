package com.wow.carlauncher.view.activity.driving.blue;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.BitmapBase64Util;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.DateUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.ex.manage.speed.SMEventSendSpeed;
import com.wow.carlauncher.ex.manage.time.event.TMEventSecond;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventNavInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventState;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventAction;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventCoverRefresh;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventState;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarInfo;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.activity.driving.DrivingBaseView;
import com.wow.carlauncher.view.base.BaseView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wow.carlauncher.common.CommonData.MINUTE_MILL;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.ICONS;
import static com.wow.carlauncher.ex.plugin.fk.FangkongProtocolEnum.YLFK;
import static com.wow.carlauncher.ex.plugin.fk.protocol.YiLianProtocol.RIGHT_BOTTOM_CLICK;

public class DrivingBlueView extends DrivingBaseView {
    private final static int MAX_REV = 8000;
    private final static int MAX_SPEED = 200;
    private static boolean fristLoad = true;


    public DrivingBlueView(@NonNull Context context) {
        super(context);
    }

    public DrivingBlueView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_driving_blue;
    }

    @BindView(R.id.iv_center)
    ImageView iv_center;

    @BindView(R.id.iv_vss_cursor)
    ImageView iv_vss_cursor;

    @BindView(R.id.iv_fuel_cursor)
    ImageView iv_fuel_cursor;

    @BindView(R.id.iv_fuel_mask)
    ImageView iv_fuel_mask;

    @BindView(R.id.iv_rpm_mask)
    ImageView iv_rpm_mask;

    @BindView(R.id.iv_rpm_cursor)
    ImageView iv_rpm_cursor;

    @BindView(R.id.tv_speed)
    TextView tv_speed;

    @BindView(R.id.ll_right)
    LinearLayout ll_right;


    @BindView(R.id.tv_tp_lf)
    TextView tv_tp_lf;

    @BindView(R.id.tv_tp_lb)
    TextView tv_tp_lb;

    @BindView(R.id.tv_tp_rf)
    TextView tv_tp_rf;

    @BindView(R.id.tv_tp_rb)
    TextView tv_tp_rb;

    @BindView(R.id.music_iv_play)
    ImageView music_iv_play;

    @BindView(R.id.tv_music_title)
    TextView tv_music_title;

    @BindView(R.id.tv_shui_temp)
    TextView tv_shui_temp;

    @BindView(R.id.tv_shengyu_oil)
    TextView tv_shengyu_oil;

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.rl_item1_not_nav_tp)
    View rl_item1_not_nav_tp;

    @BindView(R.id.rl_item1_not_nav_music)
    View rl_item1_not_nav_music;

    @BindView(R.id.music_iv_cover)
    ImageView music_iv_cover;

    @BindView(R.id.ll_obd_not_connect)
    View ll_obd_not_connect;

    @BindView(R.id.ll_obd_connect)
    View ll_obd_connect;

    @BindView(R.id.fl_item1_nav)
    View fl_item1_nav;

    @BindView(R.id.fl_item1_not_nav)
    View fl_item1_not_nav;

    @BindView(R.id.iv_nav_icon)
    ImageView iv_nav_icon;

    @BindView(R.id.tv_nav_dis)
    TextView tv_nav_dis;

    @BindView(R.id.tv_nav_msg)
    TextView tv_nav_msg;

    @BindView(R.id.rl_main_info)
    View rl_main_info;

    @BindView(R.id.btn_back)
    View btn_back;

    private boolean show = true;

    private boolean obdConnect = false;

    private boolean showNav = true;
    private boolean naving = false;
    private ScheduledFuture<?> scheduledFuture;

    @Override
    protected void initView() {
        super.initView();
        int max = 49;
        int start = 1;
        if (SharedPreUtil.getBoolean(CommonData.SDATA_DRIVING_VIEW_ABUNATION, true) && fristLoad) {
            scheduledFuture = TaskExecutor.self().run(() -> {
                for (int i = start; i <= max; i++) {
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int index = i;
                    TaskExecutor.self().autoPost(() -> {
                        iv_center.setImageResource(getContext().getResources().getIdentifier("driving_blue_center_gif_" + index, "mipmap", getContext().getPackageName()));
                        if (index == max) {
                            loadOk();
                        }
                    });
                }
            }, 1000);
        } else {
            iv_center.setImageResource(R.mipmap.driving_blue_center_gif_49);
            loadOk();
        }
        fristLoad = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }

    @OnClick(value = {R.id.music_ll_prew, R.id.music_ll_next, R.id.music_ll_play, R.id.fl_item1, R.id.btn_back})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.music_ll_prew: {
                MusicPlugin.self().pre();
                break;
            }
            case R.id.music_ll_play: {
                MusicPlugin.self().playOrPause();
                break;
            }
            case R.id.music_ll_next: {
                MusicPlugin.self().next();
                break;
            }
            case R.id.fl_item1: {
                showNav = !showNav;
                refreshNavState();
                break;
            }
            case R.id.btn_back: {
                getActivity().moveTaskToBack(true);
                break;
            }
        }
    }

    private void loadOk() {
        iv_vss_cursor.setVisibility(VISIBLE);
        iv_fuel_cursor.setVisibility(VISIBLE);
        iv_fuel_mask.setVisibility(VISIBLE);
        iv_rpm_mask.setVisibility(VISIBLE);
        iv_rpm_cursor.setVisibility(VISIBLE);
        rl_main_info.setVisibility(VISIBLE);

        iv_rpm_mask.setRotation((((float) 0 / (float) MAX_REV) * 87 - 65));
        iv_fuel_mask.setRotation((((float) 0 / (float) MAX_REV) * 87 + 65));
        ll_right.setVisibility(VISIBLE);

        //同步一下信息
        TaskExecutor.self().post(() -> {
            onEvent(new PObdEventConnect().setConnected(ObdPlugin.self().isConnect()));
            onEvent(ObdPlugin.self().getCurrentPObdEventCarInfo());
            onEvent(ObdPlugin.self().getCurrentPObdEventCarTp());
            MusicPlugin.self().requestLast();
        }, 500);
    }

    private void refreshNavState() {
        if (naving && showNav) {
            fl_item1_nav.setVisibility(View.VISIBLE);
            fl_item1_not_nav.setVisibility(View.GONE);
        } else {
            fl_item1_nav.setVisibility(View.GONE);
            fl_item1_not_nav.setVisibility(View.VISIBLE);
        }
    }

    private long cur_min = 0L;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final TMEventSecond event) {
        long time1 = System.currentTimeMillis() / MINUTE_MILL;
        if (time1 != cur_min) {
            cur_min = time1;
            Date d = new Date();
            String date = DateUtil.dateToString(d, "MM月 dd日 ");
            String time = DateUtil.dateToString(d, "HH:mm");
            if (time.startsWith("0")) {
                time = time.substring(1);
            }
            if (date.startsWith("0")) {
                date = date.substring(1);
            }
            date = date + time;
            this.tv_date.setText(date);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventInfo event) {
        if (tv_music_title != null) {
            if (CommonUtil.isNotNull(event.getTitle())) {
                String msg = event.getTitle();
                if (CommonUtil.isNotNull(event.getArtist())) {
                    msg = msg + "-" + event.getArtist();
                }
                tv_music_title.setText(msg);
            } else {
                tv_music_title.setText("音乐名称");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventState event) {
        if (music_iv_play != null) {
            if (event.isRun()) {
                music_iv_play.setImageResource(R.mipmap.ic_pause2_b);
            } else {
                music_iv_play.setImageResource(R.mipmap.ic_play2_b);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventConnect event) {
        boolean showTp = false;
        obdConnect = event.isConnected();
        if (event.isConnected()) {
            if (ObdPlugin.self().supportTp()) {
                rl_item1_not_nav_tp.setVisibility(VISIBLE);
                rl_item1_not_nav_music.setVisibility(GONE);
                showTp = true;
            }
            ll_obd_connect.setVisibility(VISIBLE);
            ll_obd_not_connect.setVisibility(GONE);
        } else {
            ll_obd_connect.setVisibility(GONE);
            ll_obd_not_connect.setVisibility(VISIBLE);
        }
        if (!showTp) {
            rl_item1_not_nav_tp.setVisibility(GONE);
            rl_item1_not_nav_music.setVisibility(VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SMEventSendSpeed event) {
        if (!obdConnect) {
            setSpeed(event.getSpeed());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventCarInfo event) {
        if (event.getRev() != null) {
            setRev(event.getRev());
        }
        if (event.getSpeed() != null) {
            setSpeed(event.getSpeed());
        }
        if (event.getWaterTemp() != null) {
            String msg = event.getWaterTemp() + "°";
            tv_shui_temp.setText(msg);
        }

        if (event.getOilConsumption() != null) {
            String msg = event.getOilConsumption() + "%";
            tv_shengyu_oil.setText(msg);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PAmapEventState event) {
        naving = event.isRunning();
        refreshNavState();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PAmapEventNavInfo event) {
        if (event.getIcon() - 1 >= 0 && event.getIcon() - 1 < ICONS.length) {
            iv_nav_icon.setImageResource(ICONS[event.getIcon() - 1]);
        }
        if (event.getSegRemainDis() > -1) {
            String msg = "";
            if (event.getSegRemainDis() < 10) {
                msg = "现在";
            } else {
                if (event.getSegRemainDis() > 1000) {
                    msg = event.getSegRemainDis() / 1000 + "公里后";
                } else {
                    msg = event.getSegRemainDis() + "米后";
                }
            }
            msg = msg + event.getNextRoadName();
            tv_nav_dis.setText(msg);
        }

        if (event.getRouteRemainTime() > -1 && event.getRouteRemainDis() > -1) {
            if (event.getRouteRemainTime() == 0 || event.getRouteRemainDis() == 0) {
                tv_nav_msg.setText("到达");
            } else {
                String msg = "剩余" + new BigDecimal(event.getRouteRemainDis() / 1000f).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue() + "公里  " +
                        event.getRouteRemainTime() / 60 + "分钟";
                tv_nav_msg.setText(msg);
            }
        }
    }

    @Subscribe(priority = 90)
    public void onEvent(PFkEventAction event) {
        if (YLFK.equals(event.getFangkongProtocol()) && showing()) {
            boolean needCancelEvent = false;
            switch (event.getAction()) {
                case RIGHT_BOTTOM_CLICK:
                    if (naving) {
                        showNav = !showNav;
                        TaskExecutor.self().autoPost(this::refreshNavState);
                    } else {
                        ToastManage.self().show("没有开启导航!");
                    }
                    needCancelEvent = true;
                    break;
            }
            if (needCancelEvent) {
                EventBus.getDefault().cancelEventDelivery(event);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventCoverRefresh event) {
        if (music_iv_cover != null) {
            music_iv_cover.setImageResource(0);
            switch (event.getCoverType()) {
                case PMusicEventCoverRefresh.TYPE_URL: {
                    ImageManage.self().loadImage(event.getUrl(), music_iv_cover, R.drawable.theme_music_dcover);
                    break;
                }
                case PMusicEventCoverRefresh.TYPE_BASE64: {
                    try {
                        music_iv_cover.setImageBitmap(BitmapBase64Util.base64ToBitmap(event.getCoverBas64()));
                    } catch (Exception ignored) {
                        music_iv_cover.setImageResource(R.drawable.theme_music_dcover);
                    }
                    break;
                }
                default:
                    music_iv_cover.setImageResource(R.drawable.theme_music_dcover);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventCarTp event) {
        if (tv_tp_lf != null && event.getlFTirePressure() != null) {
            tv_tp_lf.setText(getContext().getString(R.string.driving_cool_black_tp, event.getlFTirePressure(), event.getlFTemp()));
        }

        if (tv_tp_lb != null && event.getlBTirePressure() != null) {
            tv_tp_lb.setText(getContext().getString(R.string.driving_cool_black_tp, event.getlBTirePressure(), event.getlBTemp()));
        }

        if (tv_tp_rf != null && event.getrFTirePressure() != null) {
            tv_tp_rf.setText(getContext().getString(R.string.driving_cool_black_tp, event.getrFTirePressure(), event.getrFTemp()));
        }

        if (tv_tp_rb != null && event.getrBTirePressure() != null) {
            tv_tp_rb.setText(getContext().getString(R.string.driving_cool_black_tp, event.getrBTirePressure(), event.getrBTemp()));
        }
    }


    private void setRev(int rev) {
        if (show) {
            //tv_rev.setText(rev + "");
            if (rev > MAX_REV) {
                rev = MAX_REV;
            } else if (rev < 0) {
                rev = 0;
            }
            tagerRev = rev;
            postRev();
        }
    }

    private int currentRev = 0;
    private int tagerRev = 0;

    private void postRev() {
        //转速变化的区间
        int revChange = 20;
        if (revChange + currentRev < tagerRev) {
            currentRev = currentRev + revChange;
            if (currentRev > tagerRev) {
                currentRev = tagerRev;
            }
            postDelayed(() -> {
                iv_rpm_mask.setRotation((((float) currentRev / (float) MAX_REV) * 87 - 65));
                postRev();
            }, 1);
        } else if (revChange + currentRev > tagerRev) {
            currentRev = currentRev - revChange;
            if (currentRev < tagerRev) {
                currentRev = tagerRev;
            }
            postDelayed(() -> {
                iv_rpm_mask.setRotation((((float) currentRev / (float) MAX_REV) * 87 - 65));
                postRev();
            }, 1);
        }
    }

    private void setSpeed(int speed) {
        if (show) {
            tv_speed.setText(String.valueOf(speed));
            if (speed > MAX_SPEED) {
                speed = MAX_SPEED;
            } else if (speed < 0) {
                speed = 0;
            }
            tagertSpeed = speed;
            postSpeed();
        }
    }

    private int currentSpeed = 0;
    private int tagertSpeed = 0;

    private void postSpeed() {
        //转速变化的区间
        int speedChange = 1;
        if (speedChange + currentSpeed < tagertSpeed) {
            currentSpeed = currentSpeed + speedChange;
            if (currentSpeed > tagertSpeed) {
                currentSpeed = tagertSpeed;
            }
            postDelayed(() -> {
                iv_vss_cursor.setRotation((float) (currentSpeed * 180) / (float) MAX_SPEED);
                postSpeed();
            }, 1);
        } else if (speedChange + currentSpeed > tagertSpeed) {
            currentSpeed = currentSpeed - speedChange;
            if (currentSpeed < tagertSpeed) {
                currentSpeed = tagertSpeed;
            }
            postDelayed(() -> {
                iv_vss_cursor.setRotation((float) (currentSpeed * 180) / (float) MAX_SPEED);
                postSpeed();
            }, 1);
        }
    }
}
