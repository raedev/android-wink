package com.raedev.wink.pm

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import com.raedev.wink.content.PluginInfo
import com.raedev.wink.content.PluginInstallInfo
import com.raedev.wink.content.PluginIntent
import com.raedev.wink.content.PluginLoadedApk
import com.raedev.wink.install.PluginInstallTemplate
import com.raedev.wink.install.PluginInstaller
import com.raedev.wink.listener.IPluginElementLoadListener
import com.raedev.wink.listener.IPluginLoadListener
import com.raedev.wink.utils.WinkLog
import java.util.concurrent.Executors

/**
 * 插件管理器
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PluginManager internal constructor(context: Context) {

    /** 正在加载的插件 */
    private val loadingPlugins = mutableMapOf<String, PluginInstallInfo>()

    /** 已经加载的插件 */
    private val loadedPlugins = mutableMapOf<String, PluginLoadedApk>()

    /** 插件回调 */
    private val listeners = mutableListOf<IPluginLoadListener>()

    /** 插件加载线程池 */
    private val threadPool = Executors.newCachedThreadPool()

    /** 主线程回调 */
    private val handle = Handler(Looper.getMainLooper())

    private val pluginInstaller: PluginInstallTemplate = PluginInstaller(context)

    fun addPluginListener(listener: IPluginLoadListener) {
        if (!listeners.contains(listener)) listeners.add(listener)
    }

    fun removePluginListener(listener: IPluginLoadListener) {
        listeners.remove(listener)
    }

    /**
     * 获取已经加载的插件
     */
    fun getLoadedPlugin(pluginName: String): PluginLoadedApk? {
        return loadedPlugins[pluginName]
    }


    /**
     * 加载插件包中的类
     */
    fun <T> loadClass(
        hostContext: Context,
        pluginName: String,
        className: String,
        listener: IPluginElementLoadListener<T>? = null
    ) {
        if (!loadedPlugins.containsKey(pluginName)) {
            listener?.onElementError("插件尚未加载")
            return
        }
        threadPool.execute {
            try {
                val plugin = loadedPlugins[pluginName]!!
                val element = plugin.loadClass<T>(className)
                if (element == null) {
                    listener?.onElementError("加载插件类异常$className")
                    return@execute
                }
                handle.post { listener?.onElementLoaded(element) }
            } catch (ex: Exception) {
                handle.post {
                    listener?.onElementError("加载插件类异常:$className，${ex.message}", ex)
                }
            }
        }
    }

    /**
     * 安装插件
     */
    fun install(installInfo: PluginInstallInfo) {
        if (loadingPlugins.containsKey(installInfo.name)) {
            callPluginError("插件正在安装中，请勿重复加载插件")
            return
        }
        threadPool.execute {
            try {
                // 放到加载中列表中去
                loadingPlugins[installInfo.name] = installInfo
                pluginInstaller.downloadProgressListener = {
                    callPluginDownloading(installInfo, it)
                }
                val pluginInfo = pluginInstaller.install(installInfo)
                // 装载插件
                val plugin = loadPlugin(pluginInfo)
                loadedPlugins[installInfo.name] = plugin
                callPluginLoaded(plugin)
                // 移除
                loadingPlugins.remove(installInfo.name)
            } catch (ex: Exception) {
                // 移除
                loadingPlugins.remove(installInfo.name)
                callPluginError("安装插件失败：${ex.message}", ex)
            }
        }

    }

    private fun loadPlugin(pluginInfo: PluginInfo): PluginLoadedApk {
        return pluginInstaller.loadPlugin(pluginInfo)
    }

    /**
     * 创建Fragment
     */
    fun createFragment(
        pluginIntent: PluginIntent,
        listener: IPluginElementLoadListener<Fragment>
    ) {
        loadClass(pluginIntent.context, pluginIntent.pluginName, pluginIntent.className, listener)
    }


    private fun callPluginLoaded(plugin: PluginLoadedApk) {
        handle.post { listeners.forEach { it.onPluginLoaded(plugin) } }
    }

    private fun callPluginError(message: String, ex: Exception? = null) {
        WinkLog.e(message, ex)
        handle.post { listeners.forEach { it.onPluginError(message, ex) } }
    }

    private fun callPluginDownloading(installInfo: PluginInstallInfo, progress: Int) {
        handle.post { listeners.forEach { it.onPluginDownloading(installInfo, progress) } }
    }

}