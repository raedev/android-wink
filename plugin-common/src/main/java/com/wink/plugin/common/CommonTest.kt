package com.wink.plugin.common

import android.content.Context
import android.widget.Toast

/**
 * 公共类测试
 * @author RAE
 * @date 2022/08/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
object CommonTest {
    val name = "common test 修改了"

    fun say(context: Context, hello: String) {
        Toast.makeText(context, hello, Toast.LENGTH_SHORT).show()
    }

}