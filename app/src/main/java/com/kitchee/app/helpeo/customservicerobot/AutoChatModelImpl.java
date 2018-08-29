package com.kitchee.app.helpeo.customservicerobot;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;



import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;
import com.kitchee.app.helpeo.appCommon.Config;
import com.kitchee.app.helpeo.bean.ChatMessage;
import com.kitchee.app.helpeo.bean.robot.TuLingRequest;
import com.kitchee.app.helpeo.bean.robot.TuLingResponse;
import com.kitchee.app.helpeo.bean.robot.inputText;
import com.kitchee.app.helpeo.bean.robot.perception;
import com.kitchee.app.helpeo.bean.robot.results;
import com.kitchee.app.helpeo.bean.robot.userInfo;
import com.kitchee.app.helpeo.network.NetWork;
import com.kitchee.app.helpeo.utils.JsonParser;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

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

    // 语音听写对象
    private SpeechRecognizer mIat;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 翻译是否开启
    private boolean mTranslateEnable = false;
    // 语音种类
    private String lag = "zh_cn";
    // 前端点超时
    private String frontTime = "5000";
    // 后端点超时
    private String backTime = "1500";
    // 是否有标点符号
    private String isHaveSign = "1";
    // 是否第一次设置参数
    private boolean isFirstSetting = true;



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
                        removeDisposble();
                    }
                });

    }

    @Override
    public ChatMessage receiveMessage() {
        return null;
    }

    @Override
    public void parseSoundRecord(RecognizerResult result,AutoChatPresenterImpl.parseListener listener) {
        // 录音开始解析

        String resultText = printResult(result);
        listener.onFinishParse(resultText);


    }

    @Override
    public void startSoundRecord(SpeechRecognizer speechRecognizer) {
        mIat = speechRecognizer;
        mIatResults.clear();
        if(isFirstSetting){
            setParam();
            isFirstSetting = false;
        }
    }

    private void removeDisposble(){
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
    private String printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        return resultBuffer.toString();
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        if( mTranslateEnable ){
            Log.i( TAG, "translate enable" );
            mIat.setParameter( SpeechConstant.ASR_SCH, "1" );
            mIat.setParameter( SpeechConstant.ADD_CAP, "translate" );
            mIat.setParameter( SpeechConstant.TRS_SRC, "its" );
        }

//        String lag = mSharedPreferences.getString("iat_language_preference",
//                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);

            if( mTranslateEnable ){
                mIat.setParameter( SpeechConstant.ORI_LANG, "en" );
                mIat.setParameter( SpeechConstant.TRANS_LANG, "cn" );
            }
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);

            if( mTranslateEnable ){
                mIat.setParameter( SpeechConstant.ORI_LANG, "cn" );
                mIat.setParameter( SpeechConstant.TRANS_LANG, "en" );
            }
        }
        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, frontTime);

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, backTime);

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, isHaveSign);

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }

}
