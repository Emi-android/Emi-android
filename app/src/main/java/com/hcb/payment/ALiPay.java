package com.hcb.payment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.hcb.util.ReflectUtil;
import com.hcb.util.StringUtil;
import com.hcb.util.ToastUtil;
import com.proj.emi.loader.base.AbsLoader;
import com.proj.emi.loader.base.BasePostLoader;
import com.proj.emi.model.base.OutBody;

import java.util.Map;

/**
 * Created by lzh on 2017/4/1.
 *
 * 1、发起支付宝支付请求时，直接新建一个类继承自ALiPay，在新建的累的构造方法里实现ALiPay的构造方法，
 *    传入发起支付和获取支付结果的API地址PATH。
 * 2、通过ALiPostPay（OutBody, RespReactop）方法从服务器获取支付订单信息发起支付。
 * 3、根据需求，在成功和失败回调内进行实际逻辑操作。
 *
 * 说明：支付宝支付时同步返回的支付结果不能作为实际的支付结果来进行判断，要以服务器返回的支付结果
 *       为准，该逻辑已经封装好了，使用过程中不用再对支付结果再次进行校验，使用者可以直接根据返回
 *       的结果进行实际判断。
 */
public class ALiPay<OUTBODY extends OutBody, INBODY extends PayInBody> {
    protected String PATH;
    protected String PATH1;
    private Activity context;
    private AbsLoader.RespReactor<INBODY> reactor;

    /**
     * * @param payPath       获取支付宝支付参数的API路径
     * @param resultPath    获取支付宝支付结果的API路径
     */
    public ALiPay(Activity context, String payPath, String resultPath){
        this.context = context;
        PATH = payPath;
        PATH1 = resultPath;
    }

    /**
     * 发起支付宝支付API，并根据从服务端获取支付宝支付参数来发起支付到支付宝支付，最终返回支付结果
     * @param outbody
     * @param reactor
     */
    public void aLiPostPay(OUTBODY outbody, final AbsLoader.RespReactor<INBODY> reactor){
        this.reactor = reactor;
        new AliPayLoader().pay(outbody, new AbsLoader.RespReactor<INBODY>() {
            @Override
            public void succeed(final INBODY body) {
                final String orderInfo = body.getAliSignedOrder();
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(context);
                        Map<String, String> result = alipay.payV2(orderInfo, true);
                        Bundle bundle = new Bundle();
                        bundle.putString("myOrderUuid", body.getMyOrderUuid());
                        bundle.putString("alipayResult", body.getAliSignedOrder());
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG1;
                        msg.obj = result;
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }
                };
                Thread payThread = new Thread(payRunnable);
                payThread.start();
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
    class AliPayLoader extends BasePostLoader {
        public void pay(OUTBODY outbody, RespReactor<INBODY> respReactor) {
            super.load(genUrl(PATH), outbody, respReactor);
        }

        @Override
        protected Class inBodyClass() {
            return ALiPay.this.inBodyClass();
        }
    }

    /**
     * 获取支付结果的网络请求Loader
     */
    class AliPayResultLoader extends BasePostLoader {
        public void loadData(AliPayResultOutBody outBody, RespReactor<INBODY> respReactor) {
            super.load(genUrl(PATH1), outBody, respReactor);
        }

        @Override
        protected Class inBodyClass() {
            return ALiPay.this.inBodyClass();
        }
    }

    /**
     * 通过反射机制，获取InBody的具体类型
     * @return
     */
    protected Class<INBODY> inBodyClass() {
        return ReflectUtil.getClassGenricType(getClass(), 1);
    }


    private static final int SDK_PAY_FLAG1 = 101;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG1:
                    ALiPayResult payResult = new ALiPayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        alipayResult(msg.getData().getString("myOrderUuid"), payResult);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        alipayResult(msg.getData().getString("myOrderUuid"), payResult);
                        break;
                    }
            }
        }
    };

    private AliPayResultOutBody zfbresout;
    public void alipayResult(String myOrderUuid, ALiPayResult payResult) {
        if (StringUtil.isEqual("操作已经取消。", payResult.getMemo())) {
            ToastUtil.show("操作已经取消！");
            reactor.failed("003", "操作已经取消");
        } else if (StringUtil.isEqual("用户取消", payResult.getMemo())) {
            ToastUtil.show("用户取消！");
            reactor.failed("003", "用户取消");
        } else {
            zfbresout = new AliPayResultOutBody();
            zfbresout.setMyOrderUuid(myOrderUuid);
            zfbresout.setAlipayResult(payResult.toString());
            new AliPayResultLoader().loadData(zfbresout, new AbsLoader.RespReactor<INBODY>() {
                @Override
                public void succeed(INBODY body) {
                    reactor.succeed(body);
                }

                @Override
                public void failed(String code, String reason) {
                    reactor.failed(code, reason);
                }
            });
        }
    }
}