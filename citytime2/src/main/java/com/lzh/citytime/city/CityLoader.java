package com.lzh.citytime.city;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.lzh.citytime.util.FileUtil;

/**
 * Created by dyx on 2016/2/24.
 */
public class CityLoader {
    private final static String CITY_FILE = "citys.json";
    private final static String COUNTY_FILE = "county.json";

    private static Country country;
    private static County county;

    public static Country load(Context context) {
        if (null != country) {
            return country;
        }
        final String str = FileUtil.readAssets(context, CITY_FILE);
        if (!TextUtils.isEmpty(str)) {
            try {
                country = JSONObject.parseObject(str, Country.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return country;
    }
    public static County loadCounty(Context context){
        if(null != county){
            return county;
        }
        final String str = FileUtil.readAssets(context,COUNTY_FILE);
        if(!TextUtils.isEmpty(str)){
            try {
                county = JSONObject.parseObject(str,County.class);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return  county;
    }
}
