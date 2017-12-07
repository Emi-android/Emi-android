package com.hcb.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MultiListSerializer {

    public static <T> ArrayList<T>[] decode(String str, Class<T> type) {
        ArrayList<T>[] result = null;
        List<ArrayList> outer = JSON.parseArray(str, ArrayList.class);
        if (null != outer && outer.size() > 0) {
            result = new ArrayList[outer.size()];
            for (int i = 0; i < outer.size(); i++) {
                result[i] = new ArrayList<>();
                ArrayList<JSONObject> jsList = outer.get(i);
                if (null == jsList) {
                    continue;
                }
                for (JSONObject jso : jsList) {
                    result[i].add(JSON.parseObject(jso.toJSONString(), type));
                }
            }
        }
        return result;
    }
}
