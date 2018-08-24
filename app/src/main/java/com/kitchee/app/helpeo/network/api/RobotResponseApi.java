package com.kitchee.app.helpeo.network.api;

import com.kitchee.app.helpeo.bean.ChatMessage;
import com.kitchee.app.helpeo.bean.robot.TuLingRequest;
import com.kitchee.app.helpeo.bean.robot.TuLingResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/8/24.
 * desc: 图灵机器人应答API
 */

public interface RobotResponseApi {
    @POST("v2")
    Observable<TuLingResponse> sendMessage(@Body TuLingRequest tuLingRequest);
}
