package com.proj.emi.model.login;

import com.alibaba.fastjson.annotation.JSONField;
import com.proj.emi.model.base.InBody;

public class LoginInBody extends InBody {

    @JSONField(name = "user_uuid") private String uuid;
    //    private String password;
    @JSONField(name = "is_new") private boolean isNew;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
}
