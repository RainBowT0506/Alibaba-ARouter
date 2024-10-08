package com.example.alibaba_arouter

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.BuildConfig
import com.alibaba.android.arouter.demo.module1.testactivity.TestDynamicActivity
import com.alibaba.android.arouter.demo.module1.testservice.SingleService
import com.alibaba.android.arouter.demo.service.HelloService
import com.alibaba.android.arouter.demo.service.model.TestObj
import com.alibaba.android.arouter.demo.service.model.TestParcelable
import com.alibaba.android.arouter.demo.service.model.TestSerializable
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.facade.enums.RouteType
import com.alibaba.android.arouter.facade.model.RouteMeta
import com.alibaba.android.arouter.launcher.ARouter

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View) {
        // Build test data.
        val testSerializable: TestSerializable = TestSerializable("Titanic", 555)
        val testParcelable: TestParcelable = TestParcelable("jack", 666)
        val testObj: TestObj = TestObj("Rose", 777)
        val objList: MutableList<TestObj> = ArrayList<TestObj>()
        objList.add(testObj)
        val map: MutableMap<String, List<TestObj>> = HashMap<String, List<TestObj>>()
        map["testMap"] = objList

        when (v.id) {
            //  基本设置
            R.id.openLog -> ARouter.openLog()
            R.id.openDebug -> ARouter.openDebug()
            R.id.init -> {
                // 调试模式不是必须开启，但是为了防止有用户开启了InstantRun，但是
                // 忘了开调试模式，导致无法使用Demo，如果使用了InstantRun，必须在
                // 初始化之前开启调试模式，但是上线前需要关闭，InstantRun仅用于开
                // 发阶段，线上开启调试模式有安全风险，可以使用BuildConfig.DEBUG
                // 来区分环境
                ARouter.openDebug()
                ARouter.printStackTrace();
                ARouter.init(application)
            }

            R.id.destroy -> ARouter.getInstance().destroy()

            //  基础功能(请先初始化)
            R.id.normalNavigation -> ARouter.getInstance()
                .build("/test/activity2")
                .navigation()

            R.id.kotlinNavigation -> ARouter.getInstance()
                .build("/kotlin/test")
                .withString("name", "老王")
                .withInt("age", 23)
                .navigation()

            R.id.normalNavigation2 -> ARouter.getInstance()
                .build("/test/activity2")
                .navigation(this, 666)

            R.id.getFragment -> {
                val fragment: Fragment = ARouter.getInstance().build("/test/fragment")
                    .withString("name", "老王")
                    .withInt("age", 18)
                    .withBoolean("boy", true)
                    .withLong("high", 180)
                    .withString("url", "https://a.b.c")
                    .withSerializable("ser", testSerializable)
                    .withParcelable("pac", testParcelable)
                    .withObject("obj", testObj)
                    .withObject("objList", objList)
                    .withObject("map", map).navigation() as Fragment
                Toast.makeText(this, "找到Fragment:" + fragment.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

            R.id.normalNavigationWithParams -> {
                // ARouter.getInstance()
                //         .build("/test/activity2")
                //         .withString("key1", "value1")
                //         .navigation();
                val testUriMix = Uri.parse("arouter://m.aliyun.com/test/activity2")
                ARouter.getInstance().build(testUriMix)
                    .withString("key1", "value1")
                    .navigation()
            }

            R.id.oldVersionAnim -> ARouter.getInstance()
                .build("/test/activity2")
                .withTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                .navigation(this)

            R.id.newVersionAnim -> if (Build.VERSION.SDK_INT >= 16) {
                val compat: ActivityOptionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(
                    v,
                    v.getWidth() / 2,
                    v.getHeight() / 2,
                    0,
                    0
                );

                ARouter.getInstance()
                    .build("/test/activity2")
                    .withOptionsCompat(compat)
                    .navigation()
            } else {
                Toast.makeText(this, "API < 16,不支持新版本动画", Toast.LENGTH_SHORT).show()
            }

            //  进阶用法(请先初始化)
            R.id.navByUrl -> ARouter.getInstance()
                .build("/test/webview")
                .withString("url", "file:///android_asset/scheme-test.xml")
                .navigation()

            R.id.interceptor -> ARouter.getInstance()
                .build("/test/activity4")
                .navigation(this, object : NavCallback() {
                    override fun onArrival(postcard: Postcard) {
                    }

                    override fun onInterrupt(postcard: Postcard) {
                        Log.d("ARouter", "被拦截了")
                    }
                })

            R.id.autoInject -> ARouter.getInstance().build("/test/activity1")
                .withString("name", "老王")
                .withInt("age", 18)
                .withBoolean("boy", true)
                .withLong("high", 180)
                .withString("url", "https://a.b.c")
                .withSerializable("ser", testSerializable)
                .withParcelable("pac", testParcelable)
                .withObject("obj", testObj)
                .withObject("objList", objList)
                .withObject("map", map)
                .navigation()

            //  服务管理(请先初始化)
            R.id.navByName -> (ARouter.getInstance().build("/yourservicegroupname/hello")
                .navigation() as HelloService).sayHello("mike")

            R.id.navByType -> ARouter.getInstance().navigation(HelloService::class.java)
                .sayHello("mike")

            R.id.callSingle -> ARouter.getInstance().navigation(SingleService::class.java)
                .sayHello("Mike")

            //  多模块测试(请先初始化)
            R.id.navToMoudle1 -> ARouter.getInstance().build("/module/1").navigation()
            R.id.navToMoudle2 ->                 // 这个页面主动指定了Group名
                ARouter.getInstance().build("/module/2", "m2").navigation()

            //  跳转失败测试(请先初始化)
            R.id.failNav -> ARouter.getInstance().build("/xxx/xxx")
                .navigation(this, object : NavCallback() {
                    override fun onFound(postcard: Postcard) {
                        Log.d("ARouter", "找到了")
                    }

                    override fun onLost(postcard: Postcard) {
                        Log.d("ARouter", "找不到了")
                    }

                    override fun onArrival(postcard: Postcard) {
                        Log.d("ARouter", "跳转完了")
                    }

                    override fun onInterrupt(postcard: Postcard) {
                        Log.d("ARouter", "被拦截了")
                    }
                })

            R.id.failNav2 -> ARouter.getInstance().build("/xxx/xxx").navigation()
            R.id.failNav3 -> ARouter.getInstance().navigation(MainActivity::class.java)

            //  动态增加路由测试
            R.id.addGroup -> ARouter.getInstance().addRouteGroup { atlas ->
                atlas["/dynamic/activity"] = RouteMeta.build(
                    RouteType.ACTIVITY,
                    TestDynamicActivity::class.java,
                    "/dynamic/activity",
                    "dynamic", 0, 0
                )
            }

            // 该页面未配置 Route 注解，动态注册到 ARouter
            R.id.dynamicNavigation -> ARouter.getInstance().build("/dynamic/activity")
                .withString("name", "老王")
                .withInt("age", 18)
                .withBoolean("boy", true)
                .withLong("high", 180)
                .withString("url", "https://a.b.c")
                .withSerializable("ser", testSerializable)
                .withParcelable("pac", testParcelable)
                .withObject("obj", testObj)
                .withObject("objList", objList)
                .withObject("map", map).navigation(this)

            else -> {}
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            666 -> Log.e("activityResult", resultCode.toString())
            else -> {}
        }
    }
}
