package com.kitchee.app.helpeo.network;

import com.kitchee.app.helpeo.appCommon.Config;
import com.kitchee.app.helpeo.network.api.RobotResponseApi;
import com.kitchee.app.helpeo.network.api.Top250Api;
import com.kitchee.app.helpeo.network.api.ZhuangbiApi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kitchee on 2018/6/7.
 * desc :
 */

public class NetWork {
    private static ZhuangbiApi zhuangbiApi;
    private static Top250Api top250Api;
    private static RobotResponseApi robotResponseApi;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();

    public static ZhuangbiApi getZhuangbiApi(){

        if(zhuangbiApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://www.zhuangbi.info/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            zhuangbiApi = retrofit.create(ZhuangbiApi.class);
        }
        return zhuangbiApi;
    }

    public static Top250Api getTop250Api(){
        if(top250Api == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.douban.com/v2/movie/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            top250Api = retrofit.create(Top250Api.class);
        }
        return top250Api;
    }

    public static RobotResponseApi getRobotResponseApi(){
        if(robotResponseApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Config.URL_KEY)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            robotResponseApi = retrofit.create(RobotResponseApi.class);
        }
        return robotResponseApi;
    }



}
