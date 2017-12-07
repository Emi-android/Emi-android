package com.hcb.payment;

import android.content.Context;

import com.hcb.util.ReflectUtil;
import com.proj.emi.AppConsts;
import com.proj.emi.loader.base.AbsLoader;
import com.proj.emi.loader.base.BasePostLoader;
import com.proj.emi.model.base.OutBody;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by lzh on 2017/4/1.
 *
 * 1、发起支付宝支付请求时，直接新建一个类继承自WechatPay，在新建的累的构造方法里实现WechatPay(Context context, String path)构造方法，传入发起支付API地址PATH。
 * 2、通过wechatPostPay（OutBody, RespReactop）方法从服务器获取支付订单信息发起支付。
 * 3、根据需求，在成功和失败回调内进行实际逻辑操作。
 * 4、在WXPayEntryActivity的onResp(BaseResp resp)方法中，进行支付结果的确认，具体与服务器的交互逻辑根据具体需求确定。
 *
 */
public class WechatPay<OUTBODY extends OutBody, INBODY extends PayInBody> {
    private String PATH;
    private Context context;

    /**
     * @param context  上下文
     * @param path     获取微信支付参数的API路径
     */
    public WechatPay(Context context, String path){
        this.PATH = path;
        this.context = context;
    }

    /**
     * 发起微信支付API，并通过服务端返回的支付参数调起微信支付请求到微信
     * @param outBody
     * @param reactor
     */
    public void wechatPostPay(OUTBODY outBody, final AbsLoader.RespReactor<INBODY> reactor){
        new WechatPayLoader().pay(outBody, new AbsLoader.RespReactor<INBODY>() {
            @Override
            public void succeed(INBODY body) {
                reactor.succeed(body);
                sendWechatPay(body.getWxOrder());
            }

            @Override
            public void failed(String code, String reason) {
                reactor.failed(code, reason);
            }
        });
    }

    /**
     * 获取支付参数的网络请求Loader
     */
    class WechatPayLoader extends BasePostLoader {
        public void pay(OUTBODY outbody, RespReactor<INBODY> respReactor) {
            super.load(genUrl(PATH), outbody, respReactor);
        }

        @Override
        protected Class inBodyClass() {
            return WechatPay.this.inBodyClass();
        }
    }

    protected Class<INBODY> inBodyClass() {
        return ReflectUtil.getClassGenricType(getClass(), 1);
    }

    /**
     * 调起微信支付
     */
    private void sendWechatPay(WXOrderVo wxOrder) {
        //注册APPID
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(AppConsts.APP_ID);
        //调起支付
        PayReq request = new PayReq();
        request.appId = wxOrder.getAppId();
        request.partnerId = wxOrder.getPartnerId();
        request.prepayId = wxOrder.getPrepayId();
//        request.packageValue = "Sign=WXPay";
        request.packageValue = wxOrder.getPkg();
        request.nonceStr = wxOrder.getNoncestr();
        request.timeStamp = wxOrder.getTimestamp();
        request.sign = wxOrder.getSign();
        msgApi.sendReq(request);
    }

}