package zxwl.com.mywxgre.Fragment;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import zxwl.com.mywxgre.Adapter.ListimageAdapter;
import zxwl.com.mywxgre.R;
import zxwl.com.mywxgre.Utilis;
import zxwl.com.mywxgre.wxapi.BBBean;
import zxwl.com.mywxgre.wxapi.Bean;

/**
 * Created by Administrator on 2018/1/4.
 */

public class FragmentOne extends Fragment implements View.OnClickListener {

    private View view;
    /**
     * nihay\nsdas
     */
    private TextView mSharecode;
    /**
     * 复制
     */
    private Button mCopybutton;
    /**
     * 直接分享
     */
    private Button mShare;
    private String phone;
    private ListView mLs;
    private String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_one, null);
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(getActivity(), mPermissionList, 123);
        }

        Bundle bundle = getArguments();
        phone = bundle.getString("phone");





        initView(inflate);
        zs();
        hqidz();






        return inflate;
    }

    private void hqidz() {
        RequestBody body1 = new FormBody.Builder().add("action", "cid").add("phone", phone).build();
        Utilis.getInstance().sendPost("http://www.zxwlwh.com/Wx/WxGre.php", body1, new Callback() {



            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Gson gson = new Gson();
                Bean bean = gson.fromJson(string, Bean.class);
                id = bean.getId();
                Log.e("TTT", id);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSharecode.setText("自动抢红包,不错过任何一个红包,注册填写邀请码:" + id + "免费送会员,点击下方地址立即体验：\nhttp://www.zxwlwh.com/Wxx/?id="+id);

                    }
                });

            }
        });
    }

    private void zs() {
        RequestBody body = new FormBody.Builder().add("action", "yqjs").build();
        Utilis.getInstance().sendPost("http://www.zxwlwh.com/Wx/WxGre.php", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String string = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();
                        BBBean bbBean = gson.fromJson(string, BBBean.class);
                        List<BBBean.DatasBean> datas = bbBean.getDatas();
                        ListimageAdapter listimageAdapter = new ListimageAdapter(datas, getContext());
                        mLs.setAdapter(listimageAdapter);
                    }
                });


            }
        });
    }


    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.e("TTT", requestCode + "");

    }

    private void initView(View inflate) {
        mSharecode = (TextView) inflate.findViewById(R.id.sharecode);
        mCopybutton = (Button) inflate.findViewById(R.id.copybutton);
        mCopybutton.setOnClickListener(this);
        mShare = (Button) inflate.findViewById(R.id.share);
        mShare.setOnClickListener(this);
        mLs = (ListView) inflate.findViewById(R.id.ls);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.copybutton:

                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(mSharecode.getText());
                Toast.makeText(getContext(), "复制成功，可以发给朋友们了。", Toast.LENGTH_LONG).show();

                break;
            case R.id.share:
                String url = "http://www.zxwlwh.com/Wxx/?id="+id;
                UMWeb web = new UMWeb(url);
                web.setTitle("AITool");
                UMImage image = new UMImage(getContext(), R.drawable.logo);
                web.setThumb(image);  //缩略图
                web.setDescription("自动抢红包,不错过任何一个红包,快来一起体验一下,注册填写邀请人:【" + id + "】免费得会员");
                new ShareAction(getActivity())
                        .withMedia(web)
                        .setDisplayList(SHARE_MEDIA.QQ)
                        .setCallback(shareListener)
                        .open();
                break;
        }
    }


    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(getContext(), "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getContext(), "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getContext(), "取消了", Toast.LENGTH_LONG).show();

        }
    };
}
