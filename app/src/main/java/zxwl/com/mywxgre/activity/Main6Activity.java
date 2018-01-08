package zxwl.com.mywxgre.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import zxwl.com.mywxgre.R;

public class Main6Activity extends AppCompatActivity implements View.OnClickListener {


    /**
     * 你已经开通永久会员了
     */
    private TextView mName;
    /**
     * 添加QQ交流群
     */
    private Button mJqan;
    /**
     * 永久会员限时特价9.9
     */
    private TextView mVipts;
    /**
     * 支付
     */
    private Button mZfbzf;
    private WebView mWbv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        initView();

        Intent intent = getIntent();
        String vip = intent.getStringExtra("vip");

        if (vip.equals("VIP")) {
            mZfbzf.setVisibility(View.GONE);
            mVipts.setVisibility(View.GONE);
            mName.setVisibility(View.VISIBLE);
            mJqan.setVisibility(View.VISIBLE);
        }

    }


    private void initView() {
        mName = (TextView) findViewById(R.id.name);
        mJqan = (Button) findViewById(R.id.jqan);
        mJqan.setOnClickListener(this);
        mVipts = (TextView) findViewById(R.id.vipts);
        mZfbzf = (Button) findViewById(R.id.zfbzf);
        mZfbzf.setOnClickListener(this);
        mWbv = (WebView) findViewById(R.id.wbv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.jqan:
                joinQQGroup("s_kNTbp5gMdRUcNMmZFfgV_gPIZaIELE");
                break;
            case R.id.zfbzf:
                mWbv.loadUrl("http://www.zxwlwh.com/Wx/");
                if(mWbv.canGoBack()){
                    mWbv.goBack();
                }

                if(mWbv.canGoForward()){
                    mWbv.goForward();
                }

                mWbv.reload();
                break;
        }
    }

    public boolean joinQQGroup(String key) {

        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            Toast.makeText(this, "未安装手Q或安装的版本不支持", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}
