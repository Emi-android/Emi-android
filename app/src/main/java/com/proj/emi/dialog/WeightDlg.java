package com.proj.emi.dialog;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class WeightDlg extends PickTextDlg {

    public interface WeightListener {
        void weightPicked(float weight);
    }

    public WeightDlg setWeight(final float weight) {
        fillWeight(weight);
        return this;
    }

    public WeightDlg setListener(final WeightListener listener) {
        setListener(new PickTextDlg.PickerListener() {
            @Override
            public void onPicked(int... picked) {
                int w0 = Integer.parseInt(getLabel(0, picked[0]));
                int w2 = Integer.parseInt(getLabel(2, picked[2]));
                float w = w0 + (w2 / 10.0f);
                listener.weightPicked(w);
            }
        });
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == labels) {
            fillWeight(0);
        }
    }

    private void fillWeight(float curW) {
        if (curW <= 0) {
            curW = 70;
        }
        final List<String> wt[] = new List[4];
        int pos[] = new int[4];
        wt[0] = new ArrayList<>();
        for (int i = 20; i < 200; ++i) {
            if (i == (int) curW) {
                pos[0] = i - 20;
            }
            wt[0].add(String.format("%d", i));
        }
        wt[1] = new ArrayList<String>() {
            {
                add(".");
            }
        };
        pos[1] = 0;
        int wtPoint = (int) ((curW - (int) curW) * 10);
        wt[2] = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i == wtPoint) {
                pos[2] = i;
            }
            wt[2].add(String.format("%d", i));
        }
        wt[3] = new ArrayList<String>() {
            {
                add("kg");
            }
        };
        pos[3] = 0;
        setLabels(wt).setPositions(pos);
        setFreezedColumns(1, 3);
    }

}
