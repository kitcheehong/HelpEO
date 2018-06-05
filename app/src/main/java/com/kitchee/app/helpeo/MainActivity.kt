package com.kitchee.app.helpeo

import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import com.kitchee.app.helpeo.appCommon.GlideImageLoader
import com.kitchee.app.helpeo.appCommon.HelpEOApplication
import com.kitchee.app.helpeo.base.BaseActivity
import com.kitchee.app.helpeo.bean.User
import com.kitchee.app.helpeo.utils.DisplayUtil
import com.kitchee.app.helpeo.utils.StatusBarUtils
import com.youth.banner.Banner
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.HotCity
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity


class MainActivity : BaseActivity() {
    private val imageUrls = arrayOf("http://www.fdhao.cn/data/afficheimg/1495390513544727777.jpg", "http://www.fdhao.cn/data/afficheimg/1495589062250157826.jpg", "http://www.fdhao.cn/data/afficheimg/1494812104978168405.jpg", "http://www.fdhao.cn/data/afficheimg/1494981105456344186.jpg", "http://www.fdhao.cn/data/afficheimg/1495390183732065802.jpg", "http://www.fdhao.cn/data/afficheimg/1495409860833294008.jpg", "http://www.fdhao.cn/data/afficheimg/1494812581931179021.jpg", "http://www.fdhao.cn/data/afficheimg/1495588873555624520.jpg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView: TextView? = findViewById(R.id.tv_show) as TextView?
        val tvCityPick:TextView? = findViewById(R.id.addr_sel) as TextView?
        setSwipeBackEnable(true)

        StatusBarUtils.setTranslucentStatusBar(this, false)
//        val statusBarHeight = DisplayUtil.getStatusBarHeight(this)

        val banner = findViewById(R.id.banner) as Banner
        //设置图片加载器
        banner.setImageLoader(GlideImageLoader())
        //设置图片集合
        banner.setImages(imageUrls.asList())
        //banner设置方法全部调用完毕时最后调用
        banner.start()

//       val user = User(2,"martin","",32,1,System.currentTimeMillis(),System.currentTimeMillis())
//
//        user.save(object: SaveListener<String>() {
//            override fun done(p0: String?, p1: BmobException?) {
//                if(p1 == null){
//                    Toast.makeText(HelpEOApplication.getInstance().applicationContext ,"保存成功",Toast.LENGTH_LONG).show();
//                }else{
//                    Toast.makeText(HelpEOApplication.getInstance().applicationContext,"保存失败",Toast.LENGTH_LONG).show();
//                }
//            }
//        })


        //查找Person表里面id为6b6c11c537的数据
        val bmobQuery = BmobQuery<User>();
        bmobQuery.getObject("732766cb49", object : QueryListener<User>() {
            override fun done(p0: User?, p1: BmobException?) {
                if (p1 == null) {
                    show("查询成功")
                    textView?.setText(p0.toString())
                } else {
                    show("查询失败")
                }
            }
        }
        );

        val hotCities = listOf(HotCity("北京", "北京", "101010100"), HotCity("上海", "上海", "101020100")
                , HotCity("广州", "广东", "101280101"), HotCity("深圳", "广东", "101280601"), HotCity("杭州", "浙江", "101210101"));
        val anim = R.style.CustomAnim

        class onCityPick: OnPickListener{
            override fun onPick(position: Int, data: City?) {
                textView?.setText(data?.name)
                tvCityPick?.setText(data?.name)
                show(msg = data?.name)
            }

            override fun onLocate() {
                //开始定位，这里模拟一下定位
                Handler().postDelayed({ CityPicker.getInstance().locateComplete(LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS) }, 3000)
            }
        }

        val cityPicklis = onCityPick()

        tvCityPick?.setOnClickListener{
            CityPicker.getInstance()
                    .setFragmentManager(getSupportFragmentManager())    //此方法必须调用
                    .enableAnimation(true)    //启用动画效果
                    .setAnimationStyle(anim)    //自定义动画
                    .setLocatedCity(LocatedCity ("杭州", "浙江", "101210101"))  //APP自身已定位的城市，默认为null（定位失败）
                    .setHotCities(hotCities)    //指定热门城市
                    .setOnPickListener(cityPicklis)
                    .show();}


    }

    fun show(msg: String?) {
        Toast.makeText(HelpEOApplication.getInstance().applicationContext, msg, Toast.LENGTH_LONG).show();
    }


}


