package com.proj.emi.loader;

import com.proj.emi.loader.base.BasePostLoader;
import com.proj.emi.model.CheckInBody;
import com.proj.emi.model.CheckOutBody;
import com.proj.emi.model.base.InBody;

public class CheckLoader extends BasePostLoader<CheckOutBody, CheckInBody>{
    private final static  String PATH = "test/sign";
    public void upload(CheckOutBody body, RespReactor<CheckInBody> reactor){
        load(genUrl(PATH), body, reactor);
    }
}
