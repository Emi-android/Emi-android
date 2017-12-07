package com.proj.emi.frg.main;

import com.proj.emi.GlobalBeans;
import com.proj.emi.R;

public class PersonalFrg extends CachableFrg {

    @Override
    protected int rootLayout() {
        return R.layout.main_personal;
    }

    @Override
    protected void initView() {
        beans = GlobalBeans.getSelf();
    }

}
