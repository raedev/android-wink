package com.raedev.wink.content.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raedev.wink.Winker
import com.raedev.wink.content.PluginInstallInfo
import com.raedev.wink.content.PluginLoadedApk
import com.raedev.wink.listener.IPluginLoadListener

/**
 * 插件容器Activity，要运行插件中的组件必须继承该Activity，否则无法正常加载。
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class PluginContainerActivity : AppCompatActivity(), IPluginLoadListener {

    /**
     * 当前Activity加载的插件名称
     */
    abstract val pluginName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Winker.addPluginListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Winker.removePluginListener(this)
    }

    override fun getClassLoader(): ClassLoader {
        Winker.getLoadedPlugin(pluginName)?.let {
            return it.pluginClassLoader
        }
        return super.getClassLoader()
    }

    override fun onPluginDownloading(installInfo: PluginInstallInfo, progress: Int) {

    }

    override fun onPluginLoaded(plugin: PluginLoadedApk) {
        Winker.attachToActivity(this, plugin)
    }

    override fun onPluginError(message: String, ex: Exception?) {
    }

}