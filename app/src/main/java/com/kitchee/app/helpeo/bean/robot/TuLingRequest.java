package com.kitchee.app.helpeo.bean.robot;

/**
 * Created by Administrator on 2018/8/24.
 * desc:
 */


/*{
        "reqType":0,
        "perception": {
        "inputText": {
        "text": "附近的酒店"
        },
        "inputImage": {
        "url": "imageUrl"
        },
        "selfInfo": {
        "location": {
        "city": "北京",
        "province": "北京",
        "street": "信息路"
        }
        }
        },
        "userInfo": {
        "apiKey": "",
        "userId": ""
        }
        }*/
public class TuLingRequest {

    private int reqType;
    private perception perception;
    private userInfo userInfo;

    public TuLingRequest(){

    }

    public int getReqType() {
        return reqType;
    }

    public void setReqType(int reqType) {
        this.reqType = reqType;
    }

    public com.kitchee.app.helpeo.bean.robot.perception getPerception() {
        return perception;
    }

    public void setPerception(com.kitchee.app.helpeo.bean.robot.perception perception) {
        this.perception = perception;
    }

    public com.kitchee.app.helpeo.bean.robot.userInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(com.kitchee.app.helpeo.bean.robot.userInfo userInfo) {
        this.userInfo = userInfo;
    }
}
