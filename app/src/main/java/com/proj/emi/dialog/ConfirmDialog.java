package com.proj.emi.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proj.emi.R;
import com.hcb.util.StringUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmDialog extends BaseDialog {

    public interface SureListener {
        public void onSure();
    }

    public interface CancelListener {
        void onCancel();
    }

    private SureListener sureListener;
    private CancelListener quitListener;
    private CharSequence desc;
    private String sureLabel;

    public ConfirmDialog setDesc(final String desc) {
        if (!StringUtil.isEmpty(desc)) {
            this.desc = desc;
        }
        return this;
    }

    public ConfirmDialog setDesc(final CharSequence desc) {
        this.desc = desc;
        return this;
    }

    public ConfirmDialog setSureLabel(final String sureLabel) {
        this.sureLabel = sureLabel;
        return this;
    }

    public ConfirmDialog setSureListener(final SureListener sure) {
        sureListener = sure;
        return this;
    }

    public ConfirmDialog setCancelListener(final CancelListener cancel) {
        quitListener = cancel;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View content = inflater.inflate(R.layout.dlg_confirm, container, false);
        ButterKnife.bind(this, content);
        if (null != desc) {
            ((TextView) content.findViewById(R.id.dlg_desc)).setText(desc);
        }
        if (null != sureLabel) {
            ((TextView) content.findViewById(R.id.dlgbtn_right)).setText(sureLabel);
        }
        return content;
    }

    @OnClick(R.id.dlgbtn_left)
    public void leftClick(View v) {
        if (null != quitListener) {
            quitListener.onCancel();
        }
        dismiss();
    }

    @OnClick(R.id.dlgbtn_right)
    public void rightClick(View v) {
        if (null != sureListener) {
            sureListener.onSure();
        }
        dismiss();
    }
}
