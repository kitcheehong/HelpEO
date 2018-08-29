package com.kitchee.app.helpeo.bean.robot;

/**
 * Created by Administrator on 2018/8/24.
 * desc:
 */
//        "code": 10005,
//                "intentName": "",
//                "actionName": "",
//                "parameters": {
//                "nearby_place": "酒店"
//                }
public class intent {

    public int code;
    public String intentName ;
    public String actionName;
    public  parameters parameters;


    @Override
    public String toString() {
        return "intent{" +
                "code=" + code +
                ", intentName='" + intentName + '\'' +
                ", actionName='" + actionName + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}

class parameters{}