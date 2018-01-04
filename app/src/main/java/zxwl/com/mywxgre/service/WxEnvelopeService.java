package zxwl.com.mywxgre.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

import zxwl.com.mywxgre.MyApp;
import zxwl.com.mywxgre.common.BaseAccessibilityService;
import zxwl.com.mywxgre.utils.SettingConfig;
import zxwl.com.mywxgre.utils.listener.ScreenListener;


/**
 * Created by Administrator on 2017/12/29.
 */

public class WxEnvelopeService  extends BaseAccessibilityService {

    //锁屏、解锁相关
    private KeyguardManager.KeyguardLock kl;

    //唤醒屏幕相关
    private PowerManager.WakeLock wl = null;

    private long delayTime = 2;//延迟抢的时间

    /**
     * 描述:所有事件响应的时候会回调
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:2017/11/6 上午9:26
     */
    static final String ENVELOPE_TEXT_KEY = "[微信红包]";


    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!invalidEnable()) {
            return;
        }
        final int eventType = event.getEventType();

        //Log.d(TAG, "微信事件--------------------------" + event.getEventType());

        //通知栏事件
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            List<CharSequence> texts = event.getText();
            if (!texts.isEmpty()) {
                for (CharSequence t : texts) {
                    String text = String.valueOf(t);
                    Log.v("tag", "监控微信消息==" + text);
                    if (text.contains(ENVELOPE_TEXT_KEY)) {
                        openNotification(event);
                        break;
                    }
                }
            }
        } else if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            openEnvelope(event);
        } else if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            openEnvelopeContent(event);
        }
    }

    /**
     * 打开通知栏消息并跳转到红包页面
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openNotification(AccessibilityEvent event) {
        if (event.getParcelableData() == null || !(event.getParcelableData() instanceof Notification)) {
            return;
        }
        //以下是精华，将微信的通知栏消息打开
        Notification notification = (Notification) event.getParcelableData();
        PendingIntent pendingIntent = notification.contentIntent;
        try {
            wakeUpAndUnlock(MyApp.context);
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void openEnvelope(AccessibilityEvent event) {
        CharSequence className = event.getClassName();
        Log.e("TTTT",className.toString());

        if ("com.tencent.mm.ui.LauncherUI".equals(event.getClassName())) {
            checkKeyFirst();
        } else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI".equals(event.getClassName())) {
            checkOpen();
        } else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI".equals(event.getClassName())) {

            Toast.makeText(this, "红包已经抢完--------", Toast.LENGTH_SHORT).show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void openEnvelopeContent(AccessibilityEvent event) {

        if ("android.widget.TextView".equals(event.getClassName())) {
            checkKeyFirst();
        } else if ("com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f".equals(event.getClassName())) {
            checkOpen();
        } else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI".equals(event.getClassName())) {
             Toast.makeText(this, "红包已经抢完", Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void checkOpen() {
        //        com.tencent.mm:id/c2i
        Log.e("TTT","AAA");
        AccessibilityNodeInfo sendMessage = findViewByID("com.tencent.mm:id/c2i");
        performViewClick(sendMessage);
//        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
//        if (nodeInfo == null) {
//            return;
//        }
//        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("开");
//        for (int i = list.size() - 1; i >= 0; i--) {
//
//            AccessibilityNodeInfo parent = list.get(i).getParent();
//            for (int j = 0; j < parent.getChildCount(); j++) {
//                AccessibilityNodeInfo child = parent.getChild(j);
//                if (child != null && child.isClickable()) {
//                    child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                    break;
//                }
//            }
//
//        }
    }

    private void checkKeyFirst() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("领取红包");
        if (list.isEmpty()) {
            return;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            AccessibilityNodeInfo parent = list.get(i).getParent();
            if (parent != null) {
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
        }
    }

   



    /**
     * 唤醒屏幕并解锁权限
     * <uses-permission android:name="android.permission.WAKE_LOCK" />
     */
    @SuppressLint("Wakelock")
    @SuppressWarnings("deprecation")
    public void wakeUpAndUnlock(Context context) {
        // 点亮屏幕
        wl.acquire();
        // 释放
        wl.release();
        // 解锁
        kl.disableKeyguard();
    }



    @Override
    public void onInterrupt() {
    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        // 获取电源管理器对象
        PowerManager pm = (PowerManager) MyApp.context
                .getSystemService(Context.POWER_SERVICE);
        // 获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
        wl = pm.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");

        KeyguardManager km = (KeyguardManager) MyApp.context.getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("unLock");

        //初始化屏幕的监听
        ScreenListener screenListener = new ScreenListener(MyApp.context);
        screenListener.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                Log.e("ScreenListener", "屏幕打开了");
            }

            @Override
            public void onScreenOff() {
                //在屏幕关闭的时候，进行锁屏，不执行的话，锁屏就失效了，因为要实现锁屏状态下也可以进行抢红包。
                Log.e("ScreenListener", "屏幕关闭了");
                if (kl != null) {
                    kl.disableKeyguard();
                    kl.reenableKeyguard();
                }
            }

            @Override
            public void onUserPresent() {
                Log.e("ScreenListener", "解锁了");
            }
        });
    }
    private boolean invalidEnable() {
        return SettingConfig.getInstance().getReEnable();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void back2Home(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }
}
