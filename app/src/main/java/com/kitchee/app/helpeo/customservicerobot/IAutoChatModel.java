package com.kitchee.app.helpeo.customservicerobot;

import com.kitchee.app.helpeo.bean.ChatMessage;

/**
 * Created by Administrator on 2018/8/24.
 * desc:
 */

public interface IAutoChatModel {

    public void sendMessage(ChatMessage chatMessage);

    public ChatMessage receiveMessage();
}
