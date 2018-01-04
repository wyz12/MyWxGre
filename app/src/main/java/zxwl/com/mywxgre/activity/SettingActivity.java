package zxwl.com.mywxgre.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mixiaoxiao.smoothcompoundbutton.SmoothCompoundButton;
import com.mixiaoxiao.smoothcompoundbutton.SmoothSwitch;

import zxwl.com.mywxgre.R;
import zxwl.com.mywxgre.utils.SettingConfig;


/**
 * Created by junwen on 2017/11/3.
 */

public class SettingActivity extends AppCompatActivity implements SmoothCompoundButton.OnCheckedChangeListener{


    private SmoothSwitch mReEnable;
    private SmoothSwitch mMusicEnable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initListener();
    }


    private void initView() {
        mReEnable = (SmoothSwitch) findViewById(R.id.setting_re_enable);
        mMusicEnable = (SmoothSwitch) findViewById(R.id.setting_music_enable);


        //初始化开关
        boolean reEnable = SettingConfig.getInstance().getReEnable();
        boolean reMusicEnable = SettingConfig.getInstance().getReMusicEnable();

        mReEnable.setChecked(reEnable);
        mMusicEnable.setChecked(reMusicEnable);

    }

    private void initListener() {
        mReEnable.setOnCheckedChangeListener(this);
        mMusicEnable.setOnCheckedChangeListener(this);
    }

    public void onCheckedChanged(SmoothCompoundButton smoothCompoundButton, boolean check) {
        switch (smoothCompoundButton.getId()) {
            case R.id.setting_re_enable:
                //抢红包开关变化
                SettingConfig.getInstance().setReEnable(check);


                break;
            case R.id.setting_music_enable:
                //抢红包音乐开关变化
                SettingConfig.getInstance().setReMusicEnable(check);
                break;
        }
    }




}
