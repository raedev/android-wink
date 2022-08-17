package com.raedev.wink.content

import android.content.Context
import android.os.Bundle

/**
 * 插件Intent
 * @author RAE
 * @date 2022/08/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PluginIntent(
    var context: Context,
    /** 插件名 */
    var pluginName: String,
    /** 插件类名 */
    var className: String,
    /** 插件传值 */
    var bundle: Bundle? = null
)