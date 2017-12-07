package com.lzh.citytime.time;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;


import com.lzh.citytime.R;
import com.lzh.citytime.widget.AnimEndListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

public class DatePickerDlg extends DialogFragment implements View.OnClickListener {

    protected Activity act;
    private RelativeLayout dlg_whole;
    private LinearLayout dlg_frame;
    private TextView btn_cancel;
    private TextView btn_confirm;

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

    public DatePickerDlg hindDate() {
        showDate = false;
        return this;
    }

    public DatePickerDlg setListener(DatePickListener l) {
        this.dateListener = l;
        return this;
    }

    private boolean showTime = false;
    private boolean showDate = true;
    private boolean lockMin = false;
    private Date initDate;
    private Date minDate;
    private Date maxDate;
    private DatePickListener dateListener;
    private DatePicker datePicker;
    private TimePicker timePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dlgView = inflater.inflate(R.layout.dlg_date_picker, container, false);
//        ButterKnife.bind(this, dlgView);
        initView(dlgView);
        setListener();
        if (!showTime) {
            timePicker.setVisibility(View.GONE);
        }
        if(!showDate){
            datePicker.setVisibility(View.GONE);
        }
        return dlgView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animAppear();
    }

    @Override
    public void dismiss() {
        animDisAppear();
    }
    protected void animAppear() {
        animPopup();
    }

    protected void animDisAppear() {
        animPopDown();
    }

    protected void animPopup() {
        animFadeIn();
        if (null != dlg_frame) {
            dlg_frame.startAnimation(AnimationUtils.loadAnimation(act, R.anim.in_from_bottom));
        }
    }

    protected void animPopDown() {
        if (null != dlg_frame) {
            dlg_frame.startAnimation(AnimationUtils.loadAnimation(act, R.anim.out_to_bottom));
        }
        animFadeOut();
    }
    public void initView(View rootView){
        datePicker = (DatePicker) rootView.findViewById(R.id.date_picker);
        timePicker = (TimePicker) rootView.findViewById(R.id.time_picker);
        dlg_whole = (RelativeLayout) rootView.findViewById(R.id.dlg_whole);
        dlg_frame = (LinearLayout) rootView.findViewById(R.id.dlg_frame);
        btn_cancel = (TextView) rootView.findViewById(R.id.btn_cancel);
        btn_confirm = (TextView) rootView.findViewById(R.id.btn_confirm);
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

    private void setListener(){
        dlg_whole.setOnClickListener(this);
        dlg_frame.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.dlg_whole || id == R.id.btn_cancel || id == R.id.dlg_frame){
            dismiss();
        }else if (id == R.id.btn_confirm){
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();
            int hour = timePicker.getCurrentHour();
            int minute = timePicker.getCurrentMinute();
            String date;
            if (showDate && !showTime) {
                date = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, day);
            } else if (showDate && showTime) {
                date = String.format(Locale.getDefault(), "%d-%02d-%02d %02d:%02d", year, month + 1, day, hour, minute);
            } else {
                date = String.format(Locale.getDefault(), " %02d:%02d", hour, minute);
            }
            dateListener.onPicked(date);
            dismiss();
        }
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

    private void animFadeIn() {
        if (null == dlg_whole) {
            return;
        }
        dlg_whole.startAnimation(AnimationUtils.loadAnimation(act, R.anim.alpha_in));
    }

    private void animFadeOut() {
        if (null == dlg_whole) {
            superDismiss();
            return;
        }
        Animation anim = AnimationUtils.loadAnimation(act, R.anim.alpha_out);
        anim.setAnimationListener(new AnimEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                superDismiss();
            }
        });
        dlg_whole.startAnimation(anim);
    }

    private void superDismiss() {
        willDismiss();
        try {
            super.dismiss();
        } catch (Exception e) {
        }
    }

    protected void willDismiss() {
    }
}
