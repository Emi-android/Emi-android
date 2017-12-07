package com.proj.emi.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.proj.emi.bean.SysMsgVO;
import com.proj.emi.model.base.InBody;

import java.util.ArrayList;

public class SysMsgInBody extends InBody {

    @JSONField(name = "sys_msg_list") private ArrayList<SysMsgVO> msgList;

    public ArrayList<SysMsgVO> getMsgList() {
        return msgList;
    }

    public void setMsgList(ArrayList<SysMsgVO> msgList) {
        this.msgList = msgList;
    }
}
