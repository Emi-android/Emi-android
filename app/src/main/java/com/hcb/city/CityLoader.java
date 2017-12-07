package com.hcb.city;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.hcb.util.FileUtil;

/**
 * Created by dyx on 2016/2/24.
 */
public class CityLoader {
    private final static String CITY_FILE = "citys.json";

    private static Country country;

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
}
