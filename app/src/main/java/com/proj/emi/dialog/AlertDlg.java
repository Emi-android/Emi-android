package com.proj.emi.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proj.emi.R;
import com.hcb.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlertDlg extends BaseDialog {

    public interface SureListener {
        void onSure();
    }

    private SureListener listener;
    private String title;
    private CharSequence desc;

    public AlertDlg setDesc(final String desc) {
        if (!StringUtil.isEmpty(desc)) {
            this.desc = desc;
        }
        return this;
    }

    public AlertDlg setTitle(final String title) {
        this.title = title;
        return this;
    }

    public AlertDlg setDesc(final CharSequence desc) {
        this.desc = desc;
        return this;
    }

    public AlertDlg setSureListener(final SureListener sure) {
        listener = sure;
        return this;
    }

    @BindView(R.id.dlg_title) TextView tvTitle;
    @BindView(R.id.dlg_desc) TextView tvDesc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View content = inflater.inflate(R.layout.dlg_alert, container, false);
        ButterKnife.bind(this, content);
        tvTitle.setText("" + title);
        tvDesc.setText("" + desc);

        return content;
    }

    @OnClick(R.id.dlgbtn_confirm)
    public void onClick(View v) {
        if (null != listener) {
            listener.onSure();
        }
        dismiss();
    }

}
