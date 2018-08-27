package com.kitchee.app.helpeo.customservicerobot;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.kitchee.app.helpeo.R;
import com.kitchee.app.helpeo.appCommon.Config;

import com.kitchee.app.helpeo.bean.ChatMessage;
import com.kitchee.app.helpeo.utils.DateUtil;

import java.util.ArrayList;
import java.util.Date;
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
            holder.tvTime.setText(dateTime);

            if (type == Config.MESSAGE_TYPE_RECEIVE) {
                // 接收的消息
                holder.layoutMy.setVisibility(View.GONE);
                holder.layoutSender.setVisibility(View.VISIBLE);
                holder.tvSenderMessage.setText(chatMessage.getMessage());
                holder.imgSendHead.setImageResource(R.mipmap.kefu);
            } else {
                // 发送出去的消息
                holder.layoutSender.setVisibility(View.GONE);
                holder.layoutMy.setVisibility(View.VISIBLE);
                holder.tvMyMessage.setText(chatMessage.getMessage());

                holder.imgMyHead.setBackgroundResource(0);
                holder.imgMyHead.setImageResource(0);

                holder.imgMyHead.setImageResource(R.mipmap.ic_launcher_round);
//                ImageLoaderUtils.displayImage(myIcon, holder.imgMyHead, null);
//                new GlideImageLoader().displayImage(context,uIcon,holder.imgMyHead);
            }

            if(position == 0){
                View view = holder.viewStub.inflate();
                initViewStub(view);
            }else{
                holder.viewStub.setVisibility(View.GONE);
            }

            holder.itemView.setTag(position);
        }
    }

    private void initViewStub(View view) {
        ArrayList<String> list = new ArrayList<>();
        list.add("1.冠众电子科技总部在哪里？");
        list.add("2.冠众电子科技有限公司有那些类型的产品");
        list.add("3.冠众电子科技股份有限公司分支机构有那些");
        list.add("4.冠众电子科技股份有限公司产品软件系统升级");
        list.add("5.冠众电子客服电话？");
        list.add("6.冠众电子介绍");
        QuestionItemAdapter.ItemClickListener listener= new QuestionItemAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int Pos) {
                ChatMessage chatMessage = new ChatMessage("客服","你说什么我不太明白，你可以拨打人工客服电话：400-101-8833",Config.MESSAGE_TYPE_RECEIVE,new Date());
                addData(chatMessage);
            }
        };
        QuestionItemAdapter  adapter = new QuestionItemAdapter(list,context);
        adapter.setListener(listener);
        RecyclerView cv = view.findViewById(R.id.question_list_item);
        cv.setLayoutManager(new LinearLayoutManager(context));
        cv.setAdapter(adapter);
        cv.setItemAnimator(new DefaultItemAnimator());



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
        notifyDataSetChanged();
    }

    public void addData(ChatMessage chatMessage){
        if (chatMessage != null){
            this.list.add(chatMessage);
        }
        notifyItemInserted(list.size() - 1);
    }

    class ChatHolder extends RecyclerView.ViewHolder {
        ImageView imgSendHead;
        TextView tvSenderMessage;
        RelativeLayout layoutSender;

        TextView tvTime;

        ImageView imgMyHead;
        TextView tvMyMessage;
        RelativeLayout layoutMy;

        ViewStub viewStub;

        public ChatHolder(View itemView) {
            super(itemView);
            imgSendHead = itemView.findViewById(R.id.img_chat_sender_head);
            tvSenderMessage =  itemView.findViewById(R.id.tv_chat_sender_message);
            tvTime = itemView.findViewById(R.id.tv_chat_time);
            layoutSender = itemView.findViewById(R.id.layout_chat_sender);

            imgMyHead = itemView.findViewById(R.id.img_chat_my_head);
            tvMyMessage = itemView.findViewById(R.id.tv_chat_my_message);
            layoutMy = itemView.findViewById(R.id.layout_chat_my);

            viewStub = itemView.findViewById(R.id.viewStub);

        }
    }




}
