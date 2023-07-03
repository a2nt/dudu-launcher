package com.wow.carlauncher.view.base;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.TaskExecutor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Method;

import butterknife.ButterKnife;

/**
 * Created by 10124 on 2017/10/26.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected BaseActivity mContext;
    private int conRes = 0;//内容资源文件
    private RelativeLayout content;
    private boolean loadViewed = false;//是否已经加载视图
    protected ProgressDialog progressDialog;
    protected ProgressInterruptListener progressInterruptListener;
    private View toolbar;
    private TextView title;
    private View base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        init();
        boolean have = false;
        Method[] methods = getClass().getMethods();
        for (Method m : methods) {
            Subscribe meta = m.getAnnotation(Subscribe.class);
            if (meta != null) {
                have = true;
                break;
            }
        }
        if (have) {
            EventBus.getDefault().register(this);
        }
        //处理状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        super.setContentView(R.layout.activity_base);

        base = findViewById(R.id.base);
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.tv_title);
        content = findViewById(R.id.b_content);
        //设置actionbar
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());


        View view = LayoutInflater.from(this).inflate(conRes, null);
        content.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        progressDialog = new ProgressDialog(this) {
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                if (progressInterruptListener != null) {
                    progressInterruptListener.onProgressInterruptListener(progressDialog);
                }
            }
        };
        progressDialog.setCanceledOnTouchOutside(false);

        ButterKnife.bind(this);
        loadViewed = true;
        initView();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    protected void setContent(@LayoutRes int resId) {
        if (loadViewed) {
            throw new RuntimeException("内容页必须在init里面设置");
        }
        conRes = resId;
    }

    protected void setBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            base.setBackground(drawable);
        } else {
            base.setBackgroundDrawable(drawable);
        }
    }

    protected void hideTitle() {
        toolbar.setVisibility(View.GONE);
    }

    protected void showTitle() {
        toolbar.setVisibility(View.VISIBLE);
    }

    public void setTitle(String title) {
        if (title == null) {
            return;
        }
        this.title.setText(title);
    }

    private boolean showLoading = false;

    public void showLoading(final String msg) {
        TaskExecutor.self().autoPost(() -> {
            if (showLoading) {
                return;
            }
            if (progressDialog != null && !isFinishing() && !showLoading) {
                progressDialog.setMessage(msg);
                progressDialog.show();
                showLoading = true;
            }
        });
    }

    public synchronized void hideLoading() {
        TaskExecutor.self().post(() -> {
            if (!showLoading) {
                return;
            }
            if (progressDialog != null && showLoading) {
                progressDialog.hide();
                showLoading = false;
            }
        }, 100);
    }

    public void init() {

    }

    public void initView() {

    }

    public void loadData() {

    }

    public interface ProgressInterruptListener {
        void onProgressInterruptListener(ProgressDialog progressDialog);
    }
}
