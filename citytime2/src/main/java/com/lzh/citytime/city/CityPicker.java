package com.lzh.citytime.city;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;


import com.lzh.citytime.R;
import com.lzh.citytime.widget.ScrollerNumberPicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dyx on 2016/2/24.
 */
public class CityPicker extends LinearLayout {

    private ScrollerNumberPicker provincePicker;
    private ScrollerNumberPicker cityPicker;

    private OnSelectingListener onSelectingListener;

    private static final int REFRESH_VIEW = 0x001;

    private int tempProvinceIndex = -1;
    private int temCityIndex = -1;
    private List<CityInfo> province_list;
    private HashMap<String, ArrayList<CityInfo>> city_map;

    private CitycodeUtil citycodeUtil;
    private String city_code_string;
    private String city_string;
    private int city_id;
    private int province_id;

    private String initProv, initCity;

    public CityPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CityPicker(Context context) {
        super(context);
    }

    public CityPicker setProvinces(final List<CityInfo> provinces) {
        province_list = provinces;
        return this;
    }

    public CityPicker setCitys(final HashMap<String, ArrayList<CityInfo>> citys) {
        city_map = citys;
        return this;
    }

    public CityPicker setDefault(final String provStr, final String cityStr) {
        initProv = provStr;
        initCity = cityStr;
        return this;
    }

    public void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
        citycodeUtil = CitycodeUtil.getSingleton();
        // 获取控件引用
        provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);
        final List<String> provList = citycodeUtil.getProvince(province_list);
        int defProv = indexOf(provList, initProv);
        if (defProv < 0) {
            defProv = 1;
        }
        provincePicker.setData(provList);
        provincePicker.setDefault(defProv);

        cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
        final List<String> cityList = citycodeUtil.getCity(city_map, citycodeUtil
                .getProvince_list_code().get(defProv));
        int defCity = indexOf(cityList, initCity);
        if (defCity < 0) {
            defCity = 0;
        }
        cityPicker.setData(cityList);
        cityPicker.setDefault(defCity);

        // counyPicker.setData(citycodeUtil.getCouny(couny_map, citycodeUtil
        // .getCity_list_code().get(1)));
        // counyPicker.setDefault(1);
        provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;
                if (tempProvinceIndex != id) {

                    String selectDay = cityPicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    // String selectMonth = counyPicker.getSelectedText();
                    // if (selectMonth == null || selectMonth.equals(""))
                    // return;
                    // 城市数组
                    cityPicker.setData(citycodeUtil.getCity(city_map,
                            citycodeUtil.getProvince_list_code().get(id)));
                    cityPicker.setDefault(0);
                    // counyPicker.setData(citycodeUtil.getCouny(couny_map,
                    // citycodeUtil.getCity_list_code().get(1)));
                    // counyPicker.setDefault(1);
                    int lastDay = Integer.valueOf(provincePicker.getListSize());
                    if (id > lastDay) {
                        provincePicker.setDefault(lastDay - 1);
                    }
                }
                tempProvinceIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
            }
        });
        cityPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {

                if (text.equals("") || text == null)
                    return;
                if (temCityIndex != id) {

                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    // String selectMonth = counyPicker.getSelectedText();
                    //
                    // if (selectMonth == null || selectMonth.equals(""))
                    // return;
                    // counyPicker.setData(citycodeUtil.getCouny(couny_map,
                    // citycodeUtil.getCity_list_code().get(id)));
                    // counyPicker.setDefault(1);
                    int lastDay = Integer.valueOf(cityPicker.getListSize());
                    if (id > lastDay) {
                        cityPicker.setDefault(lastDay - 1);
                    }
                }
                temCityIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
        // counyPicker.setOnSelectListener(new OnSelectListener() {
        //
        // @Override
        // public void endSelect(int id, String text) {
        //
        // if (text.equals("") || text == null)
        // return;
        // if (tempCounyIndex != id) {
        //
        // // Toast.makeText(context.getApplicationContext(),
        // // country, Toast.LENGTH_SHORT).show();
        // String selectDay = provincePicker.getSelectedText();
        //
        // // Toast.makeText(context.getApplicationContext(),
        // // selectDay, Toast.LENGTH_SHORT).show();
        // if (selectDay == null || selectDay.equals(""))
        // return;
        // String selectMonth = cityPicker.getSelectedText();
        //
        // // Toast.makeText(context.getApplicationContext(),
        // // selectMonth, Toast.LENGTH_SHORT).show();
        // if (selectMonth == null || selectMonth.equals(""))
        // return;
        // // 城市数组
        // city_code_string = citycodeUtil.getCouny_list_code()
        // .get(id);
        // int lastDay = Integer.valueOf(counyPicker.getListSize());
        //
        // if (id > lastDay) {
        // counyPicker.setDefault(lastDay - 1);
        //
        // }
        // }
        // tempCounyIndex = id;
        // Message message = new Message();
        // message.what = REFRESH_VIEW;
        // handler.sendMessage(message);
        // }
        //
        // @Override
        // public void selecting(int id, String text) {
        //
        // }
        // });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    if (onSelectingListener != null)
                        onSelectingListener.selected(true);

                    break;
                default:
                    break;
            }
        }
    };

    public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
        this.onSelectingListener = onSelectingListener;
    }

    public String getCity_code_string() {
        city_code_string = citycodeUtil.getCouny_list_code().get(temCityIndex);
        return city_code_string;
    }

    public String getCity_string() {
        city_string = cityPicker.getSelectedText();

        // + counyPicker.getSelectedText();
        return city_string;
    }


    public int getCity_id() {
//		city_id = provincePicker.getSelected();
        city_id = cityPicker.getSelected();

        return city_id;
    }
    public int getProvince_id() {
        province_id = provincePicker.getSelected();
//		city_id = cityPicker.getSelected();

        return province_id;
    }

    public String getProvince_string() {

        return provincePicker.getSelectedText();
    }

    // public String getCountry() {
    // return country;
    // }

    public interface OnSelectingListener {

        public void selected(boolean selected);
    }

    public static <E> int indexOf(final List<E> list, final E o) {
        if (!isEmpty(list) && null != o) {
            for (int i = 0; i < list.size(); i++) {
                if (o.equals(list.get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static boolean isEmpty(final List<? extends Object> list) {
        return null == list || list.size() == 0;
    }
}
