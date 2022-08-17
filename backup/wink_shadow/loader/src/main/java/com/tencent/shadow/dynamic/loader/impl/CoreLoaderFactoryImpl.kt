package com.tencent.shadow.dynamic.loader.impl

import android.content.Context
import com.raedev.wink.loader.WinkPluginLoader
import com.tencent.shadow.core.loader.ShadowPluginLoader

/**
 * 接口工厂
 * @author RAE
 * @date 2022/08/16
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class CoreLoaderFactoryImpl : CoreLoaderFactory {
    override fun build(hostAppContext: Context): ShadowPluginLoader {
        return WinkPluginLoader(hostAppContext)
    }
}