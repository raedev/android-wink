package com.raedev.wink.loader

import android.content.Context
import android.util.Log
import com.tencent.shadow.core.common.InstalledApk
import com.tencent.shadow.core.loader.ShadowPluginLoader
import com.tencent.shadow.core.loader.managers.ComponentManager
import java.util.concurrent.Future

/**
 *
 * @author RAE
 * @date 2022/08/16
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class WinkPluginLoader(private val context: Context) : ShadowPluginLoader(context) {

    private val componentManager = WinkComponentManager(context)

    override fun getComponentManager(): ComponentManager {
        return componentManager
    }

    override fun loadPlugin(installedApk: InstalledApk): Future<*> {
        val future = super.loadPlugin(installedApk)
        mExecutorService.submit {
            future.get()
            val parameters = installedApk.getLoadParameters()
            val part = getPluginParts(parameters.partKey)!!
            val app = part.application
            val classloader = part.classLoader
            val resource = part.resources
            Log.d("Rae", "插件加载成功：classloader=$classloader, res = $resource")
        }
        return future
    }
}