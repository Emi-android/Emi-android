package com.proj.emi.frg.main;

import android.os.Bundle;
import android.view.View;

import com.proj.emi.R;

public class ShopcartFrg extends CachableFrg {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int rootLayout() {
        return R.layout.main_shopcart;
    }

    @Override
    protected void initView() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
