package com.lzh.citytime.city;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.lzh.citytime.R;
import com.lzh.citytime.dialog.BaseDialog;
import com.lzh.citytime.util.StringUtil;
import com.lzh.citytime.widget.ScrollerNumberPicker;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/22.
 */
public class CountyPickerDlg extends DialogFragment implements View.OnClickListener{

    protected Activity act;
    private RelativeLayout dlg_whole;
    private LinearLayout dlg_frame;
    private TextView btn_cancel;
    private TextView btn_confirm;
    private ScrollerNumberPicker scrollerNumberPicker;

    public interface CityPickListener {
        void onPick(String county, String code);
    }

    private CityPickListener listener;

    public CountyPickerDlg setPickListener(CityPickListener listener) {
        this.listener = listener;
        return this;
    }

    private County county;
    private String initProv, initCity,countyCode;
    private ArrayList<CountyInfor> provList;
    private HashMap<String, ArrayList<CountyInfor>> provMap;

//    @BindView(R.id.county_picker)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = getActivity();
        county = CityLoader.loadCounty(act);
        provList = new ArrayList<>();
        provMap = new HashMap<>();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dlg_county_picker, container, false);
//        ButterKnife.bind(this, rootView);
        disposalData();
        initView(rootView);
        setListener();
        return rootView;
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
            if (null != listener) {
                listener.onPick(scrollerNumberPicker.getSelectedText(),getCountyCode(scrollerNumberPicker.getSelectedText()));
            }
            dismiss();
        }
    }

    public CountyPickerDlg setCityInfo(final String provStr, final String cityStr) {
        initProv = provStr;
        initCity = cityStr;

        return this;
    }

    private void initView(View rootView) {
        dlg_whole = (RelativeLayout) rootView.findViewById(R.id.dlg_whole);
        dlg_frame = (LinearLayout) rootView.findViewById(R.id.dlg_frame);
        btn_cancel = (TextView) rootView.findViewById(R.id.btn_cancel);
        btn_confirm = (TextView) rootView.findViewById(R.id.btn_confirm);
        scrollerNumberPicker = (ScrollerNumberPicker) rootView.findViewById(R.id.county_picker);

        searchCountyList(initProv,initCity);
        scrollerNumberPicker.setData(countyNameList);
        scrollerNumberPicker.setDefault(0);
    }

    private void disposalData() {
        if (null != county) {
            for (int i = 0; i < county.getCounty().size(); i++) {
                if (1 == county.getCounty().get(i).getLevel()) {
                    provList.add(county.getCounty().get(i));
                    provMap.put(county.getCounty().get(i).getSheng(), new ArrayList<CountyInfor>());
                    provMap.get(county.getCounty().get(i).getSheng()).add(county.getCounty().get(i));
                }
                if (2 == county.getCounty().get(i).getLevel()) {
                    if (null != provMap.get(county.getCounty().get(i).getSheng())) {
                        provMap.get(county.getCounty().get(i).getSheng()).add(county.getCounty().get(i));
                    }
                }
            }
        }
    }

    private ArrayList<String> countyNameList = new ArrayList<>();
    private ArrayList<CountyInfor> list = new ArrayList<>();

    String sheng = null;
    String di = null;

    private void searchCountyList(String provStr, String cityStr) {


        for (int i = 0; i < provList.size(); i++) {
            if (provList.get(i).getName().contains(provStr)) {
                list = provMap.get(provList.get(i).getSheng());
                break;
            }
        }
        if (list.size() > 0) {
            if (StringUtil.isEqual(provStr, cityStr)) {
                sheng = list.get(0).getSheng();
                for (int i = 0; i < county.getCounty().size(); i++) {
                    if(3 == county.getCounty().get(i).getLevel() && StringUtil.isEqual(sheng,county.getCounty().get(i).getSheng())){
                        countyNameList.add(county.getCounty().get(i).getName());
                    }
                }
            }else {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getName().contains(cityStr)){
                        sheng = list.get(i).getSheng();
                        di = list.get(i).getDi();
                        break;
                    }
                }
                for (int i = 0; i < county.getCounty().size(); i++){
                    if(3 == county.getCounty().get(i).getLevel() && StringUtil.isEqual(sheng,county.getCounty().get(i).getSheng())
                            && StringUtil.isEqual(di, county.getCounty().get(i).getDi())){
                        countyNameList.add(county.getCounty().get(i).getName());
                    }
                }
            }
        }
    }


    public String getCountyCode(String countyName){
        for(int i = 0;i< county.getCounty().size(); i++){
            if (StringUtil.isEqual(sheng,county.getCounty().get(i).getSheng()) && StringUtil.isEqual(countyName,county.getCounty().get(i).getName())){
                countyCode =county.getCounty().get(i).getCode();
                break;
            }
        }
        return countyCode;
    }


}
