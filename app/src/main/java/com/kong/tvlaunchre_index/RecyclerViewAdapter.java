package com.kong.tvlaunchre_index;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kong on 2017/4/11.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private List<MyBean> myBeanList;

    interface AdapterOnClickListener {
        void onClick(MyBean myBean, int position);
    }

    AdapterOnClickListener adapterOnClickListener;

    public void setAdapterOnClickListener(AdapterOnClickListener adapterOnClickListener) {
        this.adapterOnClickListener = adapterOnClickListener;
    }


    public RecyclerViewAdapter(List<MyBean> myBeanList) {
        this.myBeanList = myBeanList;
    }

    public List<MyBean> getMyBeanList() {
        return this.myBeanList;
    }

    public int changeItem(MyBean myBean) {
        int i = this.myBeanList.indexOf(myBean);
        Log.i(TAG, " -=-=-=-=- changeItem: indexOf:" + this.myBeanList.indexOf(myBean));
        MyBean myBean1 = this.myBeanList.set(i, myBean);
        notifyItemChanged(i);
        Log.i(TAG, " -=-=-=-=- changeItem: myBean1:" + myBean1);
//        this.myBeanList.set()
        return i;
    }

    public void removeAllItem() {
        int count = this.myBeanList.size();
        this.myBeanList.removeAll(myBeanList);
        notifyItemRangeRemoved(0, count);
//        notifyDataSetChanged();
    }

    public void addItem(MyBean myBean) {
        this.myBeanList.add(myBean);
        notifyItemInserted(0);
//        notifyDataSetChanged();
    }

    public void addItem_(MyBean myBean) {
        for (int i = 0; i < myBeanList.size(); i++) {
            MyBean bean = myBeanList.get(i);
            if (bean.left_text.equals(myBean.tag)) {
                myBeanList.set(i, myBean);
                notifyItemChanged(i);
            } else {
                myBeanList.add(myBean);
                notifyItemInserted(myBeanList.size() + 1);
            }
        }
//        this.myBeanList.add(myBean);
//        notifyItemInserted(0);
    }

    public void removeItem(int position) {
//        int count = this.myBeanList.size();
        this.myBeanList.remove(position);
//        notifyItemRangeRemoved(0, count);
        notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MyBean myBean = myBeanList.get(position);
        holder.tv_left.setText(myBean.left_text);
        holder.tv_right.setText(myBean.right_text);
        holder.tv_right.setSelected(true);
        if (myBean.isIcon) {
            holder.iv_icon.setVisibility(View.VISIBLE);
        } else {
            holder.iv_icon.setVisibility(View.INVISIBLE);
        }
        holder.ll_item_layout.setFocusable(true);
//        holder.ll_item_layout.setFocusableInTouchMode(true);
        holder.ll_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterOnClickListener.onClick(myBean, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_left, tv_right;
        ImageView iv_icon;
        //        android.support.constraint.ConstraintLayout ll_item_layout;
        LinearLayout ll_item_layout;

        public ViewHolder(View itemView) {
            super(itemView);
//            ll_item_layout = (android.support.constraint.ConstraintLayout) itemView.findViewById(R.id.ll_item_layout);
            ll_item_layout = (LinearLayout) itemView.findViewById(R.id.ll_item_layout);
            tv_left = (TextView) itemView.findViewById(R.id.tv_left);
            tv_right = (TextView) itemView.findViewById(R.id.tv_right);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        }
    }

}
