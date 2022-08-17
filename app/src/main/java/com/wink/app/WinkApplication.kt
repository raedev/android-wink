package com.wink.app

import android.app.Application
import com.raedev.wink.Winker

/**
 *
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class WinkApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化
        Winker.init(this)
    }
}