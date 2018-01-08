package zxwl.com.mywxgre.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import zxwl.com.mywxgre.Adapter.VpAdapter;
import zxwl.com.mywxgre.Fragment.FragmentOne;
import zxwl.com.mywxgre.Fragment.FragmentTwo;
import zxwl.com.mywxgre.R;

public class Main7Activity extends AppCompatActivity {

    private TabLayout mTab;
    private ViewPager mVp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        initView();
        mTab.setupWithViewPager(mVp);

        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        ArrayList<String> slist = new ArrayList<>();
        ArrayList<Fragment> list = new ArrayList<>();
        slist.add("邀请规则");
        slist.add("你的战绩");


        FragmentOne fragmentOne = new FragmentOne();
        FragmentTwo fragmentTwo = new FragmentTwo();
        Bundle bundle = new Bundle();
        bundle.putString("phone",phone);
        fragmentTwo.setArguments(bundle);
        fragmentOne.setArguments(bundle);

        list.add(fragmentOne);
        list.add(fragmentTwo);
        VpAdapter vpAdapter = new VpAdapter(getSupportFragmentManager(), list, slist);
        mVp.setAdapter(vpAdapter);

    }

    private void initView() {
        mTab = (TabLayout) findViewById(R.id.tab);
        mVp = (ViewPager) findViewById(R.id.vp);
    }
}
