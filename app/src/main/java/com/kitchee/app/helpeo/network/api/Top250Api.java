package com.kitchee.app.helpeo.network.api;

import android.graphics.Movie;

import com.kitchee.app.helpeo.bean.HotMovieBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kitchee on 2018/6/15.
 * desc :
 */

public interface Top250Api {
    @GET("top250")
     Observable<HotMovieBean> getTopMovie(@Query("start") int start, @Query("count") int count);
}
