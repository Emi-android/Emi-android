package com.proj.emi.dialog;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class HeightDlg extends PickTextDlg {

    public interface HeightListener {
        void heightPicked(int height);
    }

    public HeightDlg setHeight(final int height) {
        fillHeight(height);
        return this;
    }

    public HeightDlg setListener(final HeightListener listener) {
        setListener(new PickerListener() {
            @Override
            public void onPicked(int... picked) {
                int h = Integer.parseInt(getLabel(0, picked[0]));
                listener.heightPicked(h);
            }
        });
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == labels) {
            fillHeight(0);
        }
    }

    private void fillHeight(int curH) {
        if (curH <= 0) {
            curH = 170;
        }
        final List<String> ht[] = new List[2];
        int pos[] = new int[2];
        ht[0] = new ArrayList<>();
        for (int i = 100; i < 250; ++i) {
            if (i == curH) {
                pos[0] = i - 100;
            }
            ht[0].add(String.format("%d", i));
        }
        ht[1] = new ArrayList<String>() {
            {
                add("cm");
            }
        };
        pos[1] = 0;
        setLabels(ht).setPositions(pos);
        setFreezedColumns(1);
    }

}
