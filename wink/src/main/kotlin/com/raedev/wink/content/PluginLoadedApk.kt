package com.raedev.wink.content

import android.content.Context
import android.content.res.Resources
import com.raedev.wink.content.wrapper.PluginResources
import com.raedev.wink.loader.PluginClassLoader
import com.raedev.wink.utils.ResourcesUtil
import com.raedev.wink.utils.WinkLog
import java.io.File

/**
 * 已经加载的插件
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")
class PluginLoadedApk(
    val hostContext: Context,
    val pluginInfo: PluginInfo
) {

    val pluginName: String = pluginInfo.name
    val hostResources: Resources = hostContext.resources
    val pluginClassLoader: ClassLoader
    val pluginResources: Resources

    init {
        // 类加载器
        this.pluginClassLoader = PluginClassLoader(
            pluginInfo,
            this.pluginInfo.apkFile.path,
            File(this.pluginInfo.apkFile.parent, "optimized").path,
            hostContext.classLoader
        )

        // 创建资源
        this.pluginResources = createPluginResources()

        WinkLog.d("插件类加载器为：$pluginClassLoader")
        WinkLog.d("插件资源为：$pluginResources")

//        ResourcesUtil.setPluginLayoutInflaterFactory(LayoutInflater.from(hostContext))
    }

    private fun createPluginResources(): Resources {
        val pm = hostContext.packageManager
        val resources = pm.getResourcesForApplication(pluginInfo.packageInfo.applicationInfo)
        val assetManager = resources.assets
        // 共享宿主资源
        ResourcesUtil.addAssetPath(assetManager, File(hostContext.applicationInfo.sourceDir))

        return PluginResources(
            assetManager, hostResources.displayMetrics, hostResources.configuration,
            hostResources, pluginInfo.packageName
        )
    }

    fun <T> loadClass(className: String): T? {
        return this.pluginClassLoader.loadClass(className).newInstance() as T?
    }
}