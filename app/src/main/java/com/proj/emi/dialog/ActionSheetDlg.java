package com.proj.emi.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.proj.emi.R;
import com.hcb.util.FormatUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActionSheetDlg extends BaseDialog implements View.OnClickListener {

    public interface ActionDelegate {
        void didSelect(int pos);
    }

    private String title;
    private List<String> actions;
    private ActionDelegate delegate;

    public ActionSheetDlg setTitle(String title) {
        this.title = title;
        return this;
    }

    public ActionSheetDlg setActions(List<String> actions) {
        this.actions = actions;
        return this;
    }

    public ActionSheetDlg setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
        return this;
    }

    @BindView(R.id.sheet_title) TextView tvTitle;
    @BindView(R.id.dlg_frame) LinearLayout sheet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dlg_action_sheet, container, false);
        ButterKnife.bind(this, rootView);
        if (null != title) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        initActions();
        return rootView;
    }

    @OnClick({R.id.dlg_whole, R.id.dlg_frame, R.id.btn_cancel})
    void cancel(View v) {
        switch (v.getId()) {
            case R.id.dlg_whole:
            case R.id.btn_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    private int picked = -1;

    private void initActions() {
        if (null == actions) {
            return;
        }
        for (int i = 0; i < actions.size(); i++) {
            TextView tv = makeButton();
            tv.setText(actions.get(i));
            tv.setTag(i);
            tv.setOnClickListener(this);
            sheet.addView(tv, 1 + i);
        }
    }

    private TextView makeButton() {
        final TextView tv = new TextView(sheet.getContext());
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, FormatUtil.pixOfDip(52)
        );
        lp.bottomMargin = 1;
        tv.setLayoutParams(lp);
        tv.setBackgroundColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(getResources().getInteger(R.integer.txt_size_2));
        tv.setTextColor(getResources().getColor(R.color.txt_lvl_1));
        return tv;
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (null == tag) {
            return;
        }
        picked = (int) tag;
        dismiss();
    }

    @Override
    protected void willDismiss() {
        if (picked >= 0 && null != delegate) {
            delegate.didSelect(picked);
        }
    }
}
