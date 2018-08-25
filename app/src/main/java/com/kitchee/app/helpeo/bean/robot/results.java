package com.kitchee.app.helpeo.bean.robot;

/**
 * Created by kitchee on 2018/8/25.
 */

public class results {
//             "groupType":1,
//                    "resultType":"text",
//                    "values":
//
//    {
//        "text":"亲，已帮你找到相关酒店信息"
//    }

    public int groupType;
    public String resultType;
    public values values;

    @Override
    public String toString() {
        return "results{" +
                "groupType=" + groupType +
                ", resultType='" + resultType + '\'' +
                ", values=" + values +
                '}';
    }

}


