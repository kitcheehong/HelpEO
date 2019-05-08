package com.kitchee.app.helpeo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kitchee.app.helpeo.base.BaseActivity;

import com.kitchee.app.helpeo.databinding.ActivityGuideBinding;
import com.kitchee.app.helpeo.login.LoginActivity;
import com.kitchee.app.helpeo.utils.CommonUtil;
import com.kitchee.app.helpeo.utils.PerfectClickListener;

import java.lang.ref.WeakReference;



/**
 * Created by kitchee on 2018/5/31.
 * desc : 引导页
 */

public class GuideActivity extends BaseActivity {

    public ActivityGuideBinding activityGuideBinding;
    String posterurl = "";
    MyHandler myHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全屏显示
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 不开启左右滑动
        setSwipeBackEnable(false);

        //第一：默认初始化
//        Bmob.initialize(this, "37f0a7bba95694ccdf24fdcce9bc7e51");
        // 数据绑定
        activityGuideBinding =  DataBindingUtil.setContentView(this,R.layout.activity_guide);

        // 后台返回时可能启动这个页面 http://blog.csdn.net/jianiuqi/article/details/54091181
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        showImage();
    }

    private void showImage() {

        // 先显示默认
        activityGuideBinding.ivDefaultShow.setImageDrawable(CommonUtil.getDrawable(R.mipmap.img_transition_default));
        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageDelayed(0,1000);
        myHandler.sendEmptyMessageDelayed(1,3000);
        //从网络上下载图片
        //查找Person表里面id为6b6c11c537的数据
//        BmobQuery<Poster> bmobQuery = new BmobQuery<Poster>();
//        bmobQuery.getObject("sb9HFFFX", new QueryListener<Poster>() {
//            @Override
//            public void done(Poster object,BmobException e) {
//                if(e==null){
////                    show("查询成功");
//                    posterurl=object.getPosterUrl();
//                    Log.d("kitchee", "done:posterUrl = "+ posterurl);
//                    showRandomPoster();
//                }else{
//                    show("查询失败：" + e.getMessage());
//                }
//            }
//        });

        posterurl="http://ojyz0c8un.bkt.clouddn.com/b_2.jpg";
        Log.d("kitchee", "done:posterUrl = "+ posterurl);
        showRandomPoster();

        activityGuideBinding.skip.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                toMainActivity();
            }
        });



    }

    private void toMainActivity() {
        if(myHandler != null && myHandler.hasMessages(1)){
            myHandler.removeMessages(1);
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.screen_zoom_in,R.anim.screen_zoom_out);
    }

    private void showRandomPoster() {
        Glide.with(this)
                .load(posterurl)
                .placeholder(R.mipmap.img_transition_default)
                .error(R.mipmap.img_transition_default)
                .into(activityGuideBinding.ivRandomShow);
    }

    private void  show( String msg){
        Toast.makeText(GuideActivity.this ,msg,Toast.LENGTH_LONG).show();
    }

    /**
     * 使用静态内部类以及弱引用解决Handler的内存泄漏
     */
    static class MyHandler extends Handler{

        private final WeakReference<GuideActivity> mActivity;

        private MyHandler(GuideActivity activity){
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 由于用了弱引用,在这里需要检查是否当前的activity还存在
            if (mActivity.get() == null) {
                return;
            }
            // 需要处理的事情
            doSomething(mActivity.get(),msg);
        }

        private void doSomething(GuideActivity guideActivity, Message msg) {
            switch (msg.what){
                case 0:
                    guideActivity.activityGuideBinding.ivDefaultShow.setVisibility(View.GONE);
                    break;
                case 1:
                    guideActivity.toMainActivity();
                    break;
                default:
                        break;
            }
        }
    }
}
