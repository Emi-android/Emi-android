package com.proj.emi.frg.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hcb.city.CityPickerDlg;
import com.hcb.util.StringUtil;
import com.hcb.util.ToastUtil;
import com.lzh.citytime.city.CountyPickerDlg;
import com.lzh.citytime.time.DatePickerDlg;
import com.proj.emi.R;
import com.proj.emi.loader.CheckLoader;
import com.proj.emi.loader.base.AbsLoader;
import com.proj.emi.model.CheckInBody;
import com.proj.emi.model.CheckOutBody;
import com.proj.emi.model.base.InBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;


public class HomePageFrg extends CachableFrg {

    private final static Logger LOG = LoggerFactory.getLogger(HomePageFrg.class);

    @Override
    protected int rootLayout() {
        return R.layout.main_homepage;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (null == curUser.getUid()) {
//            ActivitySwitcher.startFragment(getActivity(), LoginFrg.class);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }

    private String storeAddressProvText, storeAddressCityText;

    @BindView(R.id.cityTv)
    TextView cityTv;
    @OnClick(R.id.cityTv)
    void city(){
        new CityPickerDlg().setPickListener(new CityPickerDlg.CityPickListener() {
            @Override
            public void onPick(String province, String city) {
                storeAddressProvText = province;
                storeAddressCityText = city;
                cityTv.setText(province + " " + city);
            }
        }).show(getFragmentManager(), "chooseStoreAddress");
    }

    @BindView(R.id.select_qunxian)
    TextView select_qunxian;
    @OnClick(R.id.select_qunxian)
    void quxian(){
        if (StringUtil.isEmpty(storeAddressProvText) || StringUtil.isEmpty(storeAddressCityText)){
            ToastUtil.show("请先选择城市！");
            return;
        }else {
            new CountyPickerDlg().setCityInfo(storeAddressProvText, storeAddressCityText).setPickListener(new CountyPickerDlg.CityPickListener() {
                @Override
                public void onPick(String county, String code) {
                    select_qunxian.setText(county);
                    select_qunxian.setTextColor(getResources().getColor(R.color.txt_lvl_1));
                }
            }).show(getFragmentManager(), "chooseStoreCountyInfo");
        }
    }
    private String startDate;
    @BindView(R.id.select_date_go)
    TextView select_date_go;
    @OnClick(R.id.select_date_go)
    void data(){
        new DatePickerDlg().setMinDate(new Date()).setListener(new DatePickerDlg.DatePickListener() {
            @Override
            public void onPicked(String date) {
                startDate = date;
                select_date_go.setText(date);
            }
        }).show(getChildFragmentManager(), "dataDlg");
    }
    @BindView(R.id.select_time)
    TextView select_time;
    @OnClick(R.id.select_time)
    void time(){
        if (StringUtil.isEmpty(startDate)){
            ToastUtil.show("请先选择开始日期");
            return;
        }
        new DatePickerDlg().setMinDate(new Date()).setInitDate(new Date()).hindDate().showHourMinute().setListener(new DatePickerDlg.DatePickListener() {
            @Override
            public void onPicked(String date) {
                select_time.setText(date);
                select_time.setTextColor(Color.BLACK);
            }
        }).show(getChildFragmentManager(), "time");

    }

    @OnClick(R.id.checkLoaderTv)
    void checkLoader(){
        curUser.setUid("176528635947319012");
        curUser.setToken("f3fb8a0bc867cbfb4d3c8bfe48ff103ba20a1febaef5b7c636fc42883c8f8ac9");
        CheckOutBody body = new CheckOutBody();
        body.setPage_num("23");
        body.setEvent_uuid("176528633876512000");
        new CheckLoader().upload(body, new AbsLoader.RespReactor<CheckInBody>() {
            @Override
            public void succeed(CheckInBody body) {
                ToastUtil.show(body.getIs_correct() + " " + body.getDescription());
            }

            @Override
            public void failed(String code, String reason) {

            }
        });
    }

}
