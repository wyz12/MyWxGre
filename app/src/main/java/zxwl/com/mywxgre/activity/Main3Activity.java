package zxwl.com.mywxgre.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import zxwl.com.mywxgre.Bean;
import zxwl.com.mywxgre.R;
import zxwl.com.mywxgre.Utilis;
import zxwl.com.mywxgre.utils.MD5Jm;
import zxwl.com.mywxgre.utils.TimeUtlis;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 账号
     */
    private EditText mPhone;
    /**
     * 密码
     */
    private EditText mPass;
    /**
     * 注册
     */
    private Button mButton;
    /**
     * 请输入验证码
     */
    private EditText mYzm;
    /**
     * 获取验证码
     */
    private Button mHyyz;
    private int i=60;
    private boolean flag;
    private Handler hann = new Handler();
    private Runnable runn = new Runnable() {
        @Override
        public void run() {
            if(i>0){
                i--;
                mHyyz.setText("("+i+")秒");
                mHyyz.setEnabled(false);
                hann.postDelayed(runn,1000);
            }else{
                mHyyz.setText("重新获取");
                mHyyz.setEnabled(true);
            }

        }
    };
    private int eventSubmitVerificationCode;
    private int resultComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        initView();


    }

    private void initView() {
        mPhone = (EditText) findViewById(R.id.phone);
        mPass = (EditText) findViewById(R.id.pass);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);
        mYzm = (EditText) findViewById(R.id.yzm);
        mHyyz = (Button) findViewById(R.id.hyyz);
        mHyyz.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.button:
                boolean mobile = TimeUtlis.isMobile(mPhone.getText().toString());
                if (!mobile) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean password = TimeUtlis.isPassword(mPass.getText().toString());
                if (!password) {
                    Toast.makeText(this, "请正确输入密码大小英文+数字不能小于六位", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mYzm.length()==6){
                    submitCode("86",  mPhone.getText().toString(), mYzm.getText().toString());
                }else{
                    Toast.makeText(this, "请输入六位验证码", Toast.LENGTH_SHORT).show();
                    return;
                }





                break;

            case R.id.hyyz:
                if (mPhone.getText().length() == 11) {
                    SMSSDK.getVerificationCode("86", mPhone.getText().toString()); // 发送验证码给号码的 phoneNumber 的手机
                    Toast.makeText(this, "获取验证码成功", Toast.LENGTH_SHORT).show();
                    mYzm.requestFocus();
                }else{
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
                hann.postDelayed(runn,1000);
                break;
        }
    }

    private void zc() {
        String md5 = MD5Jm.getMD5(mPass.getText().toString());

        RequestBody body = new FormBody.Builder().add("action", "register").add("phone", mPhone.getText().toString()).add("pass", md5).build();
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
                        if (bean.isRegister_result()) {
                            Toast.makeText(Main3Activity.this, "恭喜你注册成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Main3Activity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }


    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
                                        public void afterEvent(int event, int result, Object data) {
                                            if (result == SMSSDK.RESULT_COMPLETE) {
                                                // TODO 处理验证成功的结果
                                                zc();

                                            } else{
                                                // TODO 处理错误的结果
                                                Toast.makeText(Main3Activity.this, "验证码错误", Toast.LENGTH_SHORT).show();



                                            }

                                            // 用完回调要注销，否则会造成泄露
                                            SMSSDK.unregisterEventHandler(this);
                                        }
                                    });
                // 触发操作
                SMSSDK.submitVerificationCode(country, phone, code);
    }
}
