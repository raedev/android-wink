package com.raedev.wink.pm

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.raedev.wink.Winker
import com.raedev.wink.content.PluginInfo
import com.raedev.wink.content.PluginInstallFile
import com.raedev.wink.content.PluginInstallInfo
import com.raedev.wink.utils.WinkLog
import com.tencent.shadow.core.manager.installplugin.InstalledPlugin
import com.tencent.shadow.core.manager.installplugin.InstalledType
import com.tencent.shadow.dynamic.host.EnterCallback
import com.tencent.shadow.dynamic.manager.PluginManagerThatUseDynamicLoader
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * shadow框架插件管理器实现。
 * shadow设计更为灵活，管理器也可以是动态的。一般来说一个应用程序只需要一个插件管理器即可。
 * @author RAE
 * @date 2022/08/16
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal class ShadowPluginManagerImpl(private val context: Context) :
    PluginManagerThatUseDynamicLoader(context) {

    companion object {

        /** 插件加载后台服务名称 */
        private val pluginServiceName = "com.raedev.wink.pm.WinkPluginService"
    }

    private val loadedPlugins = mutableMapOf<String, PluginInstallInfo>()

    /** 加载插件线程池 */
    private val threadPool = Executors.newCachedThreadPool()

    /** 插件管理器名称 */
    override fun getName(): String = "WinkPluginManager"


    /**
     * 进入插件默认配置的Activity
     */
    fun launch() {

    }


    /**
     * 启动插件的Activity
     */
    fun startActivity(pluginName: String, pluginIntent: Intent) {
        // 如果没有加载
        if (!isPluginLoaded(pluginName)) {
            Winker.pluginManager.onPluginError("插件尚未加载")
        }
        try {
            val intent = mPluginLoader.convertActivityIntent(pluginIntent)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val resolveInfo =
                context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (resolveInfo == null) {
                Winker.pluginManager.onPluginError("无法找到插件中的Activity：$pluginIntent")
                return
            }
            mPluginLoader.startActivityInPluginProcess(intent)
        } catch (ex: Exception) {
            Winker.pluginManager.onPluginError("启动界面失败：${ex.message}", ex)
        }
    }

    /**
     * 加载插件
     * @param inf 插件安装信息
     */
    fun loadPlugin(inf: PluginInstallInfo, block: (() -> Unit)? = null) {
        threadPool.execute {
            try {
                loadPluginOnThread(inf)
                // 回调
                block?.invoke()
            } catch (ex: Exception) {
                runOnUiThread { Winker.pluginManager.onPluginError("加载插件异常，${ex.message}", ex) }
            }
        }
    }

    private fun isPluginLoaded(name: String): Boolean {
        return this.loadedPlugins.containsKey(name)
    }

    @Throws
    private fun loadPluginOnThread(inf: PluginInstallInfo) {
        // 检查插件是否已经加载
        if (isPluginLoaded(name)) return
        // 检查文件是否已经下载
        val installFile = PluginInstallFile(context, inf)
        if (!installFile.isDownloaded()) {
            // 开始下载插件包
            PackageDownloader(context).download(inf) { progress ->
                runOnUiThread { Winker.pluginManager.onPluginDownloading(inf, progress) }
            }
        }
        runOnUiThread { Winker.pluginManager.onPluginDownloading(inf, 100) }
        // 安装插件
        val plugin = installPlugin(installFile.pluginDownloadFile)
        WinkLog.d("插件信息：${plugin.UUID}")

        // 加载RuntimeLoader
        onLoadPlugin(plugin)

        // 缓存
        loadedPlugins[inf.name] = inf

        runOnUiThread { Winker.pluginManager.onPluginLoaded(PluginInfo(plugin.plugins.firstNotNullOf { it.value })) }
    }


    /**
     * 安装插件
     */
    private fun installPlugin(file: File): InstalledPlugin {
        // 解压插件
        val config = installPluginFromZip(file, null)
        val uuid = config.UUID

        // 已经安装
        val dbPlugin = getInstalledPlugin(uuid)
        if (dbPlugin != null && dbPlugin.plugins.isNotEmpty()) return dbPlugin

        // 优化ODEX
        oDexPluginLoaderOrRunTime(
            uuid, InstalledType.TYPE_PLUGIN_RUNTIME, config.runTime.file
        )
        oDexPluginLoaderOrRunTime(
            uuid, InstalledType.TYPE_PLUGIN_LOADER, config.pluginLoader.file
        )
        // 插件安装
        config.plugins.forEach {
            oDexPlugin(uuid, it.key, it.value.file)
        }
        // 持久化
        onInstallCompleted(config, null)
        return getInstalledPlugins(1)[0]
    }

    /**
     * 加载插件的Runtime、Loader并装载插件
     */
    private fun onLoadPlugin(plugin: InstalledPlugin) {
        if (mPpsController == null) {
            bindPluginProcessService(pluginServiceName)
            waitServiceConnected(10, TimeUnit.SECONDS)
        }
        val uuid = plugin.UUID
        loadRunTime(uuid)
        loadPluginLoader(uuid)

        // call application created
        val loadedMap = mPluginLoader.loadedPlugin
        plugin.plugins.forEach {
            val isCalled = loadedMap[it.key] as Boolean? ?: false
            if (!isCalled) {
                // 加载插件
                mPluginLoader.loadPlugin(it.key)
                mPluginLoader.callApplicationOnCreate(it.key)
            }
        }
    }


    // region 不重要的方法

    override fun enter(context: Context?, fromId: Long, bundle: Bundle?, callback: EnterCallback?) {
        throw IllegalAccessError("推荐使用launch()方式启动插件")
    }

    private fun runOnUiThread(block: Runnable) {
        mUiHandler.post(block)
    }

    // endregion
}