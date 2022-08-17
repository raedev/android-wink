package com.rae.wink.content.component

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.rae.wink.content.PluginLoadedApk
import com.rae.wink.loader.PluginClassLoader
import com.rae.wink.pm.PluginManager
import com.rae.wink.utils.ReflectionUtil
import com.rae.wink.utils.WinkLog

/**
 * 插件中的Fragment必须依赖此类
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class PluginFragment : Fragment() {

    protected var pluginLoadedApk: PluginLoadedApk? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val classLoader = this.javaClass.classLoader
        if (classLoader is PluginClassLoader) {
            // 修改context的资源文件
            val pluginName = classLoader.pluginInfo.name
            pluginLoadedApk = PluginManager.get().getLoadedPlugin(pluginName)
                ?: throw NullPointerException("${classLoader.pluginInfo.title}插件尚未加载！")
            modifyHostResources(pluginLoadedApk!!.pluginResources)
        }

        WinkLog.d("LayoutInflater=${layoutInflater.javaClass.classLoader}")
    }



    /**
     * 替换掉Context
     */
    private fun modifyHostResources(res: Resources) {
        val at = context as FragmentActivity
        ReflectionUtil.setFieldValue(
            FragmentActivity::class.java, at, "mResources", res
        )
        WinkLog.d("修改插件的Host成功：${requireContext().resources}")
    }
}