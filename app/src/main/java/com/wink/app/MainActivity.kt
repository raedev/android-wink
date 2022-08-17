package com.wink.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.raedev.wink.Winker
import com.raedev.wink.content.PluginInfo
import com.raedev.wink.content.PluginInstallInfo
import com.raedev.wink.listener.IPluginLoadListener
import com.wink.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // 演示插件1
    private val demoPlugin1 = PluginInstallInfo(
        "plugin-demo", "测试插件", 1, 1,
        "https://raeblog.com/wp-content/uploads/plugin-debug.zip",
        "3fd54fe79d2983b5545c7e8719a3203c"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnStartPlugin.setOnClickListener(this::onStartClick)
        binding.btnLoadPlugin.setOnClickListener(this::onLoadPluginClick)

        // 注册插件安装回调
        Winker.pluginManager.addPluginListener(object : IPluginLoadListener {
            override fun onPluginDownloading(installInfo: PluginInstallInfo, progress: Int) {
                binding.tvMessage.text = if (progress < 100) "正在下载插件...${progress}%" else "正在安装插件.."
            }

            override fun onPluginLoaded(plugin: PluginInfo) {
                binding.tvMessage.text = "插件安装成功！"
            }

            override fun onPluginError(message: String, ex: Exception?) {
                binding.tvMessage.text = "插件安装失败！$message"
            }
        })

        onLoadPluginClick(null)
    }

    /**
     * 加载插件，不启动
     */
    private fun onLoadPluginClick(view: View?) {
        // 安装插件
        Winker.install(demoPlugin1)
    }

    /**
     * 启动插件
     */
    private fun onStartClick(view: View) {
        val intent = Intent().apply {
            this.setClassName(this@MainActivity, "com.shadow.plugina.MainActivity")
            this.putExtra("from", "host")
        }
        Winker.startActivity(demoPlugin1.name, intent)
    }
}