package com.lzh.citytime.city;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzh.citytime.R;
import com.lzh.citytime.dialog.BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by dyx on 2016/10/11.
 */
public class CitydiareaDlg extends DialogFragment implements View.OnClickListener{

    protected Activity act;
    private RelativeLayout dlg_whole;
    private LinearLayout dlg_frame;
    private TextView btn_cancel;
    private TextView btn_confirm;
    private CityPicker cityPicker;


    public interface CityPickListener {
        void onPick(String province, String city);
    }

    private Country country;
    private CityPickListener listener;

    private String initProv, initCity, area ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = getActivity();
        country = CityLoader.load(act);
    }

    public CitydiareaDlg setDefault(final String provStr, final String cityStr, final String area) {
        initProv = provStr;
        initCity = cityStr;
        return this;
    }

    public CitydiareaDlg setPickListener(CityPickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dlg_city_picker, container, false);
        initView(rootView);
        initPicker();
        setListener();
        return rootView;
    }
    private void initView(View rootView){
        dlg_whole = (RelativeLayout) rootView.findViewById(R.id.dlg_whole);
        dlg_frame = (LinearLayout) rootView.findViewById(R.id.dlg_frame);
        btn_cancel = (TextView) rootView.findViewById(R.id.btn_cancel);
        btn_confirm = (TextView) rootView.findViewById(R.id.btn_confirm);
        cityPicker = (CityPicker) rootView.findViewById(R.id.city_picker);
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
                listener.onPick(cityPicker.getProvince_string(), cityPicker.getCity_string());
            }
            dismiss();
        }
    }


    private void initPicker() {
        if (null != country) {
            cityPicker.setProvinces(country.getProvinces())
                    .setCitys(country.getCitys())
                    .setDefault(initProv, initCity)
                    .initView();
        }
    }

}
