package com.rae.wink.pm

import android.annotation.SuppressLint
import android.content.Context
import android.os.Binder
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.rae.wink.content.PluginInstallInfo
import com.rae.wink.install.PluginInstaller
import com.rae.wink.loader.IPluginLoadListener
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors

/**
 * 插件包管理器，真正实现插件安装、卸载等操作
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal class PluginPackageManager private constructor() : Binder() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        private val instance = PluginPackageManager()

        fun get(): PluginPackageManager = instance
    }

    private class InstallInfo(
        val installInfo: PluginInstallInfo,
        val listener: IPluginLoadListener
    )

    /** 安装列表 */
    private val installList = ConcurrentLinkedQueue<InstallInfo>()

    /** 安装器 */
    private val installer = PluginInstaller()

    /** 插件加载线程池 */
    private val threadPool = Executors.newFixedThreadPool(1)

    /** 主线程回调 */
    private val handle = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val obj = msg.obj
            if (msg.what == 0 && obj is InstallInfo) {
                obj.listener.onPluginDownloading(obj.installInfo, msg.arg1)
            }
        }
    }


    /**
     * 安装插件
     */
    fun install(context: Context, info: PluginInstallInfo, listener: IPluginLoadListener) {
        installList.add(InstallInfo(info, listener))
        threadPool.execute {
            while (true) {
                val item = installList.poll() ?: break
                try {
                    val plugin = installer.install(context, item.installInfo) { progress ->
                        handle.removeMessages(0)
                        handle.sendMessageDelayed(
                            Message.obtain(handle, 0, progress, 0, item), 1000
                        )
                    }
                    handle.removeMessages(0)
                    Message.obtain(handle, 0, 100, 0, item).sendToTarget()
                    handle.post { item.listener.onPluginLoaded(plugin) }
                } catch (ex: Exception) {
                    handle.post {
                        item.listener.onPluginError(
                            "插件安装失败：${ex.message} ", ex
                        )
                    }
                }
            }
        }
    }

}