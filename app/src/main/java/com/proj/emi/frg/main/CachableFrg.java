package com.proj.emi.frg.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proj.emi.GlobalBeans;
import com.proj.emi.biz.CurrentUser;

import butterknife.ButterKnife;

public abstract class CachableFrg extends Fragment {

    protected GlobalBeans beans;
    protected CurrentUser curUser;
    protected View rootView;

    protected abstract int rootLayout();

    protected abstract void initView();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beans = GlobalBeans.getSelf();
        curUser = beans.getCurUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(rootLayout(), container, false);
            ButterKnife.bind(this, rootView);
            initView();
        } else {
            //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    ProgressDialog dialog;

    protected void showProgressDialog(final String title, final String msg) {
        if (null == dialog) {
            dialog = ProgressDialog.show(getActivity(), title, msg);
        } else {
            dialog.setTitle(title);
            dialog.setMessage(msg);
            dialog.show();
        }
    }

    protected void dismissDialog() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
        }
    }

}
