//package com.rae.wink.pm
//
//import android.content.ComponentName
//import android.content.Context
//import android.content.Intent
//import android.content.ServiceConnection
//import android.os.Handler
//import android.os.IBinder
//import android.os.Looper
//import com.rae.wink.content.PluginInstallInfo
//import com.rae.wink.install.PluginInstaller
//import com.rae.wink.loader.IPluginLoadListener
//import com.rae.wink.utils.WinkLog
//import java.util.concurrent.ConcurrentLinkedQueue
//import java.util.concurrent.Executors
//
//
///**
// * 插件服务绑定
// * @author RAE
// * @date 2022/08/15
// * @copyright Copyright (c) https://github.com/raedev All rights reserved.
// */
//internal class PluginPackageManagerNative private constructor() {
//
//    companion object {
//
//        private val instance = PluginPackageManagerNative()
//
//        fun get() = instance
//    }
//
//    private class InstallInfo(
//        val installInfo: PluginInstallInfo,
//        val listener: IPluginLoadListener
//    )
//
//
//    private var onBindServiceListener: (() -> Unit)? = null
//
//    var pluginPackageManager: PluginPackageManager? = null
//
//    inner class PluginServiceConnection : ServiceConnection {
//
//        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            pluginPackageManager = service as PluginPackageManager
//            onBindServiceListener?.invoke()
//        }
//
//        override fun onServiceDisconnected(name: ComponentName?) {
//            pluginPackageManager = null
//        }
//
//    }
//
//    private val serviceConnection: ServiceConnection by lazy {
//        PluginServiceConnection()
//    }
//
//    /** 安装列表 */
//    private val installList = ConcurrentLinkedQueue<InstallInfo>()
//
//    /** 安装器 */
//    private val installer = PluginInstaller()
//
//    /** 插件加载线程池 */
//    private val threadPool = Executors.newFixedThreadPool(1)
//
//    /** 主线程回调 */
//    private val handle = Handler(Looper.getMainLooper())
//
//
////    private fun bindService(context: Context) {
////        try {
////            if (pluginPackageManager != null) {
////                WinkLog.w("插件服务已经绑定！")
////                return
////            }
////            val intent = Intent(context, PluginPackageManagerService::class.java)
////            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
////        } catch (e: Exception) {
////            WinkLog.e("插件服务启动异常", e)
////        }
////    }
//
//    private fun unbind(context: Context) {
//        if (pluginPackageManager != null) {
//            context.unbindService(serviceConnection)
//            pluginPackageManager = null
//        }
//    }
//
//    /** 自动绑定服务 */
//    private fun autoBindService(context: Context) {
//        if (pluginPackageManager == null) {
//            bindService(context)
//        }
//    }
//
//    /**
//     * 安装插件
//     */
//    fun install(context: Context, info: PluginInstallInfo, listener: IPluginLoadListener) {
//        installList.add(InstallInfo(info, listener))
//        autoBindService(context)
//        threadPool.execute {
//            while (true) {
//                val item = installList.poll() ?: break
//                try {
//                    val plugin = installer.install(item.installInfo)
//                    if (plugin != null) {
//                        handle.post { item.listener.onPluginLoaded(plugin) }
//                        return@execute
//                    }
//                    handle.post { item.listener.onPluginError("${info.title}插件安装失败! ") }
//                } catch (ex: Exception) {
//                    handle.post {
//                        item.listener.onPluginError(
//                            "${info.title}插件安装失败：${ex.message} ", ex
//                        )
//                    }
//                }
//            }
//        }
//    }
//}