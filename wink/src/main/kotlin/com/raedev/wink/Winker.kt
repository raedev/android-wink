package com.raedev.wink

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.raedev.wink.content.PluginInstallInfo
import com.raedev.wink.content.PluginIntent
import com.raedev.wink.content.PluginLoadedApk
import com.raedev.wink.listener.IPluginElementLoadListener
import com.raedev.wink.listener.IPluginLoadListener
import com.raedev.wink.pm.PluginManager
import com.raedev.wink.utils.ResourcesUtil

/**
 * 插件入口
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
object Winker {

    internal lateinit var context: Application
    private lateinit var pluginManager: PluginManager

    fun init(context: Application) {
        this.context = context
        pluginManager = PluginManager(context)
    }

    fun addPluginListener(listener: IPluginLoadListener) {
        pluginManager.addPluginListener(listener)
    }

    fun removePluginListener(listener: IPluginLoadListener) {
        pluginManager.removePluginListener(listener)
    }

    fun install(installInfo: PluginInstallInfo) {
        pluginManager.install(installInfo)
    }

    fun createFragment(
        pluginIntent: PluginIntent,
        listener: IPluginElementLoadListener<Fragment>
    ) {
        pluginManager.createFragment(pluginIntent, listener)
    }

    fun getLoadedPlugin(pluginName: String): PluginLoadedApk? {
        return pluginManager.getLoadedPlugin(pluginName)
    }

    fun attachToActivity(context: Activity, plugin: PluginLoadedApk) {
        context.classLoader
        // 加载完毕后更新资源
        ResourcesUtil.addAssetPath(context.assets, plugin.pluginInfo.apkFile)
    }
}