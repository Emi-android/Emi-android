package com.proj.emi.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.proj.emi.AppConsts;
import com.proj.emi.GlobalBeans;
import com.proj.emi.R;
import com.proj.emi.frg.main.HomePageFrg;
import com.proj.emi.frg.main.OrderFrg;
import com.proj.emi.frg.main.PersonalFrg;
import com.proj.emi.frg.main.ShopcartFrg;
import com.hcb.util.ToastUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseFragAct
        implements TabHost.OnTabChangeListener {

    private static final Logger LOG = LoggerFactory.getLogger(MainActivity.class);

    private static final int IDX_HOME = 0;
    private static final int IDX_EVENT = 1;
    private static final int IDX_NULL = 2;
    private static final int IDX_MESSAGE = 3;
    private static final int IDX_PERSONAL = 4;

    @BindView(R.id.tabhost) FragmentTabHost mTabHost;
    @BindView(R.id.main_title) TextView tvTitle;
    @BindView(R.id.img_top_left) ImageView imgLeft;
    @BindView(R.id.img_top_right) ImageView imgRight;

    @BindView(R.id.updateLayout)
    RelativeLayout updateLayout;

    private int curTab = 0;
    private GlobalBeans beans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (GlobalBeans.getSelf() == null) {
            GlobalBeans.initForMainUI(getApplication());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        ButterKnife.bind(this);
        initTabHost();

        setTabSelected(curTab, true);
        beans = GlobalBeans.getSelf();
        updateLayout.setVisibility(View.GONE);
//        curUser = beans.getCurUser();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final int index = intent.getIntExtra(AppConsts.EX_INDEX, IDX_HOME);
        if (null != mTabHost) {
            mTabHost.setCurrentTab(index);
        } else {
            curTab = index;
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        final int tabIdx = mTabHost.getCurrentTab();
        if (tabIdx == curTab) {
            return;
        }
        setTabSelected(curTab, false);
        curTab = tabIdx;
        setTabSelected(curTab, true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final Fragment frg = getSupportFragmentManager().findFragmentByTag(tabs.get(curTab).tag);
        if (null != frg) {
            frg.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initTabHost() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for (int i = 0; i < tabs.size(); i++) {
            final TabDesc td = tabs.get(i);
            final View vTab = makeTabView();
            if (i == 2){
                vTab.setVisibility(View.INVISIBLE);
            }else {
                vTab.setVisibility(View.VISIBLE);
            }
            ((TextView) vTab.findViewById(R.id.tab_label)).setText(td.name);
            refreshTab(vTab, td, false);

            mTabHost.addTab(mTabHost.newTabSpec(td.tag).setIndicator(vTab), td.frgClass, null);
        }
        mTabHost.setBackgroundResource(R.color.white_lvl_1);
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setOnTabChangedListener(this);
    }

    private View makeTabView() {
        return this.getLayoutInflater().inflate(
                R.layout.cell_maintab, mTabHost.getTabWidget(), false);
    }

    private void setTabSelected(int tabIdx, boolean selected) {
        refreshTab(mTabHost.getTabWidget().getChildAt(tabIdx),
                tabs.get(tabIdx), selected);
        if (selected) {
            tvTitle.setText(tabs.get(curTab).name);
            changeTop();
        }
    }

    private void refreshTab(View vTab, TabDesc td, boolean selected) {
        final ImageView iv = (ImageView) vTab.findViewById(R.id.tab_image);
        iv.setImageResource(selected ? td.icSelect : td.icNormal);
    }

    private void changeTop() {
        switch (curTab) {
            case IDX_PERSONAL:
                imgRight.setVisibility(View.VISIBLE);
                imgRight.setImageResource(R.mipmap.personal_notice);
                break;
            default:
                imgLeft.setVisibility(View.GONE);
                imgRight.setVisibility(View.GONE);
                break;
        }
    }

    private final static List<TabDesc> tabs = new ArrayList<TabDesc>() {
        {
            add(TabDesc.make("homepage", R.string.homepage,
                    R.mipmap.tab_home_n, R.mipmap.tab_home_h, HomePageFrg.class));
            add(TabDesc.make("event", R.string.event,
                    R.mipmap.tab_order_n, R.mipmap.tab_order_h, OrderFrg.class));
            add(TabDesc.make("empty", R.string.empty,
                    R.mipmap.tab_order_n, R.mipmap.tab_order_h, OrderFrg.class));
            add(TabDesc.make("message", R.string.message,
                    R.mipmap.tab_cart_n, R.mipmap.tab_cart_h, ShopcartFrg.class));
            add(TabDesc.make("personal", R.string.personal,
                    R.mipmap.tab_personal_n, R.mipmap.tab_personal_h, PersonalFrg.class));
        }
    };

    private static class TabDesc {
        String tag;
        int name;
        int icNormal;
        int icSelect;
        Class<? extends Fragment> frgClass;

        static TabDesc make(String tag, int name, int icNormal, int icSelect,
                            Class<? extends Fragment> frgClass) {
            TabDesc td = new TabDesc();
            td.tag = tag;
            td.name = name;
            td.icNormal = icNormal;
            td.icSelect = icSelect;
            td.frgClass = frgClass;
            return td;
        }

    }

    @OnClick(R.id.img_top_right)
    void topRightClick(View view) {
        switch (curTab) {
            case IDX_PERSONAL://
                break;
            default:
                break;
        }
    }

    /**
     * 发布按钮点击事件
     */
    @OnClick(R.id.updateImg)
    void showUpdateLayout(){
        updateLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 关闭按钮点击事件
     */
    @OnClick(R.id.closeImg)
    void close(){
        updateLayout.setVisibility(View.GONE);
    }

    /**
     * 点击签到
     */
    @OnClick(R.id.signInLayout)
    void signIn(){

        updateLayout.setVisibility(View.GONE);
    }

    /**
     * 点击活动
     */
    @OnClick(R.id.eventLayout)
    void event(){

        updateLayout.setVisibility(View.GONE);
    }

    /**
     * 点击约会
     */
    @OnClick(R.id.engagementLayout)
    void engagement(){

        updateLayout.setVisibility(View.GONE);
    }

    private long backPressTime = 0;
    private static final int SECOND = 1000;

    @Override
    public void onBackPressed() {
        final long uptimeMillis = SystemClock.uptimeMillis();
        if (uptimeMillis - backPressTime > 2 * SECOND) {
            backPressTime = uptimeMillis;
            ToastUtil.show(getString(R.string.press_again_to_leave));
        } else {
            ToastUtil.cancel();
            onExit();
        }
    }

    private void onExit() {
        finish();
        if (null != beans) {
            beans.onTerminate();
        }
    }

}
