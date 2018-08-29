package com.kitchee.app.helpeo.customservicerobot;

import com.kitchee.app.helpeo.bean.ChatMessage;

/**
 * Created by kitchee on 2018/8/25.
 * Desc:
 */

public interface OnRobotResponseListener {
    void onReceiverMessage(ChatMessage chatMessage);
}
