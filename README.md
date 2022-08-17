# android-wink

Wink是一个轻量级的Android插件框架，只用于动态加载Fragment并与宿主进行资源共享。

# 集成文档

**1、引用库**

```groovy

// 添加仓库地址
repositories {
    maven { url 'https://maven.raeblog.com/repository/public/' }
}

// 引用库
dependencies {
    implementation 'com.github.raedev:wink:1.0.0'
}

```

**2、快速使用**

更多详细操作请运行`app`查看示例。

```kotlin

// 在你的应用程序中进行初始化
class WinkApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化
        Winker.init(this)
    }
}


// 装载插件的Activity必须继承 PluginContainerActivity
class MainActivity : PluginContainerActivity() {

    // 演示插件1
    private val demoPlugin1 = PluginInstallInfo(
        "plugin-debug", "测试插件", 1, 1,
        "https://raeblog.com/wp-content/uploads/plugin-debug.zip",
        "3fd54fe79d2983b5545c7e8719a3203c"
    )

    // 插件名称
    override val pluginName: String
        get() = demoPlugin1.name

    
    private fun onLoadPluginClick(view: View?) {
        // 安装插件
        Winker.install(demoPlugin1)
    }


    /**
     * 启动插件
     */
    private fun onStartClick(view: View?) {
        Winker.createFragment(
            PluginIntent(
                this,
                demoPlugin1.name,
                "com.wink.plugin.a.PluginAFragment",
                Bundle().also {
                    it.putString("from", "host")
                    it.putString("hostName", this.packageName)
                }), object : IPluginElementLoadListener<Fragment> {
                
                override fun onElementLoaded(element: Fragment) {
                    binding.tvMessage.text = "加载成功"
                    supportFragmentManager.beginTransaction().replace(R.id.content, element).commit()
                }
                
                override fun onElementError(message: String, ex: Exception?) {
                    binding.tvMessage.text = message
                }
            })
    }

}


```
