package com.kitchee.app.helpeo.customservicerobot;

import com.iflytek.cloud.SpeechRecognizer;
import com.kitchee.app.helpeo.bean.ChatMessage;

/**
 * Created by Administrator on 2018/8/24.
 */

public interface AutoChatPresenter {

    void sendMessage(ChatMessage chatMessage);

    ChatMessage receiveMessage();

    void soundRecording(SpeechRecognizer speechRecognizer);

    void onStart();

    void onResume();

    void onPause();

    void onDestory();
}
