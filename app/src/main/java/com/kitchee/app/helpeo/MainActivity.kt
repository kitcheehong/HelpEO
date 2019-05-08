package com.kitchee.app.helpeo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.kitchee.app.helpeo.appCommon.GlideImageLoader
import com.kitchee.app.helpeo.appCommon.HelpEOApplication
import com.kitchee.app.helpeo.base.BaseActivity
import com.kitchee.app.helpeo.bean.HeadLineNews
import com.kitchee.app.helpeo.customservicerobot.AutoChatActivity
import com.kitchee.app.helpeo.disklrucache.DiskLruCache
import com.kitchee.app.helpeo.display.ScreenAdaption
import com.kitchee.app.helpeo.network.NetWork
import com.kitchee.app.helpeo.testRxJava.RxJavaTextActivity
import com.kitchee.app.helpeo.utils.SharePreferencesUtil
import com.kitchee.app.helpeo.utils.StatusBarUtils
import com.kitchee.app.helpeo.view.UPMarqueeView
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
import java.util.*


class MainActivity : BaseActivity() {
    private val imageUrls = arrayOf("file:///android_asset/fengjing1.jpg", "file:///android_asset/fengjing2.jpg", "file:///android_asset/fengjing3.jpg", "file:///android_asset/fengjing4.jpg", "file:///android_asset/fengjing5.jpg", "file:///android_asset/fengjing6.jpg")
    protected var disposable: Disposable? = null
    private val adapter: LabelItemAdapter = LabelItemAdapter()
    private var keywork: String = "在下"
    internal var mHeadLineNews: List<HeadLineNews> = ArrayList()
    internal var views: MutableList<View> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ScreenAdaption.setCustomDensity(this, HelpEOApplication.helpEOApplication, 360f)
        val textView: TextView? = findViewById(R.id.tv_show) as TextView?
        val tvCityPick:TextView? = findViewById(R.id.addr_sel) as TextView?
        val editText:EditText? = findViewById(R.id.cwet_edit) as EditText?
        val ivScan:ImageView? = findViewById(R.id.scan) as ImageView?
        val recycleView:RecyclerView? = findViewById(R.id.recycleView) as RecyclerView?
        val itemRecycle:RecyclerView? = findViewById(R.id.hot_recycler_view) as RecyclerView?
        val upMarqueeView: UPMarqueeView? = findViewById(R.id.upview1) as UPMarqueeView?

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
                Handler().postDelayed({ CityPicker.getInstance().locateComplete(LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS) }, 1000)
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
             val psw = SharePreferencesUtil.getInstance().saveGesturePassword;
            val intent:Intent
            if(psw == null){
                intent = Intent(this@MainActivity, PatternSettingActivity::class.java)
            }else{
                intent = Intent(this@MainActivity,PatternCheckActivity::class.java)
            }

            val location = IntArray(2)
            editText.getLocationOnScreen(location)
            intent.putExtra("x", location[0])
            intent.putExtra("y", location[1])
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        ivScan?.setOnClickListener{
            val intent = Intent(this@MainActivity,AutoChatActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out)
        }

        val manager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recycleView?.setLayoutManager(manager)
//        val adapter = ZhuangbiAdapter()
        recycleView?.setAdapter(adapter)
        recycleView?.setItemAnimator(DefaultItemAnimator())
        search(keywork)

        if (upMarqueeView != null) {
            initHeadlineNewsView(upMarqueeView)
        }
    }

    private fun initHeadlineNewsView(upView: UPMarqueeView) {
        setView()
        upView.setViews(views)
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

    /**
     * 初始化需要循环的View
     * 为了灵活的使用滚动的View，所以把滚动的内容让用户自定义
     * 假如滚动的是三条或者一条，或者是其他，只需要把对应的布局，和这个方法稍微改改就可以了，
     */
    private fun setView() {
        if (mHeadLineNews.size == 0) {
            mHeadLineNews = listOf(HeadLineNews("1","约看电影-<速度8激情>","","生活资讯"),HeadLineNews("2","陪玩游戏-<绝地求生>","","娱乐玩耍"))
        }
        var i = 0
        while (i < mHeadLineNews.size) {
            val position = i
            //设置滚动的单个布局
            val moreView = LayoutInflater.from(this).inflate(R.layout.item_view, null) as LinearLayout
            //初始化布局的控件_内容view
            val tv1 = moreView.findViewById<TextView>(R.id.tv1) as TextView
            val tv2 = moreView.findViewById<TextView>(R.id.tv2) as TextView
            //初始化布局的控件_标题View
            val title1 = moreView.findViewById<TextView>(R.id.title_tv1) as TextView
            val title2 = moreView.findViewById<TextView>(R.id.title_tv2) as TextView

            moreView.findViewById<RelativeLayout>(R.id.rl).setOnClickListener(View.OnClickListener {
                //都去到更多页面

            })
            moreView.findViewById<RelativeLayout>(R.id.rl2).setOnClickListener(View.OnClickListener {
                //都去到更多页面

            })
            //进行对控件赋值
            tv1.text = mHeadLineNews[i].title
            title1.text = mHeadLineNews[i].category
            if (mHeadLineNews.size > i + 1) {
                //因为淘宝那儿是两条数据，但是当数据是奇数时就不需要赋值第二个，所以加了一个判断，还应该把第二个布局给隐藏掉
                tv2.text = mHeadLineNews[i + 1].title
                title2.text = mHeadLineNews[i].category
            } else {
                moreView.findViewById<RelativeLayout>(R.id.rl2).setVisibility(View.GONE)
            }

            //添加到循环滚动数组里面去
            views.add(moreView)
            i +=  1
        }
    }

}




