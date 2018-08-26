package com.kitchee.app.helpeo.customservicerobot;

import com.iflytek.cloud.RecognizerResult;

/**
 * Created by kitchee on 2018/8/26.
 * Desc:
 */

public interface OnRecordingListener {
    void onRecordFinish(RecognizerResult results);
}
