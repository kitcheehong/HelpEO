package com.kitchee.app.helpeo.customservicerobot;

import android.util.Log;

import com.kitchee.app.helpeo.appCommon.Config;
import com.kitchee.app.helpeo.bean.ChatMessage;
import com.kitchee.app.helpeo.bean.robot.TuLingRequest;
import com.kitchee.app.helpeo.bean.robot.TuLingResponse;
import com.kitchee.app.helpeo.bean.robot.inputText;
import com.kitchee.app.helpeo.bean.robot.perception;
import com.kitchee.app.helpeo.bean.robot.userInfo;
import com.kitchee.app.helpeo.network.NetWork;


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
    @Override
    public void sendMessage(ChatMessage chatMessage) {
        TuLingRequest tuLingRequest = new TuLingRequest();

        inputText inputText = new inputText();
        inputText.text = chatMessage.getMessage();
        perception perception = new perception();
        perception.inputText = inputText;
        userInfo userInfo = new userInfo();
        userInfo.apiKey = Config.APP_KEY;
        userInfo.userId = "18888";
        tuLingRequest.setPerception(perception);
        tuLingRequest.setReqType(Config.MESSAGE_TYPE_SEND);
        tuLingRequest.setUserInfo(userInfo);
        Log.d(TAG, "sendMessage: 已经开始发送请求");
        disposable = NetWork.getRobotResponseApi().sendMessage(tuLingRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TuLingResponse>() {
                    @Override
                    public void accept(TuLingResponse tuLingResponse) throws Exception {
                        Log.d(TAG, "accept: tuLingResponse = "+ tuLingResponse.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    @Override
    public ChatMessage receiveMessage() {
        return null;
    }
}
