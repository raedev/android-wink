//package com.rae.wink.pm
//
//import android.app.Service
//import android.content.Intent
//import android.os.IBinder
//
///**
// * 插件后台服务，管理插件的调用、安装等操作
// * @author RAE
// * @date 2022/08/15
// * @copyright Copyright (c) https://github.com/raedev All rights reserved.
// */
//class PluginPackageManagerService : Service() {
//
//    private val binder = PluginPackageManager.get()
//
//    override fun onCreate() {
//        super.onCreate()
//        binder.context = applicationContext
//    }
//
//    override fun onBind(intent: Intent?): IBinder {
//        return binder
//    }
//}