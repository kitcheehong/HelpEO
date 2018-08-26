package com.kitchee.app.helpeo.customservicerobot;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import com.kitchee.app.helpeo.bean.ChatMessage;

/**
 * Created by Administrator on 2018/8/24.
 * desc:
 */

public interface IAutoChatModel {

    public void sendMessage(ChatMessage chatMessage,OnRobotResponseListener listener);

    public ChatMessage receiveMessage();

    public void parseSoundRecord(RecognizerResult recognizerResult, AutoChatPresenterImpl.parseListener parseListener);

    public void startSoundRecord(SpeechRecognizer speechRecognizer);
}
