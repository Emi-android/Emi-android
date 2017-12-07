package com.hcb.payment;


import com.proj.emi.model.base.OutBody;

/**
 * Created by lzh on 2017/4/1.
 */
public class AliPayResultOutBody extends OutBody {
    private String myOrderUuid;
    private String alipayResult;

    public String getMyOrderUuid() {
        return myOrderUuid;
    }

    public AliPayResultOutBody setMyOrderUuid(String myOrderUuid) {
        this.myOrderUuid = myOrderUuid;
        return this;
    }

    public String getAlipayResult() {
        return alipayResult;
    }

    public AliPayResultOutBody setAlipayResult(String alipayResult) {
        this.alipayResult = alipayResult;
        return this;
    }
}
