package zxwl.com.mywxgre.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import zxwl.com.mywxgre.R;
import zxwl.com.mywxgre.wxapi.BBBean;

/**
 * Created by Administrator on 2018/1/5.
 */

public class ListimageAdapter extends BaseAdapter {
    private List<BBBean.DatasBean> list;
    private Context context;

    public ListimageAdapter(List<BBBean.DatasBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       HH hh;
        if(convertView==null){
          hh = new HH();
            convertView = LayoutInflater.from(context).inflate(R.layout.ls_image_layout, null);
            hh.name = convertView.findViewById(R.id.image);
            convertView.setTag(hh);
        }else{
            hh = (HH) convertView.getTag();
        }

        Glide.with(context).load(list.get(position).getImage()).into(hh.name);

        return convertView;
    }

    class HH{
        ImageView name;
    }
}
