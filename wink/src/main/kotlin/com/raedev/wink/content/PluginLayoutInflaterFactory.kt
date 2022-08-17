package com.raedev.wink.content

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.raedev.wink.utils.WinkLog

/**
 * 插件的布局管理器
 * @author RAE
 * @date 2022/08/16
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PluginLayoutInflaterFactory(private val factory: LayoutInflater.Factory2) :
    LayoutInflater.Factory2 {

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {

        WinkLog.d("PluginLayoutInflaterFactory：view=$name, res=${context.resources}， classloader=${context.classLoader}")

        return factory.onCreateView(parent, name, context, attrs)
    }


    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return factory.onCreateView(name, context, attrs)
    }
}