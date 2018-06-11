package com.kitchee.app.helpeo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kitchee.app.helpeo.bean.ZhuangbiImg;
import com.kitchee.app.helpeo.testRxJava.ZhuangbiAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kitchee on 2018/6/11.
 * desc :
 */

public class LabelItemAdapter extends RecyclerView.Adapter {


    List<ZhuangbiImg> imgList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.label_grid_item,parent,false);
        return new ZBViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ZBViewHolder imgViewHolder = (ZBViewHolder) holder;
        ZhuangbiImg zhuangbiImg = imgList.get(position);
        imgViewHolder.descriptionTv.setText(zhuangbiImg.description);
        Glide.with(imgViewHolder.itemView.getContext()).load(zhuangbiImg.image_url).into(imgViewHolder.imageIv);
    }

    @Override
    public int getItemCount() {
        return imgList == null ? 0 : imgList.size();
    }
    public void setImages(List<ZhuangbiImg> images) {
        this.imgList = images;
        notifyDataSetChanged();
    }

    class ZBViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_item)
        ImageView imageIv;
        @BindView(R.id.tv_item)
        TextView descriptionTv;
        public ZBViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }



}
