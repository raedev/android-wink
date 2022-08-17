package com.raedev.wink.loader

import com.raedev.wink.content.PluginInfo
import com.raedev.wink.utils.PackageNameTrie
import com.raedev.wink.utils.WinkLog
import dalvik.system.DexClassLoader

/**
 * 插件类加载器，用于加载插件包里面的类。
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PluginClassLoader(
    private val pluginInfo: PluginInfo,
    dexPath: String,
    optimizedDirectory: String?,
    parent: ClassLoader
) : DexClassLoader(dexPath, optimizedDirectory, null, parent) {

    private val dependenciesList = PackageNameTrie()

    init {
        pluginInfo.dependencies?.forEach {
            dependenciesList.insert(it)
        }
    }

    override fun loadClass(name: String, resolve: Boolean): Class<*> {
//        val parentClass = parentLoadClass(name)
//        val selfClass = selfLoadClass(name)
//        // 两者都有的时候以父为主
//        if (selfClass != null && parentClass != null) return parentClass
//        // 父没有自己有
//        if (parentClass == null && selfClass != null) return selfClass
//        return super.loadClass(name, resolve)
//        try {
//            val packageName = name.subStringBeforeDot()
//            if (packageName == pluginInfo.packageName) {
//                WinkLog.d("从插件中加载类：$name")
//                return this.findClass(name)
//            }
//            if (packageName.inPackage(dependenciesList)) {
//                WinkLog.d("配置白名单加载：$name")
//                return this.findClass(name)
//            }
//        } catch (ex: ClassNotFoundException) {
//            WinkLog.d("从父类加载：$name")
//            return parent.loadClass(name)
//        }
//        WinkLog.d("默认加载类：$name")
        return try {
            WinkLog.d("默认加载类：$name")
            parent.loadClass(name)
        } catch (ex: ClassNotFoundException) {
            this.findClass(name)
        }
    }


    private fun String.subStringBeforeDot() = substringBeforeLast('.', "")

    private fun String.inPackage(packageNames: PackageNameTrie): Boolean {
        val packageName = this.subStringBeforeDot()
        return packageNames.isMatch(packageName)
    }
}