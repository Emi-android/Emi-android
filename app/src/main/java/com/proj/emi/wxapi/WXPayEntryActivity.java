package com.proj.emi.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.hcb.util.StringUtil;
import com.proj.emi.AppConsts;
import com.proj.emi.GlobalBeans;
import com.proj.emi.R;
import com.proj.emi.biz.EventCenter;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by lzh on 2017/4/1.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "Apparel.WXPayEntryActivity";
    private static final String TRADE_STATE = "SUCCESS";
    private IWXAPI api;
    private EventCenter eventCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_entry);

        api = WXAPIFactory.createWXAPI(this, AppConsts.APP_ID);
        api.handleIntent(getIntent(), this);
        eventCenter = GlobalBeans.getSelf().getEventCenter();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {//判断是否是微信支付状态码
            if (-2 != resp.errCode) {//用户没有取消，执行微信支付结果确认逻辑，根据实际需求请求服务器进行支付结果确认
                //---------

            } else {
                creatAlerDlg("支付请求已取消！");
            }
        }
    }


    /**
     * 提示对话框
     */

    public void creatAlerDlg(final String desc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(desc);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (StringUtil.isEqual("支付成功！",desc)){
                    eventCenter.send(new EventCenter.HcbEvent(EventCenter.EventType.EVT_ORDER_PAY_SUCCESS));
                }
                finish();
            }
        });
        builder.create().show();
    }
}

