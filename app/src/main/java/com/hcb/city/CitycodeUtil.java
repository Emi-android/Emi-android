package com.hcb.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dyx on 2016/2/24.
 */
public class CitycodeUtil {

    private ArrayList<String> province_list = new ArrayList<String>();
    private ArrayList<String> city_list = new ArrayList<String>();
    private ArrayList<String> couny_list = new ArrayList<String>();
    public ArrayList<String> province_list_code = new ArrayList<String>();
    public ArrayList<String> city_list_code = new ArrayList<String>();
    public ArrayList<String> couny_list_code = new ArrayList<String>();
    /** 单例 */
    public static CitycodeUtil model;

    // private Context context;

    private CitycodeUtil() {
    }

    public ArrayList<String> getProvince_list_code() {
        return province_list_code;
    }

    public ArrayList<String> getCity_list_code() {
        return city_list_code;
    }

    public void setCity_list_code(ArrayList<String> city_list_code) {
        this.city_list_code = city_list_code;
    }

    public ArrayList<String> getCouny_list_code() {
        return couny_list_code;
    }

    public void setCouny_list_code(ArrayList<String> couny_list_code) {
        this.couny_list_code = couny_list_code;
    }

    public void setProvince_list_code(ArrayList<String> province_list_code) {

        this.province_list_code = province_list_code;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static CitycodeUtil getSingleton() {
        if (null == model) {
            model = new CitycodeUtil();
        }
        return model;
    }

    public ArrayList<String> getProvince(List<CityInfo> provice) {
        if (province_list_code.size() > 0) {
            province_list_code.clear();
        }
        if (province_list.size() > 0) {
            province_list.clear();
        }
        for (int i = 0; i < provice.size(); i++) {
            province_list.add(provice.get(i).getName());
            province_list_code.add(provice.get(i).getCode());
        }
        return province_list;

    }

    public ArrayList<String> getCity(
            HashMap<String, ArrayList<CityInfo>> city_map, String provicecode) {
        if (city_list_code.size() > 0) {
            city_list_code.clear();
        }
        if (city_list.size() > 0) {
            city_list.clear();
        }
        List<CityInfo> city = new ArrayList<CityInfo>();
        city = city_map.get(provicecode);
        for (int i = 0; i < city.size(); i++) {
            city_list.add(city.get(i).getName());
            city_list_code.add(city.get(i).getCode());
        }
        return city_list;

    }

    public ArrayList<String> getCouny(
            HashMap<String, List<CityInfo>> cityHashMap, String citycode) {
        if (couny_list_code.size() > 0) {
            couny_list_code.clear();
        }
        if (couny_list.size() > 0) {
            couny_list.clear();
        }

        List<CityInfo> couny = cityHashMap.get(citycode);
        for (int i = 0; i < couny.size(); i++) {
            couny_list.add(couny.get(i).getName());
            couny_list_code.add(couny.get(i).getCode());
        }
        return couny_list;

    }
}