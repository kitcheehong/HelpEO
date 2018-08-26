package com.kitchee.app.helpeo.customservicerobot;

import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.kitchee.app.helpeo.bean.ChatMessage;

/**
 * Created by Administrator on 2018/8/24.
 * desc:
 */

public interface AutoChatView {

    void sendMsgUpdateView(ChatMessage chatMessage);

    void receiveMsgUpdateView(ChatMessage chatMessage);

    void showRecognizerDialog(RecognizerDialogListener listener);

    void showRecognizeRecord(String record);

    void showToast(String message);

}
