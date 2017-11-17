package com.xiaolian.amigo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.main.intf.ISplashPresenter;
import com.xiaolian.amigo.ui.main.intf.ISplashView;
import com.xiaolian.amigo.util.Log;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <p>
 * Created by zcd on 10/15/17.
 */

public class SplashActivity extends MainBaseActivity implements ISplashView {
    private static final String TAG = SplashActivity.class.getSimpleName();
    @Inject
    ISplashPresenter<ISplashView> presenter;
    @BindView(R.id.iv_logo)
    ImageView iv_logo;

    // 计时器
    private CountDownTimer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(this);

        presenter.getNoticeAmount();
        timer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                startMainServerNoResponse();
            }
        };
        timer.start();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void startMain() {
        Log.i(TAG, "startMain");
        cancelTimer();
        Log.i(TAG, "clearObserver");
        presenter.clearObservers();
        iv_logo.postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 200);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }



    @Override
    public void startMainServerNoResponse() {
        Log.i(TAG, "startMainServerNoResponse");
        Log.i(TAG, "clearObserver");
        presenter.clearObservers();
        startActivity(new Intent(this, MainActivity.class)
                .putExtra(MainActivity.INTENT_KEY_SERVER_ERROR, true));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void cancelTimer() {
        if (null != timer) {
            timer.cancel();
        }
    }
}
