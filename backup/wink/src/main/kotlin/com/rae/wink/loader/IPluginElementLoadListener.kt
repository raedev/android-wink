package com.rae.wink.loader

import android.content.ComponentName

/**
 * 插件加载回调
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface IPluginElementLoadListener<T> {

    /**
     * 加载成功
     * @param element 插件元素实例
     * @param componentName 组件名称
     */
    fun onElementLoaded(element: T, componentName: ComponentName)

    /**
     * 加载失败
     * @param message 错误消息
     * @param ex 异常信息
     */
    fun onElementError(message: String, ex: Exception? = null)
}