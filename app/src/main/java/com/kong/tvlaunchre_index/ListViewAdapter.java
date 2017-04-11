package com.kong.tvlaunchre_index;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kong on 2017/4/11.
 */

public class ListViewAdapter extends BaseAdapter {

    private List<MyBean> myBeanList;
    private Context mContext;

    public ListViewAdapter(Context mContext,List<MyBean> myBeanList) {
        this.myBeanList = myBeanList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return myBeanList.size();
    }

    @Override
    public MyBean getItem(int position) {
        return myBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position; 
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_layout,parent,false);
            holder = new ViewHolder();
            holder.tv_left = (TextView) convertView.findViewById(R.id.tv_left );
            holder.tv_right = (TextView) convertView.findViewById(R.id.tv_right );
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon );
            holder.ll_item_layout = (LinearLayout) convertView.findViewById(R.id.ll_item_layout); 
            convertView.setTag(holder);   //将Holder存储到convertView中
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_left.setText(getItem(position).left_text);
        holder.tv_right.setText(getItem(position).right_text);

        holder.ll_item_layout.setFocusable(true);
        
//        holder.ll_item_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                adapterOnClickListener.onClick(myBean, position);
//            }
//        });
        
//        holder.iv_icon.setBackgroundResource(mData.get(position).getaIcon());
        return convertView;
    }

    class ViewHolder{
        TextView tv_left;
        TextView tv_right;
        ImageView iv_icon;
        LinearLayout ll_item_layout;
    }

}
