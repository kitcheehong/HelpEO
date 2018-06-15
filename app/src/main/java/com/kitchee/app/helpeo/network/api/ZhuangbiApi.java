package com.kitchee.app.helpeo.network.api;

import android.graphics.Movie;

import com.kitchee.app.helpeo.bean.ZhuangbiImg;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kitchee on 2018/6/7.
 * desc :
 */

public interface ZhuangbiApi {
    @GET("search")
    Observable<List<ZhuangbiImg>>search(@Query("q") String query);

}
