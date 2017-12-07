package com.hcb.payment;


import com.proj.emi.model.base.InBody;

/**
 * Created by lzh on 2017/4/1.
 */

public class PayInBody extends InBody {
    private String myOrderUuid;//订单ID
    private WXOrderVo wxOrder;//微信支付请求参数
    private String aliSignedOrder;//支付宝支付请求参数
    private String trade_state;//支付宝支付结果参数 ：“SUCCESS”，“FELL”

    public String getMyOrderUuid() {
        return myOrderUuid;
    }

    public void setMyOrderUuid(String myOrderUuid) {
        this.myOrderUuid = myOrderUuid;
    }

    public WXOrderVo getWxOrder() {
        return wxOrder;
    }

    public void setWxOrder(WXOrderVo wxOrder) {
        this.wxOrder = wxOrder;
    }

    public String getAliSignedOrder() {
        return aliSignedOrder;
    }

    public void setAliSignedOrder(String aliSignedOrder) {
        this.aliSignedOrder = aliSignedOrder;
    }

    public String getTrade_state() {
        return trade_state;
    }

    public void setTrade_state(String trade_state) {
        this.trade_state = trade_state;
    }
}
