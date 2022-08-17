package com.raedev.wink.utils

import android.content.res.AssetManager
import android.view.LayoutInflater
import com.raedev.wink.content.PluginLayoutInflaterFactory
import java.io.File

/**
 * 资源工具
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
object ResourcesUtil {

    /**
     * 添加资源路径
     */
    fun addAssetPath(assets: AssetManager, file: File) {
        try {
            val clazz = assets.javaClass
            val method = clazz.getMethod("addAssetPath", String::class.java)
            method.isAccessible = true
            method.invoke(assets, file.path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
//
//    private fun newAssetManager(context: Context, apkFile: File): AssetManager {
//        val am = context.packageManager.getResourcesForApplication(context.applicationInfo).assets
//
//        val clazz = AssetManager::class.java
//        val method = clazz.getMethod("addAssetPath", String::class.java)
//        method.isAccessible = true
//        // 共享宿主资源
//        method.invoke(am, context.applicationInfo.sourceDir)
//        method.invoke(am, apkFile.path)
//        return am
//    }
//
//
//    fun createPluginResources(
//        packageName: String,
//        hostResources: Resources,
//        apkFile: File
//    ): PluginResources {
//        val am = newAssetManager(apkFile)
//        return PluginResources(
//            am,
//            hostResources.displayMetrics,
//            hostResources.configuration,
//            hostResources,
//            packageName
//        )
//    }

    fun setPluginLayoutInflaterFactory(inflater: LayoutInflater) {
        val clazz = LayoutInflater::class.java
        val field = clazz.getDeclaredField("mPrivateFactory")
        field.isAccessible = true
        val factory = field.get(inflater) as LayoutInflater.Factory2
        val method = clazz.getMethod("setPrivateFactory", LayoutInflater.Factory2::class.java)
        method.isAccessible = true
        method.invoke(inflater, PluginLayoutInflaterFactory(factory))
    }

}