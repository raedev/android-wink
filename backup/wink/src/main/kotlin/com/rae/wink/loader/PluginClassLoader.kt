package com.rae.wink.loader

import com.rae.wink.content.PluginInfo
import com.rae.wink.utils.PackageNameTrie
import com.rae.wink.utils.WinkLog
import dalvik.system.DexClassLoader

/**
 * 插件类加载器，用于加载插件包里面的类。
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PluginClassLoader(
    val pluginInfo: PluginInfo,
    dexPath: String,
    optimizedDirectory: String?,
    parent: ClassLoader
) : DexClassLoader(dexPath, optimizedDirectory, null, parent) {

    /**
     * 排除类列表，排除的类会由父类加载器加载。
     */
    internal val excludeList = PackageNameTrie()


//    override fun loadClass(name: String, resolve: Boolean): Class<*> {
//        try {
//            val classPackageName = name.subStringBeforeDot()
//
//            // 排除的类由父加载器加载
//            if (excludeList.isMatch(classPackageName)) {
//                WinkLog.d("父加载器加载：$name")
//                return parent.loadClass(name)
//            }
//
//
//            // 插件包的类
////            if (classPackageName == pluginInfo.packageName) {
//
//            val clazz = findClass(name)
//            WinkLog.d("插件加载类：$name")
//            return clazz
//
//
//        } catch (e: ClassNotFoundException) {
//            // 找不到给双亲委派
//            WinkLog.w("插件加载类不存在，由父加载：$name")
//            return parent.loadClass(name)
//        } catch (e: Exception) {
//            WinkLog.e("PluginClassLoader Exception: ${e.message}", e)
//        }
//        WinkLog.w("默认父加载：$name")
//        // 遵守双亲委派
//        return parent.loadClass(name)
//    }

    override fun loadClass(name: String?, resolve: Boolean): Class<*> {
        try {
            var clazz = this.findClass(name)
            WinkLog.i("从插件中加载类：$name")
//            try {
//                // 如果父加载器中也有，则以父类为基准
//                clazz = parent.loadClass(name)
//            } catch (ex: ClassNotFoundException) {
//            }
            return clazz
        } catch (ex: ClassNotFoundException) {
            // 无
        }
        WinkLog.d("从父加载器加载类：$name")
        return super.loadClass(name, resolve)
    }


    private fun String.subStringBeforeDot() = substringBeforeLast('.', "")

    private fun String.inPackage(packageNames: PackageNameTrie): Boolean {
        val packageName = this.subStringBeforeDot()
        return packageNames.isMatch(packageName)
    }
}