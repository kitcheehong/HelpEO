package com.kitchee.app.helpeo.customservicerobot;

import android.util.Log;

import com.kitchee.app.helpeo.bean.ChatMessage;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/8/24.
 * desc:
 */

public class AutoChatPresenterImpl implements AutoChatPresenter,OnRobotResponseListener{
    private AutoChatView chatView;
    private IAutoChatModel model;

    public AutoChatPresenterImpl(AutoChatView autoChatView){
        chatView = autoChatView;
        model = new AutoChatModelImpl();

    }

    @Override
    public void sendMessage(ChatMessage chatMessage) {
        chatView.sendMsgUpdateView(chatMessage);
        model.sendMessage(chatMessage,this);

    }

    @Override
    public ChatMessage receiveMessage() {
        return model.receiveMessage();
    }

    @Override
    public void onReceiverMessage(ChatMessage chatMessage) {
        Log.d(TAG, "sendMessage: receiveMsg = "+ chatMessage);
        chatView.receiveMsgUpdateView(chatMessage);
    }
}
