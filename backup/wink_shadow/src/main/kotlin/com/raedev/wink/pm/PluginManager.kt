package com.raedev.wink.pm

import android.app.Application
import android.content.Intent
import com.raedev.wink.content.PluginInfo
import com.raedev.wink.content.PluginInstallInfo
import com.raedev.wink.listener.IPluginLoadListener
import com.raedev.wink.utils.WinkLog

/**
 * 插件管理器
 * @author RAE
 * @date 2022/08/16
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PluginManager internal constructor(context: Application) {

    private val impl = ShadowPluginManagerImpl(context)

    /** 插件回调 */
    private val listeners = mutableListOf<IPluginLoadListener>()

    internal fun onPluginDownloading(inf: PluginInstallInfo, progress: Int) {
        WinkLog.d("正在下载插件(${progress}%)：$inf")
        listeners.forEach { it.onPluginDownloading(inf, progress) }
    }

    internal fun onPluginError(message: String, ex: Exception? = null) {
        WinkLog.e(message, ex)
        listeners.forEach { it.onPluginError(message, ex) }
    }

    internal fun onPluginLoaded(pluginInfo: PluginInfo) {
        listeners.forEach { it.onPluginLoaded(pluginInfo) }
    }

    fun addPluginListener(listener: IPluginLoadListener) {
        if (!listeners.contains(listener)) listeners.add(listener)
    }

    fun removePluginListener(listener: IPluginLoadListener) {
        listeners.remove(listener)
    }

    fun install(inf: PluginInstallInfo) {
        impl.loadPlugin(inf)
    }

    fun startActivity(pluginName: String, intent: Intent) {
        impl.startActivity(pluginName, intent)
    }

}