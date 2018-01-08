package zxwl.com.mywxgre.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import zxwl.com.mywxgre.Adapter.ListviewAdapter;
import zxwl.com.mywxgre.wxapi.Bbean;
import zxwl.com.mywxgre.R;
import zxwl.com.mywxgre.Utilis;
import zxwl.com.mywxgre.wxapi.Bean;

/**
 * Created by Administrator on 2018/1/4.
 */

public class FragmentTwo extends Fragment {
    private View view;
    /**
     * 用户名
     */
    private TextView mName;
    /**
     * 100个
     */
    private TextView mNum;
    private ListView mLs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_two, null);
        mName = (TextView) inflate.findViewById(R.id.name);
        mNum = (TextView) inflate.findViewById(R.id.num);
        mLs = (ListView) inflate.findViewById(R.id.ls);
        Bundle bundle = getArguments();
        final String phone = bundle.getString("phone");

        RequestBody body = new FormBody.Builder().add("action", "cid").add("phone", phone).build();
        Utilis.getInstance().sendPost("http://www.zxwlwh.com/Wx/WxGre.php", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Gson gson = new Gson();
                Bean bean = gson.fromJson(string, Bean.class);
                String id = bean.getId();
                RequestBody body = new FormBody.Builder().add("action", "tgzs").add("phone", id).build();
                Utilis.getInstance().sendPost("http://www.zxwlwh.com/Wx/WxGre.php", body, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        final String string = response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                Bbean bbean = gson.fromJson(string, Bbean.class);
                                List<Bbean.DatasBean> datas = bbean.getDatas();

                                mName.setText("邀请人数\n"+datas.size()+"人");
                                mNum.setText("获得奖励:\n"+datas.size()*3+"天体验会员");

                                ListviewAdapter listviewAdapter = new ListviewAdapter(datas, getContext());
                                mLs.setAdapter(listviewAdapter);

                            }
                        });


                    }
                });
            }
        });







        return inflate;

    }


}
