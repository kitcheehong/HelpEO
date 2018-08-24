package com.kitchee.app.helpeo.customservicerobot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.kitchee.app.helpeo.R;
import com.kitchee.app.helpeo.appCommon.Config;
import com.kitchee.app.helpeo.appCommon.GlideImageLoader;
import com.kitchee.app.helpeo.bean.ChatMessage;
import com.kitchee.app.helpeo.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kitchee on 2018/8/24.
 * desc:
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatHolder> {

    private List<ChatMessage> list;
    private Context context;
    private String uIcon;    // 对方的头像地址
    private String myIcon;    // 我的头像地址
    private String uid;        // 对方uid

    public ChatMessageAdapter(List<ChatMessage> list, Context context, String uIcon, String myIcon) {
        this.list = list;
        this.context = context;
        this.uIcon = uIcon;
        this.myIcon = myIcon;
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatHolder(LayoutInflater.from(context).inflate(R.layout.widget_list_item_chat_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        if (holder instanceof ChatHolder) {

            ChatMessage chatMessage = list.get(position);
            int type = chatMessage.getType();
            String dateTime = DateUtil.format(chatMessage.getDate().getTime(),DateUtil.DATE_FORMAT_YMD_HMS);
            ( holder).tvTime.setText(dateTime);

            if (type == Config.MESSAGE_TYPE_RECEIVE) {
                // 接收的消息
                ( holder).layoutMy.setVisibility(View.GONE);
                ( holder).layoutSender.setVisibility(View.VISIBLE);
                ( holder).tvSenderMessage.setText(chatMessage.getMessage());
                ( holder).imgSendHead.setImageResource(android.R.mipmap.sym_def_app_icon);
            } else {
                // 发送出去的消息
                ( holder).layoutSender.setVisibility(View.GONE);
                ( holder).layoutMy.setVisibility(View.VISIBLE);
                ( holder).tvMyMessage.setText(chatMessage.getMessage());

                ( holder).imgMyHead.setBackgroundResource(0);
                ( holder).imgMyHead.setImageResource(0);

                ( holder).imgMyHead.setImageResource(R.mipmap.kefu);
//                ImageLoaderUtils.displayImage(myIcon, ( holder).imgMyHead, null);
//                new GlideImageLoader().displayImage(context,uIcon,( holder).imgMyHead);
            }
            holder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateData(List<ChatMessage> datas){
        if (datas == null){
            datas = new ArrayList<>();
        }
        this.list = datas;
    }

    class ChatHolder extends RecyclerView.ViewHolder {
        public ImageView imgSendHead;
        public TextView tvSenderMessage;
        public RelativeLayout layoutSender;

        public TextView tvTime;

        public ImageView imgMyHead;
        public TextView tvMyMessage;
        public RelativeLayout layoutMy;

        public ChatHolder(View itemView) {
            super(itemView);
            imgSendHead = (ImageView) itemView.findViewById(R.id.img_chat_sender_head);
            tvSenderMessage = (TextView) itemView.findViewById(R.id.tv_chat_sender_message);
            tvTime = (TextView) itemView.findViewById(R.id.tv_chat_time);
            layoutSender = (RelativeLayout) itemView.findViewById(R.id.layout_chat_sender);

            imgMyHead = (ImageView) itemView.findViewById(R.id.img_chat_my_head);
            tvMyMessage = (TextView) itemView.findViewById(R.id.tv_chat_my_message);
            layoutMy = (RelativeLayout) itemView.findViewById(R.id.layout_chat_my);

        }
    }


}
