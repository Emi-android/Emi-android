package com.hcb.city;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dyx on 2016/2/24.
 */
public class Country {

    private ArrayList<CityInfo> provinces;
    private HashMap<String, ArrayList<CityInfo>> citys;

    public ArrayList<CityInfo> getProvinces() {
        return provinces;
    }

    public void setProvinces(ArrayList<CityInfo> provinces) {
        this.provinces = provinces;
    }

    public HashMap<String, ArrayList<CityInfo>> getCitys() {
        return citys;
    }

    public void setCitys(HashMap<String, ArrayList<CityInfo>> citys) {
        this.citys = citys;
    }

}
