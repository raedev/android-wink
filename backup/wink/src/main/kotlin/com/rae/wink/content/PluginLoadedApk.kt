package com.rae.wink.content

import android.content.ComponentName
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import com.rae.wink.content.wrapper.PluginResources
import com.rae.wink.loader.PluginClassLoader
import com.rae.wink.utils.ResourcesUtil
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

        // 排除列表
        pluginInfo.excludePackageList?.forEach {
            this.pluginClassLoader.excludeList.insert(it)
        }

        // 创建资源
        this.pluginResources = createPluginResources()

        ResourcesUtil.setPluginLayoutInflaterFactory(LayoutInflater.from(hostContext))
    }

    private fun createPluginResources(): Resources {
        val pm = hostContext.packageManager
        val resources = pm.getResourcesForApplication(pluginInfo.packageInfo.applicationInfo)
        val assetManager = resources.assets
//        val assetManager =  AssetManager::class.java.newInstance()
//        ResourcesUtil.addAssetPath(assetManager, File(pluginInfo.packageInfo.applicationInfo.sourceDir))
        // 共享宿主资源
        ResourcesUtil.addAssetPath(assetManager, File(hostContext.applicationInfo.sourceDir))
        return PluginResources(
            assetManager, hostResources.displayMetrics, hostResources.configuration,
            hostResources, pluginInfo.packageName
        )
    }

    fun <T> loadClass(name: ComponentName): T? {
        return this.pluginClassLoader.loadClass(name.className).newInstance() as T?
    }
}