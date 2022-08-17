package com.wink.plugin.a

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 *
 * @author RAE
 * @date 2022/08/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PluginAActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, PluginAFragment())
            .commitNow()
    }
}