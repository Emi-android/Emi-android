package com.proj.emi.frg.user;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.proj.emi.AppConsts;
import com.proj.emi.R;
import com.proj.emi.actlink.NaviLeftListener;
import com.proj.emi.biz.ActivitySwitcher;
import com.proj.emi.loader.CaptchaLoader;
import com.proj.emi.loader.LoginLoader;
import com.proj.emi.loader.base.AbsLoader;
import com.proj.emi.loader.base.BaseGetLoader;
import com.proj.emi.model.base.InBody;
import com.proj.emi.model.login.LoginInBody;
import com.proj.emi.model.login.LoginOutBody;
import com.hcb.util.Md5;
import com.hcb.util.TimeUtil;
import com.hcb.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFrg extends BaseAuthFrg implements NaviLeftListener {

    @BindView(R.id.editor_phone) EditText edtPhone;
    @BindView(R.id.editor_captcha) EditText edtCaptcha;
    @BindView(R.id.login_fetch_captcha) TextView btnFetch;

    private final long COUNT_DOWN_TOTAL = 60000;
    private final long COUNT_DOWN_INTERVAL = 1000;
    private final CountDownTimer countDownTimer = new CountDownTimer(COUNT_DOWN_TOTAL, COUNT_DOWN_INTERVAL) {
        @Override
        public void onTick(long millisUntilFinished) {
            btnFetch.setText(String.format("请等待 %d 秒", millisUntilFinished / COUNT_DOWN_INTERVAL));
            btnFetch.setEnabled(false);
        }

        @Override
        public void onFinish() {
            btnFetch.setEnabled(true);
            btnFetch.setText(R.string.send_captcha);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        act.hideNavi();
        rootView = inflater.inflate(R.layout.frg_login, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        edtPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    fetchCaptcha();
                }
                return false;
            }
        });
        edtCaptcha.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendLoginReq();
                }
                return false;
            }
        });
    }

    @OnClick(R.id.login_fetch_captcha)
    void fetchCaptcha() {
        if (!checkPhone(edtPhone.getText().toString())) {
            return;
        }
        ToastUtil.show(getString(R.string.captcha_sending));
        btnFetch.setEnabled(false);
        new CaptchaLoader().load(phone, new BaseGetLoader.RespReactor() {
            @Override
            public void succeed(InBody body) {
                btnFetch.setEnabled(true);
                ToastUtil.show(getString(R.string.captcha_sended));
                countDownTimer.start();
            }

            @Override
            public void failed(String code, String reason) {
                btnFetch.setEnabled(true);
                ToastUtil.show(reason);
            }
        });
    }

    @OnClick(R.id.btn_login)
    void sendLoginReq() {
        if (null == phone && !checkPhone(edtPhone.getText().toString())) {
            return;
        }
        if (!phone.equals(edtPhone.getText().toString())) {
            ToastUtil.show(getString(R.string.input_phone_changed));
            return;
        }
        if (!checkCaptcha(edtCaptcha.getText().toString())) {
            return;
        }
        pswd = Md5.encode(TimeUtil.getDateString() + phone + AppConsts.FIT_PWD_KEY);
        LoginOutBody outBody = new LoginOutBody()
                .setPhone(phone).setCode(captcha).setPassword(pswd);
        showProgressDialog(getString(R.string.login), getString(R.string.login_ing));
        new LoginLoader().login(outBody, new AbsLoader.RespReactor<LoginInBody>() {

            @Override
            public void succeed(LoginInBody body) {
                dismissDialog();
                onLogin(body);
            }

            @Override
            public void failed(String code, String reason) {
                dismissDialog();
                ToastUtil.show(reason);
            }
        });
    }

    private String pswd;

    protected void onLogin(LoginInBody body) {
        curUser.setPhone(phone);
        curUser.setPassword(pswd);
        curUser.setUid(body.getUuid());

        eventCenter.evtLogin();
    }

    @OnClick(R.id.login_protocol)
    void seeProtocol() {
        ActivitySwitcher.startWebFragment(act,
                getString(R.string.privacy_items),
                AppConsts.SERVICE_AGREEMENTS_URL);
    }

    @Override
    public boolean onLeftClicked() {
        return true;
    }
}
