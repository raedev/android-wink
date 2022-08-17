package com.raedev.wink

import android.app.Application
import android.content.Intent
import com.raedev.wink.content.PluginInstallInfo
import com.raedev.wink.pm.PluginManager
import com.raedev.wink.utils.AndroidLogLoggerFactory
import com.tencent.shadow.core.common.LoggerFactory

/**
 * 插件入口
 * @author RAE
 * @date 2022/08/16
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
object Winker {

    /**
     * 全局Context
     */
    lateinit var context: Application

    /**
     * 获取插件管理器
     */
    val pluginManager: PluginManager by lazy {
        PluginManager(context)
    }

    /**
     * 初始化Wink
     */
    fun init(application: Application) {
        this.context = application
        LoggerFactory.setILoggerFactory(AndroidLogLoggerFactory())
    }

    fun install(inf: PluginInstallInfo) {
        this.pluginManager.install(inf)
    }

    fun startActivity(pluginName: String, intent: Intent) {
        this.pluginManager.startActivity(pluginName, intent)
    }

}