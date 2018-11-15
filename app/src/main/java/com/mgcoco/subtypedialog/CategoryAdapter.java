package com.mgcoco.subtypedialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter<T extends BaseCategoryModel> extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<T> mData = new ArrayList<>();

    private View.OnClickListener mOnItemClickListener;

    private int mSeletedColor, mUnSelectedColor;

    private Context mContext;

    public CategoryAdapter(Context context){
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
        }
    }

    public void setData(List<T> data) {
        if(data == null)
            return;
        mData = data;
        notifyDataSetChanged();
    }

    public BaseCategoryModel getSelectedItem() {
        for(BaseCategoryModel data:mData){
            if(data.isPick())
                return data;
        }
        return null;
    }

    public void setSeletedColor(int color){
        mSeletedColor = color;
        notifyDataSetChanged();
    }

    public void setUnSelectedColor(int color){
        mUnSelectedColor = color;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(View.OnClickListener listener){
        mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_name, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.name.setText(mData.get(position).getName());
        holder.name.setTag(mData.get(position));

        if(mData.get(position).isPick())
            holder.name.setTextColor(mSeletedColor);
        else
            holder.name.setTextColor(mUnSelectedColor);

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPick(position);
                if(mOnItemClickListener != null)
                    mOnItemClickListener.onClick(v);
            }
        });
    }

    private boolean setPick(int position){
        for(int i = 0; i < mData.size(); i++){
            if(i != position) {
                mData.get(i).setPick(false);
            }
            else{
                mData.get(position).setPick(!mData.get(position).isPick());
            }
        }
        notifyDataSetChanged();
        return mData.get(position).isPick();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
