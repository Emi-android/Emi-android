package com.proj.emi.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.proj.emi.R;
import com.proj.emi.actlink.ActDecorator;
import com.proj.emi.actlink.NaviLeftListener;
import com.proj.emi.actlink.NaviLeftText;
import com.proj.emi.actlink.NaviRightDecorator;
import com.proj.emi.actlink.NaviRightListener;
import com.proj.emi.actlink.NaviTitleDecorator;
import com.proj.emi.biz.ActivitySwitcher;
import com.proj.emi.frg.TitleFragment;
import com.hcb.util.ReflectUtil;
import com.hcb.util.StringUtil;
import com.hcb.util.TypeSafer;
import com.umeng.analytics.MobclickAgent;

public class NaviActivity extends BaseFragAct {

    public static final String EXT_FRAGMENT = "fragment_name";

    protected TitleFragment fragment;

    protected boolean isDestroyed;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        fragment = createInstance(getFragmentName(getIntent()));
        if (null == fragment) {
            finish();
            return;
        }
        fragment.setArguments(getIntent().getExtras());
        fragment.setActivity(this);
        if (fragment instanceof ActDecorator) {
            ((ActDecorator) fragment).beforeContentView();
        }
        setContentView(R.layout.act_common);
        initNaviBar();
        if (fragment instanceof ActDecorator) {
            ((ActDecorator) fragment).afterContentView();
        }
        addFragment();
    }

    public void setNaviTitle(final String title) {
        TypeSafer.text((TextView) findViewById(R.id.navi_title), title);
    }

    public void setNaviColor(int color) {
        final int rgb = getResources().getColor(color);
        findViewById(R.id.act_navi).setBackgroundColor(rgb);
        setStatuBarColor(rgb);
    }

    @Deprecated
    public void hideNavi() {
        findViewById(R.id.act_navi).setVisibility(View.GONE);
    }

    public boolean isDisable() {
        return isDestroyed || isFinishing();
    }

    private String getFragmentName(final Intent intent) {
        return (null != intent) ? intent.getStringExtra(EXT_FRAGMENT) : null;
    }

    public TitleFragment createInstance(final String fragName) {
        final Object obj = ReflectUtil.createInstance(fragName);
        if (obj instanceof TitleFragment) {
            return (TitleFragment) obj;
        }
        return null;
    }

    public void initNaviBar() {
        initTitle();
        initLeft();
        initRight();
    }

    private View arrowLeft;

    public void initLeft() {
        final OnClickListener cl = new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };

        arrowLeft = findViewById(R.id.navi_left);
        if (!fragment.hideLeftArrow()) {
            arrowLeft.setOnClickListener(cl);
        }
        arrowLeft.setVisibility(View.GONE);

        final TextView tvLeft = (TextView) findViewById(R.id.navi_left_txt);
        if (fragment instanceof NaviLeftText) {
            tvLeft.setVisibility(View.VISIBLE);
            tvLeft.setText(((NaviLeftText) fragment).leftText());
            tvLeft.setOnClickListener(cl);
            arrowLeft.setPadding(
                    arrowLeft.getPaddingLeft(), arrowLeft.getPaddingTop(),
                    0, arrowLeft.getPaddingBottom());
        }
    }

    public void initTitle() {
        final TextView tv = (TextView) findViewById(R.id.navi_title);
        final int title = fragment.getTitleId();
        final String titleName = fragment.getTitleName();
        if (title > 0) {
            tv.setText(title);
        } else if (!StringUtil.isEmpty(titleName)) {
            tv.setText(titleName);
        }
        if (fragment instanceof NaviTitleDecorator) {
            ((NaviTitleDecorator) fragment).decorTitle(tv);
        }
    }

    public void initRight() {
        final TextView btn = (TextView) findViewById(R.id.navi_right_txt);
        if (fragment instanceof NaviRightListener) {
            final NaviRightListener l = (NaviRightListener) fragment;
            btn.setText(l.rightText());
            btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != l) {
                        l.onRightClicked(v);
                    }
                }
            });
        } else if (fragment instanceof NaviRightDecorator) {
            ((NaviRightDecorator) fragment).decorRightBtn(btn);
        } else {
            btn.setVisibility(View.INVISIBLE);
        }
    }

    public void addFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.act_content, fragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        fragment.onActivityResult(arg0, arg1, arg2);
    }

    public void finishSelf() {
        arrowLeft.setVisibility(View.GONE);
        ActivitySwitcher.finish(this);
    }

    public void finishDown() {
        ActivitySwitcher.finishDown(this);
    }

    @Override
    public void onBackPressed() {
        if (fragment instanceof NaviLeftListener
                && ((NaviLeftListener) fragment).onLeftClicked()) {
            return;
        }
        finishSelf();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != fragment) {
            MobclickAgent.onPageStart(simpleName());
        }
        if (!fragment.hideLeftArrow()) {
            arrowLeft.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != fragment) {
            MobclickAgent.onPageEnd(simpleName());
        }
    }

    public String simpleName() {
        return fragment.getClass().getSimpleName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }

}
