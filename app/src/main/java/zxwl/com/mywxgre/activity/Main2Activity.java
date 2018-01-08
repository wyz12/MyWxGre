package zxwl.com.mywxgre.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import zxwl.com.mywxgre.utils.MD5Jm;
import zxwl.com.mywxgre.wxapi.Bean;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {


    /**
     * 账号
     */
    private EditText mPhone;
    /**
     * 密码
     */
    private EditText mPass;
    /**
     * 登录
     */
    private Button mLogin;
    /**
     * 注册
     */
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        SharedPreferences preferences=getSharedPreferences("SP", Context.MODE_PRIVATE);
        String phone = preferences.getString("phone", "");
        if(!phone.equals("")){
            Intent intent = new Intent(Main2Activity.this, Main4Activity.class);
            intent.putExtra("phone",phone);
            startActivity(intent);
            finish();
        }

    }

    private void initView() {
        mPhone = (EditText) findViewById(R.id.phone);
        mPass = (EditText) findViewById(R.id.pass);
        mLogin = (Button) findViewById(R.id.login);
        mLogin.setOnClickListener(this);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.login:
                String md5 = MD5Jm.getMD5(mPass.getText().toString());
                RequestBody body1 = new FormBody.Builder().add("action","login").add("phone",mPhone.getText().toString()).add("pass",md5).build();
                Utilis.getInstance().sendPost("http://www.zxwlwh.com/Wx/WxGre.php", body1, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String string = response.body().string();
                        Log.e("TTT",string);
                        Log.e("TTT",mPass.getText().toString());
                        Log.e("TTT",mPhone.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Gson gson = new Gson();
                                Bean bean = gson.fromJson(string, Bean.class);
                                if(bean.isLogin_result()){
                                    cx();
                                }else{
                                    Toast.makeText(Main2Activity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


                    }
                });
                break;
            case R.id.button:
                startActivity(new Intent(Main2Activity.this,Main3Activity.class));
                break;
        }
    }

    private void cx() {
        RequestBody body = new FormBody.Builder().add("action","total").add("phone",mPhone.getText().toString()).build();
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

                            SharedPreferences sp = getSharedPreferences("SP", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("phone", mPhone.getText().toString());
                            editor.commit();
                             Log.e("TTT",bean.getLogin());
                            if(bean.getLogin().equals("0")){
                                cr(1);
                            }else{
                                cr(0);
                            }

                            Intent intent = new Intent(Main2Activity.this, Main4Activity.class);
                            intent.putExtra("phone",mPhone.getText().toString());
                            startActivity(intent);
                            finish();

                    }
                });

            }
        });
    }


    private void cr(final int ss){
        RequestBody body = new FormBody.Builder().add("action","modifylogin").add("phone",mPhone.getText().toString()).add("login",""+ss).build();
        Utilis.getInstance().sendPost("http://www.zxwlwh.com/Wx/WxGre.php", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SharedPreferences sp = getSharedPreferences("SP", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if("1".equals(ss+"")){
                    editor.putString("login", "1");
                }else{
                    editor.putString("login", "0");
                }

                editor.commit();
            }
        });
    }



}
