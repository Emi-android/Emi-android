package com.proj.emi.loader;

import com.proj.emi.loader.base.BaseGetLoader;
import com.proj.emi.model.SysMsgInBody;

public class SysMsgLoader extends BaseGetLoader<SysMsgInBody> {

    private final static String PATH = "service/sys_msg_list/%s/%d/%s";
    // {user_uuid}/({pagesize})/({sys_msg_uuid}))

    public void load(int size, String lastId, RespReactor<SysMsgInBody> respReactor) {
        super.load(genUrl(PATH, curUser.getUid(), size, lastId), respReactor);
    }
}
