package me.kirkhorn.knut.android_sudoku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import me.kirkhorn.knut.android_sudoku.data.databaseBean;

public class MyAdapter extends BaseAdapter {
    private List<databaseBean> databaseBean;
    private Context context;

    public MyAdapter(List<databaseBean> databaseBean, Context context) {
        this.databaseBean = databaseBean;
        this.context = context;
    }

    @Override
    public int getCount() {
        return databaseBean.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
        }
        TextView tv1,tv2,tv3,tv4;
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        tv4 = view.findViewById(R.id.tv4);
        tv1.setText(String.valueOf(databaseBean.get(i).get_id()));
        tv2.setText(databaseBean.get(i).getName());
        tv3.setText(String.valueOf(databaseBean.get(i).getTime())+"秒");
        tv4.setText(databaseBean.get(i).getBroad());

        return view;
    }
}
