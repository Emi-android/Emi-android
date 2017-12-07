package com.proj.emi.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.proj.emi.model.base.InBody;

public class ImagePostInBody extends InBody {

    private String url;

    @JSONField(name = "file_url")
    public String getUrl() {
        return url;
    }

    @JSONField(name = "file_url")
    public void setUrl(String url) {
        this.url = url;
    }
}
