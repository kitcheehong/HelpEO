package com.kitchee.app.helpeo.testRxJava;

import android.app.Activity;
import android.widget.Toast;

import com.kitchee.app.helpeo.bean.ZhuangbiImg;
import com.kitchee.app.helpeo.network.NetWork;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kitchee on 2018/6/7.
 * desc : MVVM -VM
 */

public class MyHandler {

    protected Disposable disposable;
    private ZhuangbiAdapter adapter;
    private Activity activity;
    public MyHandler(Activity activity){
        this.activity = activity;
    }

    /**开始加载数据**/
    public void onClickStart(){
        search("可爱");
    }
    /**取消加载数据**/
    public void onClickCancel(){
        removeDisposble();
    }

    public void removeDisposble(){
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private void search(String key){
        disposable = NetWork.getZhuangbiApi()
                .search(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ZhuangbiImg>>() {
                    @Override
                    public void accept(List<ZhuangbiImg> zhuangbiImgs) throws Exception {
                        Logger.d(zhuangbiImgs);
                        adapter.setImages(zhuangbiImgs);
                        removeDisposble();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(activity, "数据加载失败", Toast.LENGTH_SHORT).show();
                    }
                });

//        disposable = NetWork.getTop250Api().getTopMovie(0,10)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<HotMovieBean>() {
//                    @Override
//                    public void accept(HotMovieBean movie) throws Exception {
//                        Logger.d(movie.getTitle());
//                        Logger.d(movie.getCount());
//                        Logger.d(movie.getTotal());
//                        Logger.d(movie.getSubjects());
//                        Logger.d(movie.getSubjects().toArray());
//
//                        removeDisposble();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Toast.makeText(activity, "数据加载失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }


    public void setAdapter(ZhuangbiAdapter adapter){
        this.adapter = adapter;
    }
}
