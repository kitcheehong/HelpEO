package com.kitchee.app.helpeo.customservicerobot;

import android.util.Log;

import com.kitchee.app.helpeo.appCommon.Config;
import com.kitchee.app.helpeo.bean.ChatMessage;
import com.kitchee.app.helpeo.bean.robot.TuLingRequest;
import com.kitchee.app.helpeo.bean.robot.TuLingResponse;
import com.kitchee.app.helpeo.bean.robot.inputText;
import com.kitchee.app.helpeo.bean.robot.perception;
import com.kitchee.app.helpeo.bean.robot.results;
import com.kitchee.app.helpeo.bean.robot.userInfo;
import com.kitchee.app.helpeo.network.NetWork;


import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by Administrator on 2018/8/24.
 * desc:
 */

public class AutoChatModelImpl implements IAutoChatModel {
    protected Disposable disposable;
    private ChatMessageAdapter adapter;

    @Override
    public void sendMessage(ChatMessage chatMessage, final OnRobotResponseListener listener) {
        TuLingRequest tuLingRequest = new TuLingRequest();

        inputText inputText = new inputText();
        inputText.text = chatMessage.getMessage();
        perception perception = new perception();
        perception.inputText = inputText;
        userInfo userInfo = new userInfo();
        userInfo.apiKey = Config.APP_KEY;
        userInfo.userId = "18888";
        tuLingRequest.setPerception(perception);
        tuLingRequest.setReqType(0);
        tuLingRequest.setUserInfo(userInfo);
        Log.d(TAG, "sendMessage: 已经开始发送请求---tuLingRequest = "+ tuLingRequest.toString());
        disposable = NetWork.getRobotResponseApi().sendMessage(tuLingRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object tuLingResponse) throws Exception {
                        int code  = ((TuLingResponse)tuLingResponse).getIntent().code;
                        Log.d("kitchee","code = "+ code+"result = "+ ((TuLingResponse)tuLingResponse).getResults().get(0).toString());

                        Log.d(TAG, "accept: tuLingResponse = "+ tuLingResponse.toString());

                        results result = (com.kitchee.app.helpeo.bean.robot.results) ((TuLingResponse)tuLingResponse).getResults().get(0);
                        String message = (result.values).text;
                        ChatMessage receiveMsg = new ChatMessage("我",message,Config.MESSAGE_TYPE_RECEIVE,new Date());

                        listener.onReceiverMessage(receiveMsg);

                        removeDisposble();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "accept: 响应异常"+throwable.getMessage());
                    }
                });

    }

    @Override
    public ChatMessage receiveMessage() {
        return null;
    }

    private void removeDisposble(){
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void setAdapter(ChatMessageAdapter adapter){
        this.adapter = adapter;
    }
}
