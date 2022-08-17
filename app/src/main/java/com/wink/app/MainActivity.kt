package com.wink.app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.raedev.wink.Winker
import com.raedev.wink.content.PluginInstallInfo
import com.raedev.wink.content.PluginIntent
import com.raedev.wink.content.PluginLoadedApk
import com.raedev.wink.content.component.PluginContainerActivity
import com.raedev.wink.listener.IPluginElementLoadListener
import com.raedev.wink.listener.IPluginLoadListener
import com.wink.app.databinding.ActivityMainBinding

class MainActivity : PluginContainerActivity() {

    private lateinit var binding: ActivityMainBinding

    // 演示插件1
    private val demoPlugin1 = PluginInstallInfo(
        "plugin-debug", "测试插件", 1, 1,
        "https://raeblog.com/wp-content/uploads/plugin-debug.zip",
        "3fd54fe79d2983b5545c7e8719a3203c"
    )
    override val pluginName: String
        get() = demoPlugin1.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnStartPlugin.setOnClickListener(this::onStartClick)
        binding.btnLoadPlugin.setOnClickListener(this::onLoadPluginClick)

        // 注册插件安装回调
        Winker.addPluginListener(object : IPluginLoadListener {
            override fun onPluginDownloading(installInfo: PluginInstallInfo, progress: Int) {
                binding.tvMessage.text = if (progress < 100) "正在下载插件...${progress}%" else "正在安装插件.."
            }

            override fun onPluginLoaded(plugin: PluginLoadedApk) {
                binding.tvMessage.text = "插件安装成功！"
//                onStartClick(null)
            }

            override fun onPluginError(message: String, ex: Exception?) {
                binding.tvMessage.text = "插件安装失败！$message"
            }
        })

//        onLoadPluginClick(null)
    }

    /**
     * 加载插件，不启动
     */
    private fun onLoadPluginClick(view: View?) {
        // 安装插件
        Winker.install(demoPlugin1)
    }

    override fun getClassLoader(): ClassLoader {
        Winker.getLoadedPlugin(demoPlugin1.name)?.let {
            return it.pluginClassLoader
        }
        return super.getClassLoader()
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
                    onLoadFragment(element)
                }

                override fun onElementError(message: String, ex: Exception?) {
                    binding.tvMessage.text = message
                }
            })
    }

    private fun onLoadFragment(element: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.content, element).commit()
    }

}