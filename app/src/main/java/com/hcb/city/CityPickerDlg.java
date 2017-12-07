package com.hcb.city;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proj.emi.R;
import com.proj.emi.dialog.BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dyx on 2016/2/24.
 */
public class CityPickerDlg extends BaseDialog {

    public interface CityPickListener {
        void onPick(String province, String city);
    }

    private Country country;
    private CityPickListener listener;

    private String initProv, initCity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        country = CityLoader.load(act);
    }

    public CityPickerDlg setDefault(final String provStr, final String cityStr) {
        initProv = provStr;
        initCity = cityStr;
        return this;
    }

    public CityPickerDlg setPickListener(CityPickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dlg_city_picker, container, false);
        ButterKnife.bind(this, rootView);
        initPicker();
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

    @BindView(R.id.city_picker) CityPicker cityPicker;

    private void initPicker() {
        if (null != country) {
            cityPicker.setProvinces(country.getProvinces())
                    .setCitys(country.getCitys())
                    .setDefault(initProv, initCity)
                    .initView();
        }
    }

    @OnClick(R.id.btn_confirm)
    void onPick(View v) {
        if (null != listener) {
            listener.onPick(cityPicker.getProvince_string(), cityPicker.getCity_string());
        }
        dismiss();
    }

}
