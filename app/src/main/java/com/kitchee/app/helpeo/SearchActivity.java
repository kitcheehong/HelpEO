package com.kitchee.app.helpeo;

import android.os.Bundle;
import android.widget.EditText;

import com.kitchee.app.helpeo.base.BaseActivity;
import com.kitchee.app.helpeo.utils.StatusBarUtils;

/**
 * Created by kitchee on 2018/6/5.
 * desc : 搜索页
 */

public class SearchActivity extends BaseActivity {

    EditText mSearchBGRlt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(true);
        setContentView(R.layout.activity_search);
        StatusBarUtils.setTranslucentStatusBar(this, false);
        mSearchBGRlt = (EditText) findViewById(R.id.et_search);
        float originY = getIntent().getIntExtra("y", 0);

        int location[] = new int[2];
        mSearchBGRlt.getLocationOnScreen(location);

        final float translateY = originY - (float) location[1];

//        frameBgHeight = frameBg.getHeight();

        //放到前一个页面的位置
        mSearchBGRlt.setY(mSearchBGRlt.getY() + translateY);
    }


}
