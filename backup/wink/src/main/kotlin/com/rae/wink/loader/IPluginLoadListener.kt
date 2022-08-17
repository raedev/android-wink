package com.rae.wink.loader

import com.rae.wink.content.PluginInstallInfo
import com.rae.wink.content.PluginLoadedApk

/**
 * 插件加载回调
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface IPluginLoadListener {

    /**
     * 插件正在下载
     */
    fun onPluginDownloading(installInfo: PluginInstallInfo, progress: Int)

    /**
     * 插件加载成功
     */
    fun onPluginLoaded(plugin: PluginLoadedApk)

    /**
     * 插件加载失败
     */
    fun onPluginError(message: String, ex: Exception? = null)
}