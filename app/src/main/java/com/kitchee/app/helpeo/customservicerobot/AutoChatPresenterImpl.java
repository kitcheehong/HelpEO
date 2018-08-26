package com.kitchee.app.helpeo.customservicerobot;

import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;
import com.kitchee.app.helpeo.bean.ChatMessage;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/8/24.
 * desc:
 */

public class AutoChatPresenterImpl implements AutoChatPresenter, OnRobotResponseListener, RecognizerListener {
    private AutoChatView chatView;
    private IAutoChatModel model;

    public AutoChatPresenterImpl(AutoChatView autoChatView) {
        chatView = autoChatView;
        model = new AutoChatModelImpl();

    }

    @Override
    public void sendMessage(ChatMessage chatMessage) {
        chatView.sendMsgUpdateView(chatMessage);
        model.sendMessage(chatMessage, this);

    }

    @Override
    public ChatMessage receiveMessage() {
        return model.receiveMessage();
    }

    @Override
    public void soundRecording(SpeechRecognizer speechRecognizer) {
        // 开始录音，并弹出收音dialog

        model.startSoundRecord(speechRecognizer);
        chatView.showRecognizerDialog(mRecognizerDialogListener);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestory() {

    }

    //-------------------------OnRecordingListener--------------------------//
    @Override
    public void onReceiverMessage(ChatMessage chatMessage) {
        Log.d(TAG, "sendMessage: receiveMsg = " + chatMessage);
        chatView.receiveMsgUpdateView(chatMessage);
    }

    //-------------------------RecognizerListener的回调-----------------------//
    @Override
    public void onVolumeChanged(int i, byte[] bytes) {
        // 此回调表示：当前音量的变化
    }

    @Override
    public void onBeginOfSpeech() {
        // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
    }

    @Override
    public void onEndOfSpeech() {
        // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
    }

    @Override
    public void onResult(RecognizerResult recognizerResult, boolean b) {
        // 此回调表示：语音识别结果
    }

    @Override
    public void onError(SpeechError speechError) {
        // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {
        // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
        // 若使用本地能力，会话id为null
    }


    //----------------------------RecognizerDialogListener-------------------------//
    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, final boolean isLast) {
            Log.d("kitchee", "onResult: 录音结果------------------------");
            RecognizerResult recognizerResult;

            model.parseSoundRecord(results, new parseListener() {
                @Override
                public void onFinishParse(String record) {
                    Log.d("kitchee", "onFinishParse: 识别结果---------------------");
                    if(isLast){
                        chatView.showRecognizeRecord(record);
                    }
                }
            });


        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {

        }

    };

    public interface parseListener {
        void onFinishParse(String record);
    }
}
