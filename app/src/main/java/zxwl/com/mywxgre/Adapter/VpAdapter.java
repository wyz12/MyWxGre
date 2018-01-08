package zxwl.com.mywxgre.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/4.
 */

public class VpAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> list;
    private ArrayList<String> wz;

    public VpAdapter(FragmentManager fm, ArrayList<Fragment> list, ArrayList<String> wz) {
        super(fm);
        this.list = list;
        this.wz = wz;
    }

    public VpAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return wz.get(position);
    }
}
