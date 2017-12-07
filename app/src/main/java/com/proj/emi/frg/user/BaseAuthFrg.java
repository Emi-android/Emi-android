package com.proj.emi.frg.user;

import android.os.Bundle;

import com.proj.emi.R;
import com.proj.emi.biz.EventCenter;
import com.proj.emi.frg.TitleFragment;
import com.hcb.util.StringUtil;
import com.hcb.util.ToastUtil;

import java.util.regex.Pattern;

public abstract class BaseAuthFrg extends TitleFragment
        implements EventCenter.EventListener {

    protected EventCenter eventCenter;

    protected String captcha;
    protected String phone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventCenter = beans.getEventCenter();
        eventCenter.registEvent(this, EventCenter.EventType.EVT_LOGIN);
    }

    protected boolean checkPhone(String tempPhone) {
        phone = null;
        if (StringUtil.isEmpty(tempPhone)) {
            ToastUtil.show(getString(R.string.phonenum_cant_empty));
        } else {
            if (!isLikePhone(tempPhone)) {
                ToastUtil.show(getString(R.string.phonenum_invalid));
            } else {
                phone = tempPhone;
            }
        }
        return null != phone;
    }

    protected boolean checkCaptcha(String tempCaptcha) {
        captcha = null;
        if (StringUtil.isEmpty(tempCaptcha)) {
            ToastUtil.show(getString(R.string.captcha_cant_empty));
        } else {
            tempCaptcha = tempCaptcha.trim();
            captcha = tempCaptcha;
        }
        return null != captcha;
    }

    @Override
    public void onEvent(EventCenter.HcbEvent e) {
        if (EventCenter.EventType.EVT_LOGIN == e.type) {
            hideKeyboard();
            act.finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventCenter.unregistEvent(this, EventCenter.EventType.EVT_LOGIN);
    }

    private boolean isLikePhone(String str) {
        return Pattern.compile("^1[0-9]{10}$").matcher(str).matches();
    }
}
