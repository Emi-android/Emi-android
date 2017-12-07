package com.proj.emi.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.proj.emi.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DatePickerDlg extends BaseDialog {

    public DatePickerDlg() {
        this.setStyle(STYLE_NO_FRAME, R.style.DatePickerDlg);
    }

    public interface DatePickListener {
        void onPicked(String date);
    }

    public DatePickerDlg setInitDate(Date initDate) {
        this.initDate = initDate;
        return this;
    }

    public DatePickerDlg setMinDate(Date minDate) {
        this.minDate = minDate;
        return this;
    }

    public DatePickerDlg setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    public DatePickerDlg lockMinDate() {
        this.lockMin = true;
        return this;
    }

    public DatePickerDlg showHourMinute() {
        showTime = true;
        return this;
    }

    public DatePickerDlg setListener(DatePickListener l) {
        this.dateListener = l;
        return this;
    }

    private boolean showTime = false;
    private boolean lockMin = false;
    private Date initDate;
    private Date minDate;
    private Date maxDate;
    private DatePickListener dateListener;

    @BindView(R.id.date_picker) DatePicker datePicker;
    @BindView(R.id.time_picker) TimePicker timePicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dlgView = inflater.inflate(R.layout.dlg_date_picker, container, false);
        ButterKnife.bind(this, dlgView);
        if (!showTime) {
            timePicker.setVisibility(View.GONE);
        }
        initView();
        return dlgView;
    }

    @Override
    protected void animAppear() {
        animPopup();
    }

    @Override
    protected void animDisAppear() {
        animPopDown();
    }

    public void initView() {
        final Calendar c = Calendar.getInstance();
        if (null != initDate) {
            c.setTime(initDate);
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        resizePikcer(datePicker);
        if (null != minDate) {
            datePicker.setMinDate(minDate.getTime());
        }
        if (null != maxDate) {
            datePicker.setMaxDate(maxDate.getTime());
        }
        if (lockMin) {
            datePicker.setMinDate(c.getTimeInMillis() - 1000);
        }
        datePicker.updateDate(year, month, day);
        if (showTime) {
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            timePicker.setIs24HourView(true);
            resizePikcer(timePicker);
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
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
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        String date = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, day);
        if (showTime) {
            date += String.format(Locale.getDefault(), " %02d:%02d", hour, minute);
        }
        dateListener.onPicked(date);
        dismiss();
    }

    /**
     * 调整FrameLayout大小
     *
     * @param tp
     */
    private void resizePikcer(FrameLayout tp) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);
        }
    }

    /**
     * 得到viewGroup里面的numberpicker组件
     *
     * @param viewGroup
     * @return
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    /*
     * 调整numberpicker大小
     */
    private void resizeNumberPicker(NumberPicker np) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        params.setMargins(10, 0, 10, 0);
        np.setLayoutParams(params);
    }
}
