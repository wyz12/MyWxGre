package zxwl.com.mywxgre;

import android.app.Application;
import android.content.Context;

import zxwl.com.mywxgre.common.BaseAccessibilityService;
import zxwl.com.mywxgre.utils.SettingConfig;


/**
 * Created by junwen on 2017/10/31.
 */

public class MyApp extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //初始化辅助功能基类
        BaseAccessibilityService.getInstance().init(getApplicationContext());
        //初始化配置文件
        SettingConfig.getInstance().init(getApplicationContext());


    }



}
