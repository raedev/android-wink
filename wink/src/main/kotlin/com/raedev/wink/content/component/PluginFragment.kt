package com.raedev.wink.content.component

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.raedev.wink.Winker
import com.raedev.wink.content.PluginLoadedApk
import com.raedev.wink.loader.PluginClassLoader
import com.raedev.wink.utils.ReflectionUtil
import com.raedev.wink.utils.WinkLog

/**
 * 插件中的Fragment必须依赖此类
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class PluginFragment : Fragment() {

//    protected val pluginApk: PluginLoadedApk by lazy {
//        val classLoader = this.javaClass.classLoader as PluginClassLoader
//        Winker.getLoadedPlugin(classLoader.pluginInfo.name)!!
//    }
//
    protected fun createLayout(id: Int, root: ViewGroup?): View {
        return LayoutInflater.from(requireContext()).inflate(id, root, false)
    }
//
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        modifyHostResources(pluginApk.pluginResources)
//    }
//
//
//    /**
//     * 替换掉Context
//     */
//    private fun modifyHostResources(res: Resources) {
//        val at = context as FragmentActivity
//        ReflectionUtil.setFieldValue(
//            FragmentActivity::class.java, at, "mResources", res
//        )
//        WinkLog.d("修改插件的Host成功：${requireContext().resources}")
//    }
}