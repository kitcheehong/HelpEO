package com.kitchee.app.helpeo.testRxJava;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kitchee.app.helpeo.R;
import com.kitchee.app.helpeo.bean.ZhuangbiImg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kitchee on 2018/6/7.
 * desc :
 */

public class ZhuangbiAdapter extends RecyclerView.Adapter {
    List<ZhuangbiImg> imgList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
        return new IMGViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IMGViewHolder imgViewHolder = (IMGViewHolder) holder;
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

    class IMGViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.imageIv)
        ImageView imageIv;
        @BindView(R.id.descriptionTv)
        TextView descriptionTv;
        public IMGViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
