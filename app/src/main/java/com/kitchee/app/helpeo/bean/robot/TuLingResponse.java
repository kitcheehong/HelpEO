package com.kitchee.app.helpeo.bean.robot;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/24.
 * desc:
 */

public class TuLingResponse {

    private intent intent;
    private ArrayList<Object> result;

    public TuLingResponse() {
    }

    public com.kitchee.app.helpeo.bean.robot.intent getIntent() {
        return intent;
    }

    public void setIntent(com.kitchee.app.helpeo.bean.robot.intent intent) {
        this.intent = intent;
    }

    public ArrayList getResult() {
        return result;
    }

    public void setResult(ArrayList result) {
        this.result = result;
    }
}
