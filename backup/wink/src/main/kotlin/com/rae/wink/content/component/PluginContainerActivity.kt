package com.rae.wink.content.component

import androidx.appcompat.app.AppCompatActivity

/**
 * 插件容器Activity，要运行插件中的组件必须继承该Activity，否则无法正常加载。
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class PluginContainerActivity : AppCompatActivity() {

    /**
     * 当前Activity装载的插件。
     * 一个Activity可以装载多个插件，根据业务需要动态返回当前activity装载的插件名。
     */
    abstract val pluginName: String


}