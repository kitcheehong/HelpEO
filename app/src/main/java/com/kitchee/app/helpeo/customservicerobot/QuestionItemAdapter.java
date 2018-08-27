package com.kitchee.app.helpeo.customservicerobot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kitchee.app.helpeo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kitchee on 2018/8/27.
 * desc:
 */

public class QuestionItemAdapter extends  RecyclerView.Adapter<QuestionItemAdapter.QuestionItem>{
    private static final String TAG = "QuestionItemAdapter";
    private List<String> list;
    private Context context;
    private ItemClickListener listener;
    public QuestionItemAdapter(ArrayList<String> list,Context context) {
        this.list = list == null? new ArrayList<String>() : list;
        this.context = context;
    }

    @Override
    public QuestionItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuestionItem(LayoutInflater.from(context).inflate(R.layout.list_item_question,null));
        //
//        return new QuestionItem(LayoutInflater.from(context).inflate(R.layout.list_item_question,null,false));
    }

    @Override
    public void onBindViewHolder(QuestionItem holder, final int position) {
        String item = list.get(position);
        Log.e(TAG, "onBindViewHolder: position = "+ position );
        holder.tvItem.setText(item);
        holder.tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onItemClick(view,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class QuestionItem extends RecyclerView.ViewHolder{

        private TextView tvItem;
        public QuestionItem(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.question_text);
        }
    }

    public interface ItemClickListener{
        void onItemClick(View view,int Pos);
    }

    public void setListener(ItemClickListener clickListener){
        this.listener = clickListener;
    }
}
