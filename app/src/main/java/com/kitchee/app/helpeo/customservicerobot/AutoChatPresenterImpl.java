package com.kitchee.app.helpeo.customservicerobot;

import com.kitchee.app.helpeo.bean.ChatMessage;

/**
 * Created by Administrator on 2018/8/24.
 * desc:
 */

public class AutoChatPresenterImpl implements AutoChatPresenter{
    private AutoChatView chatView;
    private IAutoChatModel model;

    public AutoChatPresenterImpl(AutoChatView autoChatView){
        chatView = autoChatView;
        model = new AutoChatModelImpl();
    }

    @Override
    public void sendMessage(ChatMessage chatMessage) {
        model.sendMessage(chatMessage);
        chatView.sendMsgUpdateView(chatMessage);
    }

    @Override
    public ChatMessage receiveMessage() {
        return model.receiveMessage();
    }
}
