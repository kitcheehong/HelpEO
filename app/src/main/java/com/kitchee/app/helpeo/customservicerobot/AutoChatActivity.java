package com.kitchee.app.helpeo.customservicerobot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitchee.app.helpeo.R;
import com.kitchee.app.helpeo.appCommon.Config;
import com.kitchee.app.helpeo.bean.ChatMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kitchee on 2018/8/23.
 */

public class AutoChatActivity extends AppCompatActivity implements AutoChatView {


    @BindView(R.id.left_btn)
    ImageView leftBtn;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_line)
    View titleLine;
    @BindView(R.id.chatRecyclerView)
    RecyclerView recycleView;
    @BindView(R.id.chat_btn_send)
    Button chatBtnSend;
    @BindView(R.id.chat_et_content)
    EditText chatEtContent;

    AutoChatPresenter presenter;
    ChatMessageAdapter adapter;
    List<ChatMessage> list = new ArrayList<>();
    @BindView(R.id.chat_btn_voice)
    Button chatBtnVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_chat);
        ButterKnife.bind(this);

        ChatMessage chatMessage = new ChatMessage("客服小e", "你好，客服小e很高兴为你服务！", Config.MESSAGE_TYPE_RECEIVE, new Date());
        list.add(chatMessage);
        presenter = new AutoChatPresenterImpl(this);
        adapter = new ChatMessageAdapter(list, this, "", "");
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(adapter);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        titleTv.setText("客服小e");


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick({R.id.left_btn, R.id.chat_btn_send, R.id.chat_et_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_btn:
                onBackPressed();
                break;
            case R.id.chat_btn_send:
                Log.d("kitchee", "onViewClicked: 点击了发送");
                String message = chatEtContent.getText().toString().trim();
                if(!checkInputText(message)){
                    Toast.makeText(AutoChatActivity.this,"请输入你想知道的问题~",Toast.LENGTH_LONG).show();
                    return;
                };
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setName("kitchee");
                chatMessage.setType(Config.MESSAGE_TYPE_SEND);
                chatMessage.setDate(new Date());
                chatMessage.setMessage(message);
                Log.d("kitchee", "onViewClicked: message = " + message);
                presenter.sendMessage(chatMessage);
                chatEtContent.setText("");
                break;
            case R.id.chat_et_content:
                break;
        }
    }

    private boolean checkInputText(String message) {
        if(TextUtils.isEmpty(message)){
            return false;
        }
        return true;
    }

    //------------------------更新view-------------------------------//

    @Override
    public void sendMsgUpdateView(ChatMessage chatMessage) {
        //发送问题信息后更新当前页面
        Log.d("kitchee", "sendMsgUpdateView: ---------------");
        list.add(chatMessage);
        adapter.updateData(list);
        chatEtContent.setText("");
        recycleView.scrollToPosition(list.size() - 1);
    }

    @Override
    public void receiveMsgUpdateView(ChatMessage chatMessage) {
        Log.d("kitchee", "receiveMsgUpdateView: --------------");
        if (chatMessage != null){
//            list.add(chatMessage);
//            adapter.updateData(list);
            adapter.addData(chatMessage);
            recycleView.scrollToPosition(list.size() -1);

        }
    }

    @OnClick(R.id.chat_btn_voice)
    public void onViewClicked() {
        Log.d("kitchee", "onViewClicked: 点击了发送");

    }
}
