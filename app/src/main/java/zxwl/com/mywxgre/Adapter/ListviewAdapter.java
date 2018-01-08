package zxwl.com.mywxgre.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import zxwl.com.mywxgre.wxapi.Bbean;
import zxwl.com.mywxgre.R;

/**
 * Created by Administrator on 2018/1/4.
 */

public class ListviewAdapter extends BaseAdapter {
   private List<Bbean.DatasBean> list;
   private Context context;

    public ListviewAdapter(List<Bbean.DatasBean> list, Context context) {
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
            hh=new HH();
            convertView = LayoutInflater.from(context).inflate(R.layout.ls_layout, null);
            hh.name = convertView.findViewById(R.id.name);
            hh.time = convertView.findViewById(R.id.time);

            convertView.setTag(hh);
        }else{
            hh = (HH) convertView.getTag();
        }
        hh.name.setText(list.get(position).getBphone());
        hh.time.setText(list.get(position).getTime());

        return convertView;
    }

    class HH{
        TextView name;
        TextView time;
    }
}
