package zxwl.com.mywxgre.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import zxwl.com.mywxgre.R;
import zxwl.com.mywxgre.Utilis;
import zxwl.com.mywxgre.service.Server;
import zxwl.com.mywxgre.utils.SettingConfig;
import zxwl.com.mywxgre.utils.TimeUtlis;
import zxwl.com.mywxgre.wxapi.Bean;

public class Main4Activity extends AppCompatActivity implements View.OnClickListener {


    private ArrayList<String> list;
    /**
     * 账号
     */
    private TextView mPhone;
    /**
     * 时间
     */
    private TextView mViptime;
    private String phone;
    private String vip;
    private String time;
    private Handler han = new Handler();
    private Runnable runn = new Runnable() {
        @Override
        public void run() {

            cx();
            cxx();
            han.postDelayed(runn, 2000000);
        }
    };
    /**
     * 抢红包
     */
    private Button mQhb;
    /**
     * 更多功能
     */
    private Button mGdgn;
    /**
     * 开通会员
     */
    private Button mKthy;
    /**
     * 邀请好友
     */
    private Button mYqhy;
    /**
     * 设置
     */
    private Button mSz;
    /**
     * 后台运行
     */
    private Button mTc;
    private ImageView mImage;
    private int mFinishTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        initView();

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        mPhone.setText("用户名\n" + phone);
//        检测会员是否到期
        cx();
//        //        检测应用是否需要更新
        gxjc();
        cxx();
        han.postDelayed(runn, 2000000);


    }

    private void initView() {

        mPhone = (TextView) findViewById(R.id.phone);
        mViptime = (TextView) findViewById(R.id.viptime);
        mQhb = (Button) findViewById(R.id.qhb);
        mQhb.setOnClickListener(this);
        mGdgn = (Button) findViewById(R.id.gdgn);
        mGdgn.setOnClickListener(this);
        mKthy = (Button) findViewById(R.id.kthy);
        mKthy.setOnClickListener(this);
        mYqhy = (Button) findViewById(R.id.yqhy);
        mYqhy.setOnClickListener(this);
        mSz = (Button) findViewById(R.id.sz);
        mSz.setOnClickListener(this);
        mTc = (Button) findViewById(R.id.tc);
        mTc.setOnClickListener(this);
        mImage = (ImageView) findViewById(R.id.image);
    }


    private void cx() {
        RequestBody body = new FormBody.Builder().add("action", "hqvip").add("phone", phone).build();
        Utilis.getInstance().sendPost("http://www.zxwlwh.com/Wx/WxGre.php", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                String string = response.body().string();
                Gson gson = new Gson();
                Bean bean = gson.fromJson(string, Bean.class);
                    vip = bean.getVip();
                }catch(NullPointerException vip) {
                    SharedPreferences sp = getSharedPreferences("SP", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(Main4Activity.this,Main2Activity.class));
                    finish();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Main4Activity.this, "账号异123常请重新登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }



                hq();


            }
        });
    }




    private void hq() {
        RequestBody body = new FormBody.Builder().add("action", "severtime").build();
        Utilis.getInstance().sendPost("http://www.zxwlwh.com/Wx/WxGre.php", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Gson gson = new Gson();
                Bean bean = gson.fromJson(string, Bean.class);
                time = bean.getTime();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("VIP".equals(vip)) {
                            mViptime.setText("永久VIP");
                            mQhb.setEnabled(true);
                            TimeUtlis.setPd(true);
                            return;
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {

                            long vv = sdf.parse(vip).getTime();

                            long tt = sdf.parse(time).getTime();

                           if (tt > vv) {
                                mViptime.setText("体验会员到期\n开通永久会员限时9.9元");
                                mQhb.setEnabled(false);
                                SettingConfig.getInstance().setReEnable(false);
                                mQhb.setText("抢红包\n体验会员到期\n开通永久会员\n或邀请好友得体验");
                                TimeUtlis.setPd(false);
                                mViptime.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(Main4Activity.this, Main6Activity.class));
                                    }
                                });
                            }else{
                                mViptime.setText("体验会员到期时间\n" + vip);
                                mQhb.setEnabled(true);
                                TimeUtlis.setPd(true);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }


    private void cxx() {
        RequestBody body = new FormBody.Builder().add("action", "total").add("phone", phone).build();
        Utilis.getInstance().sendPost("http://www.zxwlwh.com/Wx/WxGre.php", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        Bean bean = gson.fromJson(string, Bean.class);

                        String login1 = bean.getLogin();

                        SharedPreferences sp = getSharedPreferences("SP", MODE_PRIVATE);
                        String login = sp.getString("login", "");
                        if (!login.equals("")) {
                            if (!login1.equals(login)) {

                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                                cr();
                                Toast.makeText(Main4Activity.this, "有用户登录了您的账号", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

            }
        });
    }

    private void cr() {
        RequestBody body = new FormBody.Builder().add("action", "modifylogin").add("phone", phone).add("login", "0").build();
        Utilis.getInstance().sendPost("http://www.zxwlwh.com/Wx/WxGre.php", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Gson gson = new Gson();
                Bean bean = gson.fromJson(string, Bean.class);

                if (bean.isModify_result()) {
                    startActivity(new Intent(Main4Activity.this, Main2Activity.class));
                    finish();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.qhb:
                startActivity(new Intent(Main4Activity.this, MainActivity.class));
                break;
            case R.id.gdgn:
                Toast.makeText(this, "等待下个版本添加", Toast.LENGTH_SHORT).show();
                break;
            case R.id.kthy:
                Intent intent = new Intent(Main4Activity.this, Main6Activity.class);
                 intent.putExtra("vip",vip);
                startActivity(intent);
                break;
            case R.id.yqhy:
                Intent intent2 = new Intent(Main4Activity.this, Main7Activity.class);
                intent2.putExtra("phone", phone);
                startActivity(intent2);
                break;
            case R.id.sz:
                Intent intent1 = new Intent(Main4Activity.this, Main5Activity.class);
                intent1.putExtra("phone", phone);
                startActivity(intent1);
                break;
            case R.id.tc:
                finish();
                break;
        }
    }


    private void gxjc() {
//        http://www.zxwlwh.com/Wx/WxGre.php?action=hqediton
        RequestBody body = new FormBody.Builder().add("action", "hqediton").build();
        Utilis.getInstance().sendPost("http://www.zxwlwh.com/Wx/WxGre.php", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        Bean bean = gson.fromJson(string, Bean.class);
                        String edition = bean.getDatas().get(0).getEdition();
                        String address = bean.getDatas().get(0).getAddress();
                        String img = bean.getDatas().get(0).getImg();
                        Log.e("TTT",img);
                        Glide.with(Main4Activity.this).load(img).into(mImage);
                        long l = System.currentTimeMillis();
                        SharedPreferences preferences = getSharedPreferences("SP", Context.MODE_PRIVATE);
                        long int_key = preferences.getLong("INT_KEY", 0);

                        if (int_key < l) {

                            if (!edition.equals("1")) {
                                ShowEditonDialog(address);
                            }
                            long l1 = l + 30000000;
                            SharedPreferences sp = getSharedPreferences("SP", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putLong("INT_KEY", l1);
                            editor.commit();
                        }

                    }
                });

            }
        });


    }


    private void ShowEditonDialog(final String address) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("有新版本是否更新");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(Main4Activity.this, Server.class);
                intent.putExtra("url", address);
                startService(intent);
            }
        });
        builder.setPositiveButton("取消", null);
        builder.show();
    }

    long firstTime;
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 800) {//如果两次按键时间间隔大于800毫秒，则不退出
                Toast.makeText(Main4Activity.this, "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                firstTime = secondTime;//更新firstTime
                return true;
            } else {
                System.exit(0);//否则退出程序
            }
        }
        return super.onKeyUp(keyCode, event);
    }

}

