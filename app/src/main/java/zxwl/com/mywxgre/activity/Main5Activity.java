package zxwl.com.mywxgre.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import zxwl.com.mywxgre.R;
import zxwl.com.mywxgre.Utilis;
import zxwl.com.mywxgre.service.Server;
import zxwl.com.mywxgre.utils.MD5Jm;
import zxwl.com.mywxgre.wxapi.Bean;

public class Main5Activity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 退出登录
     */
    private Button mTc;
    private String phone;
    /**
     * 版本更新检测
     */
    private Button mGx;
    /**
     * 意见反馈
     */
    private Button mYjfk;
    private EditText editText;
    /**
     * 修改密码
     */
    private Button mXgpass;
    private EditText ypass;
    private EditText xpass;
    /**
     * 关于我们
     */
    private Button mGywm;
    /**
     * 联系客服
     */
    private Button mKf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        initView();
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
    }

    private void initView() {
        mTc = (Button) findViewById(R.id.tc);
        mTc.setOnClickListener(this);
        mGx = (Button) findViewById(R.id.gx);
        mGx.setOnClickListener(this);
        mYjfk = (Button) findViewById(R.id.yjfk);
        mYjfk.setOnClickListener(this);
        mXgpass = (Button) findViewById(R.id.xgpass);
        mXgpass.setOnClickListener(this);
        mGywm = (Button) findViewById(R.id.gywm);
        mGywm.setOnClickListener(this);
        mKf = (Button) findViewById(R.id.kf);
        mKf.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tc:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("是否确认退出登录");
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences sp = getSharedPreferences("SP", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.commit();
                        cr();
                    }
                });
                builder.setPositiveButton("取消", null);
                builder.show();

                break;
            case R.id.gx:
                gxjc();
                break;
            case R.id.yjfk:
                yjfk();
                break;
            case R.id.xgpass:
                passcr();
                break;
            case R.id.gywm:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setIcon(R.drawable.logo);
                builder1.setTitle("告示");
                builder1.setMessage("本应用由昭兴网络科技公司开发,\n切勿用到违背国家道德法律的地方" +
                        "\n争做遵纪守法好公民，共创美好未来 \n\n如有意见！！！\n感谢各位在意见反馈中提出你的宝贵见解\n \n            小昭在这里祝大家2018新年快乐");
                builder1.setNegativeButton("确定", null);
                builder1.show();
                break;
            case R.id.kf:
                if (checkApkExist(this, "com.tencent.mobileqq")) {

                    String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=1561230358&version=1";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                } else {
                    Toast.makeText(this, "本机未安装QQ应用", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
                    startActivity(new Intent(Main5Activity.this, Main2Activity.class));
                    finish();
                }

            }
        });
    }

    private void yjfk() {


        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(Main5Activity.this);
        normalDialog.setIcon(R.drawable.logo);
        normalDialog.setTitle("意见箱");
        normalDialog.setMessage("感谢你宝贵的意见");
        editText = new EditText(this);
        normalDialog.setView(editText);
        normalDialog.setPositiveButton("取消", null);
        normalDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!editText.getText().toString().equals("")) {
                    RequestBody body = new FormBody.Builder().add("action", "yhfk").add("phone", phone).add("yhyj", editText.getText().toString()).build();
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


                                    if (bean.isModify_result()) {
                                        Toast.makeText(Main5Activity.this, "感谢你宝贵的意见", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(Main5Activity.this, "服务器错误，请等一等在提交", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });

                        }
                    });

                } else {
                    Toast.makeText(Main5Activity.this, "请输入内容在提交", Toast.LENGTH_SHORT).show();

                }


            }
        });
        normalDialog.show();


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


                        if (!edition.equals("1")) {
                            ShowEditonDialog(address);
                        } else {
                            Toast.makeText(Main5Activity.this, "检测到服务器版本1.0,暂时不需要更新", Toast.LENGTH_SHORT).show();
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

                Intent intent = new Intent(Main5Activity.this, Server.class);
                intent.putExtra("url", address);
                startService(intent);
            }
        });
        builder.setPositiveButton("取消", null);
        builder.show();
    }


    private void passcr() {

        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(Main5Activity.this);
        normalDialog.setIcon(R.drawable.logo);
        normalDialog.setTitle("修改密码");
        ;
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_ls, null);
        normalDialog.setView(inflate);
        ypass = inflate.findViewById(R.id.ypass);
        xpass = inflate.findViewById(R.id.xpass);
        normalDialog.setPositiveButton("取消", null);
        normalDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String md5 = MD5Jm.getMD5(ypass.getText().toString());

                RequestBody body = new FormBody.Builder().add("action", "login").add("phone", phone).add("pass", md5).build();
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
                                if (bean.isLogin_result()) {
                                    String md5 = MD5Jm.getMD5(xpass.getText().toString());
                                    RequestBody body = new FormBody.Builder().add("action", "modifypass").add("phone", phone).add("pass", md5).build();
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
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(Main5Activity.this, "修改成功新密码：" + xpass.getText().toString(), Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                            }

                                        }
                                    });

                                } else {
                                    Toast.makeText(Main5Activity.this, "原密码错误", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });
            }
        });
        normalDialog.show();


    }
    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
