package com.rae.wink.pm

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.rae.wink.content.PluginInstallInfo
import com.rae.wink.content.PluginLoadedApk
import com.rae.wink.content.component.PluginFragment
import com.rae.wink.loader.IPluginElementLoadListener
import com.rae.wink.loader.IPluginLoadListener
import com.rae.wink.utils.WinkLog
import java.util.concurrent.Executors

/**
 * 插件管理器
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PluginManager private constructor() {

    companion object {
        private val instance: PluginManager = PluginManager()
        fun get(): PluginManager = instance
    }

    /** 运行时的插件 */
    private val runtimePlugins = mutableMapOf<String, PluginInstallInfo>()

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

    fun addRuntimePlugin(info: PluginInstallInfo) {
        if (!runtimePlugins.containsKey(info.name)) {
            runtimePlugins[info.name] = info
        }
    }

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
     * 实例化Fragment
     */
    fun <T : PluginFragment> newFragment(
        hostContext: Context,
        pluginName: String,
        name: ComponentName,
        bundle: Bundle?,
        listener: IPluginElementLoadListener<T>
    ) {
        this.loadClass(hostContext, pluginName, name, object : IPluginElementLoadListener<T> {
            override fun onElementError(message: String, ex: Exception?) {
                listener.onElementError(message, ex)
            }

            override fun onElementLoaded(element: T, componentName: ComponentName) {
                element.arguments = bundle
                listener.onElementLoaded(element, componentName)
            }

        })
    }

    /**
     * 加载插件包中的类
     */
    fun <T> loadClass(
        hostContext: Context,
        pluginName: String,
        name: ComponentName,
        listener: IPluginElementLoadListener<T>
    ) {

        // 没有装载插件到运行时中
        if (!runtimePlugins.containsKey(pluginName)) {
            notifyLoadPluginError("请先将${pluginName}添加到运行时插件中。")
            return
        }

        val info: PluginInstallInfo = runtimePlugins[pluginName]!!

        // 如果插件已经在加载中
        if (loadingPlugins.containsKey(pluginName)) {
            notifyLoadPluginError("${pluginName}正在加载中，请勿重复运行。")
            return
        }

        // 放到加载中列表中去
        loadingPlugins[pluginName] = info

        // 如插件已经安装，从安装好的插件中加载
        if (loadedPlugins.containsKey(pluginName)) {
            WinkLog.d("插件已经加载：$pluginName")
            loadClassFormPlugin(
                pluginName, loadedPlugins[pluginName]!!, hostContext, name, listener
            )
            return
        }

        // 开始加载插件
        WinkLog.d("开始安装插件：$pluginName")
        PluginPackageManager.get().install(hostContext, info, object : IPluginLoadListener {
            override fun onPluginDownloading(installInfo: PluginInstallInfo, progress: Int) {
                listeners.forEach { it.onPluginDownloading(installInfo, progress) }
            }

            override fun onPluginLoaded(plugin: PluginLoadedApk) {
                // 添加到已加载插件列表中
                loadedPlugins[pluginName] = plugin
                loadClassFormPlugin(pluginName, plugin, hostContext, name, listener)
            }

            override fun onPluginError(message: String, ex: Exception?) {
                loadingPlugins.remove(pluginName)
                notifyLoadPluginError(message, ex)
            }
        })
    }

    /**
     * 从插件中加载类
     */
    private fun <T> loadClassFormPlugin(
        pluginName: String,
        plugin: PluginLoadedApk,
        hostContext: Context,
        name: ComponentName,
        listener: IPluginElementLoadListener<T>
    ) {
        threadPool.execute {
            try {
                val element = plugin.loadClass<T>(name)
                if (element == null) {
                    listener.onElementError("加载插件类[${name.className}]异常，原因未知。")
                    return@execute
                }
                handle.post { listener.onElementLoaded(element, name) }
            } catch (ex: Exception) {
                handle.post {
                    listener.onElementError("加载插件类[${name.className}]异常:${ex.message}", ex)
                }
            }

            loadingPlugins.remove(pluginName)
        }
    }


    /**
     * 通知插件加载错误
     */
    private fun notifyLoadPluginError(message: String, ex: Exception? = null) {
        WinkLog.e(message, ex)
        listeners.forEach { it.onPluginError(message, ex) }
    }

}