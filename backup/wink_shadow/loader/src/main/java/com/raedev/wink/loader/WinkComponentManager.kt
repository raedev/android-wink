package com.raedev.wink.loader

import android.content.ComponentName
import android.content.Context
import com.tencent.shadow.core.loader.infos.ContainerProviderInfo
import com.tencent.shadow.core.loader.managers.ComponentManager

/**
 *
 * @author RAE
 * @date 2022/08/16
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class WinkComponentManager(private val context: Context) : ComponentManager() {

    override fun onBindContainerActivity(pluginActivity: ComponentName): ComponentName {
        val name = pluginActivity.className
        return when {
            name.contains("SingleInstance") -> ComponentName(
                context,
                "com.raedev.wink.runtime.WinkSinglePluginActivity"
            )
            name.contains("SingleTask") -> ComponentName(
                context,
                "com.raedev.wink.runtime.WinkTaskPluginActivity"
            )
            else -> ComponentName(context, "com.raedev.wink.runtime.WinkPluginActivity")
        }
    }

    override fun onBindContainerContentProvider(pluginContentProvider: ComponentName): ContainerProviderInfo {
        return ContainerProviderInfo(
            "com.tencent.shadow.core.runtime.container.PluginContainerContentProvider",
            "${context.packageName}.contentprovider.authority.dynamic"
        )
    }
}