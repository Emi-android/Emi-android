package com.proj.emi.wxapi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.hcb.util.Md5;
import com.hcb.util.TimeUtil;
import com.proj.emi.AppConsts;
import com.proj.emi.GlobalBeans;
import com.proj.emi.HcbApp;
import com.proj.emi.R;
import com.proj.emi.biz.CurrentUser;
import com.proj.emi.biz.EventCenter;
import com.proj.emi.dialog.AlertDlg;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * Created by lzh on 2017/4/1.
 */
public class WXEntryActivity extends FragmentActivity implements IWXAPIEventHandler{

    //    private IWXAPI api;
    protected EventCenter eventCenter;
    protected GlobalBeans beans;
    protected CurrentUser curUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        //注册API
        HcbApp.api = WXAPIFactory.createWXAPI(this, AppConsts.APP_ID);
        HcbApp.api.handleIntent(getIntent(), this);
        beans = GlobalBeans.getSelf();
        curUser = beans.getCurUser();
        eventCenter = beans.getEventCenter();
//        Log.i("savedInstanceState"," sacvsa"+api.handleIntent(getIntent(), this));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        HcbApp.api.handleIntent(intent, this);//必须调用此句话
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    //  发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof SendAuth.Resp) {
            SendAuth.Resp newResp = (SendAuth.Resp) resp;
            //获取微信传回的code
            String code = newResp.code;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //发送成功
                    showProgressDialog("跳转", "登陆成功，正在返回...");
                    String pwd = Md5.encode(TimeUtil.getDateString() + code + AppConsts.FIT_PWD_KEY);
                    //根据具体场景，执行具体的操作：绑定、获取信息...
//                    new WeChatLoginLoader().load(code, pwd, new AbsLoader.RespReactor<WeChatLoginInBody>() {
//                        @Override
//                        public void succeed(InHead head, WeChatLoginInBody body) {
//                            onLogin(body);
//                            if (body.isHas_profile()) {
//                                dialog.dismiss();
//                                ActivitySwitcher.start(WXEntryActivity.this, MainActivity.class);
//                                finish();
//                            } else {
//                                dialog.dismiss();
//                                Bundle bundle = new Bundle();
//                                bundle.putBoolean(IS_LOGIN, true);
//                                ActivitySwitcher.startFragment(WXEntryActivity.this, UpdataFragment.class, bundle);
//                                finish();
//                            }
//                        }
//
//                        @Override
//                        public void failed(String code, String reason) {
//                            finish();
//                            ToastUtil.show("授权失败！");
//                        }
//                    });
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    //发送取消
                    showStatus("取消授权！");
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    //发送被拒绝
                    showStatus("授权被拒绝！");
                    break;
                default:
                    finish();
                    break;
            }
        }
        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    showStatus("分享成功！");
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    showStatus("取消分享！");
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    showStatus("分享被拒绝！");
                    break;
                default:
                    break;
            }
        }

    }


//    protected void onLogin(WeChatLoginInBody body) {
//        curUser.setPassword(body.getPassword());
//        curUser.setUid(body.getUser_uuid());
//        eventCenter.evtLogin();
//    }

    ProgressDialog dialog;

    protected void showProgressDialog(final String title, final String msg) {
        if (null == dialog) {
            dialog = ProgressDialog.show(this, title, msg);
        } else {
            dialog.setTitle(title);
            dialog.setMessage(msg);
            dialog.show();
        }
    }

    private void showStatus(String str) {
        new AlertDlg().setTitle("提示").setDesc(str).setSureListener(new AlertDlg.SureListener() {
            @Override
            public void onSure() {
                finish();
            }
        }).show(getSupportFragmentManager(), "alert");
    }
}
