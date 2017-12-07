package com.proj.emi.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proj.emi.R;
import com.proj.emi.adapter.BigPictureAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureShowDlg extends BaseDialog {

    private List<String> imgUris;

    private int index = 0;
    private boolean isAvatar = false;

    public PictureShowDlg setImageUrls(List<String> urls) {
        this.imgUris = urls;
        return this;
    }

    public PictureShowDlg setInitPos(int pos) {
        this.index = pos;
        return this;
    }

    public PictureShowDlg setAvatar() {
        this.isAvatar = true;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dlg_bigpicture, container, false);
        ButterKnife.bind(this, v);
        initViewPager();
        return v;
    }

    @BindView(R.id.dlg_frame) ViewPager pager;

    private void initViewPager() {
        pager.setAdapter(new BigPictureAdapter(imgUris).setAvatar(isAvatar)
                .setClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                }));
        pager.setCurrentItem(index);
    }
}
