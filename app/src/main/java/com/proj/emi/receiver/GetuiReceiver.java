package com.proj.emi.receiver;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.alibaba.fastjson.JSON;
import com.proj.emi.GlobalBeans;
import com.proj.emi.R;
import com.proj.emi.act.WelcomeActivity;
import com.proj.emi.bean.PushPayload;
import com.hcb.util.LoggerUtil;
//import com.igexin.sdk.PushConsts;
//import com.igexin.sdk.PushManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class GetuiReceiver extends BroadcastReceiver {

    private final static Logger LOG = LoggerFactory.getLogger(GetuiReceiver.class);

    private final static int NTF_CHAT_MSG = 10001;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

//        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
//            case PushConsts.GET_CLIENTID:
//                final String cid = bundle.getString("clientid");
//                saveCid(cid);
//                break;
//
//            case PushConsts.GET_MSG_DATA:
//                printBundle(bundle);
//                // 获取透传（payload）数据
//                byte[] payload = bundle.getByteArray("payload");
//                String taskid = bundle.getString("taskid");
//                String messageid = bundle.getString("messageid");
//                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
//                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
//                LoggerUtil.d(LOG, "----回执接口调用 {}", (result ? "成功" : "失败"));
//                if (payload != null) {
//                    String data = new String(payload);
//                    LoggerUtil.d(LOG, "----receiver payload :{} ", data);
//                    dispatchMsg(context, data);
//                }
//                break;
//
//            default:
//                LoggerUtil.d(LOG, "----onReceive CMD:{}", bundle.getInt(PushConsts.CMD_ACTION));
//                break;
//        }
    }

    private void saveCid(String cid) {
        if (null != GlobalBeans.getSelf()) {
            GlobalBeans.getSelf().getCurUser().setCid(cid);
//            new RpcUploader().updateCid();
        }
    }

    private static int tmpCount = 0;

    public static void testNotification(Context ctx) {
        tmpCount++;
        createNotification(ctx, "chatmsg_" + tmpCount, "外部推送调试" + tmpCount, "消息来自逆袭学院xx班级");
    }

    public static void clearNotification(Context ctx) {
        final NotificationManagerCompat ntfManager = NotificationManagerCompat.from(ctx);
        ntfManager.cancelAll();
    }

    private void printBundle(Bundle bundle) {
        Set<String> keys = bundle.keySet();
        if (null != keys) {
            LoggerUtil.d(LOG, "----收到透传数据包：");
            for (String key : keys) {
                LoggerUtil.d(LOG, "------{}:{}", key, bundle.get(key));
            }
        }
    }

    private void dispatchMsg(Context ctx, String payload) {
        tmpCount++;
        PushPayload pp = JSON.parseObject(payload, PushPayload.class);
        clearNotification(ctx);
        createNotification(ctx, "pushmsg_" + tmpCount, pp.getTitle(), pp.getContent());
        dlgPushMsg(pp);
    }

    private static void createNotification(Context ctx, String tag, String title, String desc) {
        final NotificationManagerCompat ntfManager = NotificationManagerCompat.from(ctx);
        Notification ntf = new NotificationCompat.Builder(ctx)
                .setAutoCancel(true)
                .setColor(Color.YELLOW)
                .setContentTitle(title)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.push)
                .setNumber(tmpCount)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(PendingIntent.getActivity(
                        ctx, 0, new Intent(ctx, WelcomeActivity.class), 0))
                .build();
        ntfManager.notify(tag, NTF_CHAT_MSG, ntf);
    }

    private static void dlgPushMsg(PushPayload pp) {
//        ActivityWatcher.curAct();
    }
}
