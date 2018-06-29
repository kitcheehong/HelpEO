package com.kitchee.app.helpeo.bean;

/**
 * Created by kitchee on 2018/6/28.
 * desc : 圆圈的属性
 */

public class CircleBean {

    public int id;
    public float x;
    public float y;
    public float radius;
    public int state = 0;//0:normal;1:selected;2:success;3:wrong

    public CircleBean(int id, float x, float y, int radius ) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }


    public boolean legal(float x , float y){
        float dx = this.x - x;
        float dy = this.y - y;
        return Math.sqrt((dx * dx + dy * dy)) <= radius;
    }
}
