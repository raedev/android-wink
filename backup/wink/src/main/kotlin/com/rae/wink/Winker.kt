package com.rae.wink

import android.app.Application

/**
 * 插件入口
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
object Winker {

    internal lateinit var context: Application

    fun init(context: Application) {
        this.context = context
    }
}