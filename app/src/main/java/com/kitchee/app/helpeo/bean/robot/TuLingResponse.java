package com.kitchee.app.helpeo.bean.robot;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/24.
 * desc:
 */

public class TuLingResponse {
    private Object emotion;
    private intent intent;
    private ArrayList<results> results;

    public TuLingResponse() {
    }

    public com.kitchee.app.helpeo.bean.robot.intent getIntent() {
        return intent;
    }

    public void setIntent(com.kitchee.app.helpeo.bean.robot.intent intent) {
        this.intent = intent;
    }

    public ArrayList getResults() {
        return results;
    }

    public void setResults(ArrayList result) {
        this.results = result;
    }

    @Override
    public String toString() {
        return "TuLingResponse{" +
                "emotion=" + emotion +
                ", intent=" + intent +
                ", results=" + results +
                '}';
    }
}
