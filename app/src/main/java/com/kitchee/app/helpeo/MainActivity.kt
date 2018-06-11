package com.kitchee.app.helpeo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.kitchee.app.helpeo.appCommon.GlideImageLoader
import com.kitchee.app.helpeo.appCommon.HelpEOApplication
import com.kitchee.app.helpeo.base.BaseActivity
import com.kitchee.app.helpeo.network.NetWork
import com.kitchee.app.helpeo.testRxJava.RxJavaTextActivity
import com.kitchee.app.helpeo.utils.StatusBarUtils
import com.youth.banner.Banner
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.HotCity
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class MainActivity : BaseActivity() {
    private val imageUrls = arrayOf("http://www.fdhao.cn/data/afficheimg/1495390513544727777.jpg", "http://www.fdhao.cn/data/afficheimg/1495589062250157826.jpg", "http://www.fdhao.cn/data/afficheimg/1494812104978168405.jpg", "http://www.fdhao.cn/data/afficheimg/1494981105456344186.jpg", "http://www.fdhao.cn/data/afficheimg/1495390183732065802.jpg", "http://www.fdhao.cn/data/afficheimg/1495409860833294008.jpg", "http://www.fdhao.cn/data/afficheimg/1494812581931179021.jpg", "http://www.fdhao.cn/data/afficheimg/1495588873555624520.jpg")

    protected var disposable: Disposable? = null
    private val adapter: LabelItemAdapter = LabelItemAdapter()
    private var keywork: String = "在下"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView: TextView? = findViewById(R.id.tv_show) as TextView?
        val tvCityPick:TextView? = findViewById(R.id.addr_sel) as TextView?
        val editText:EditText? = findViewById(R.id.cwet_edit) as EditText?
        val ivScan:ImageView? = findViewById(R.id.scan) as ImageView?
        val recycleView:RecyclerView? = findViewById(R.id.recycleView) as RecyclerView?
        setSwipeBackEnable(true)

        StatusBarUtils.setTranslucentStatusBar(this, false)

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
//        val bmobQuery = BmobQuery<User>()
//        bmobQuery.getObject("732766cb49", object : QueryListener<User>() {
//            override fun done(p0: User?, p1: BmobException?) {
//                if (p1 == null) {
//                    show("查询成功")
//                    textView?.text = p0.toString()
//                } else {
//                    show("查询失败")
//                }
//            }
//        }
//        )

        val hotCities = listOf(HotCity("北京", "北京", "101010100"), HotCity("上海", "上海", "101020100")
                , HotCity("广州", "广东", "101280101"), HotCity("深圳", "广东", "101280601"), HotCity("杭州", "浙江", "101210101"))
        val anim = R.style.CustomAnim

        class onCityPick: OnPickListener{
            override fun onPick(position: Int, data: City?) {
                textView?.text = data?.name
                tvCityPick?.text = data?.name
                if("深圳".equals(data?.name.toString())){
                    keywork = "可爱"
                }else{
                    keywork = "在下"
                }
                show(msg = data?.name)
                search(keywork)
            }

            override fun onLocate() {
                //开始定位，这里模拟一下定位
                Handler().postDelayed({ CityPicker.getInstance().locateComplete(LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS) }, 3000)
            }
        }

        val cityPicklis = onCityPick()

        tvCityPick?.setOnClickListener{
            CityPicker.getInstance()
                    .setFragmentManager(supportFragmentManager)    //此方法必须调用
                    .enableAnimation(true)    //启用动画效果
                    .setAnimationStyle(anim)    //自定义动画
                    .setLocatedCity(LocatedCity ("杭州", "浙江", "101210101"))  //APP自身已定位的城市，默认为null（定位失败）
                    .setHotCities(hotCities)    //指定热门城市
                    .setOnPickListener(cityPicklis)
                    .show();}

        editText?.setOnClickListener{
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            val location = IntArray(2)
            editText.getLocationOnScreen(location)
            intent.putExtra("x", location[0])
            intent.putExtra("y", location[1])
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        ivScan?.setOnClickListener{
            val intent = Intent(this@MainActivity,RxJavaTextActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out)
        }

        val manager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recycleView?.setLayoutManager(manager)
//        val adapter = ZhuangbiAdapter()
        recycleView?.setAdapter(adapter)
        recycleView?.setItemAnimator(DefaultItemAnimator())
        search(keywork)

    }

    fun show(msg: String?) {
        Toast.makeText(HelpEOApplication.getInstance().applicationContext, msg, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable != null && disposable?.isDisposed() != true) {
            disposable?.dispose()
        }
    }

    private fun search(key: String) {
        disposable = NetWork.getZhuangbiApi()
                .search(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ zhuangbiImgs ->
                    adapter.setImages(zhuangbiImgs)
                    disposable?.dispose()
                }) { Toast.makeText(this, "数据加载失败", Toast.LENGTH_SHORT).show() }
    }

}




