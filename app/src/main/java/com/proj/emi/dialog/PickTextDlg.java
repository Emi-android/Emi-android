package com.proj.emi.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.proj.emi.R;
import com.hcb.widget.ScrollerNumberPicker;
import com.hcb.widget.ScrollerNumberPicker.OnSelectListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PickTextDlg extends BaseDialog {
    public interface PickerListener {
        void onPicked(int... pos);
    }

    protected List<String> labels[];
    private int indexs[];
    private int colWs[];
    private int columns[];
    private PickerListener listener;

    public PickTextDlg setLabels(List<String>... lists) {
        this.labels = lists;
        return this;
    }

    public String getLabel(int grp, int index) {
        return labels[grp].get(index);
    }

    public PickTextDlg setPositions(int... indexs) {
        this.indexs = indexs;
        return this;
    }

    public PickTextDlg setFreezedColumns(int... columns) {
        this.columns = columns;
        return this;
    }

    public PickTextDlg setColWeights(int[] colWeights) {
        this.colWs = colWeights;
        return this;
    }

    private boolean isFreezed(int index) {
        if (null == columns) {
            return false;
        }
        for (int i = 0; i < columns.length; i++) {
            if (columns[i] == index) {
                return true;
            }
        }
        return false;
    }

    public PickTextDlg setListener(PickerListener listener) {
        this.listener = listener;
        return this;
    }

    @BindView(R.id.scroller_group) LinearLayout pickerGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.dlg_picker, container, false);
        ButterKnife.bind(this, root);
        addPickers(inflater);
        return root;
    }

    @Override
    protected void animAppear() {
        animPopup();
    }

    @Override
    protected void animDisAppear() {
        animPopDown();
    }

    private int colw4Index(int index) {
        if (null != colWs && index < colWs.length) {
            return colWs[index];
        }
        return 0;
    }

    private void addPickers(LayoutInflater inflater) {
        if (null == labels) {
            return;
        }
        if (indexs == null) {
            indexs = new int[labels.length];
        }
        for (int i = 0; i < labels.length; i++) {
            ScrollerNumberPicker picker = (ScrollerNumberPicker)
                    inflater.inflate(R.layout.single_scroll_picker, null);
            pickerGroup.addView(picker);
            if (isFreezed(i)) {
                picker.setEnable(false);
            }
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT
            );
            int w = colw4Index(i);
            if (labels.length == 1) {
                picker.setGravity(Gravity.CENTER);
                lp.weight = 1;
            } else if (i == 0) {
                picker.setGravity(Gravity.RIGHT);
                lp.weight = w > 0 ? w : 3;
            } else if (i == labels.length - 1) {
                picker.setGravity(Gravity.LEFT);
                lp.weight = w > 0 ? w : 3;
            } else {
                picker.setGravity(Gravity.CENTER);
                lp.weight = w > 0 ? w : 1;
            }
            picker.setLayoutParams(lp);

            picker.setData(labels[i]);
            picker.setDefault(indexs[i]);
            picker.setOnSelectListener(new TxtSelectListener(i));

        }
    }

    private class TxtSelectListener implements OnSelectListener {

        int tag;

        TxtSelectListener(int tag) {
            this.tag = tag;
        }

        @Override
        public void endSelect(final int id, String text) {
            indexs[tag] = id;
        }

        @Override
        public void selecting(int id, String text) {

        }
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

    @OnClick(R.id.btn_confirm)
    void confirm(View v) {
        if (v.getId() == R.id.btn_confirm && null != listener) {
            listener.onPicked(indexs);
        }
        dismiss();
    }

}
