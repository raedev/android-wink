package com.raedev.wink.utils

import android.util.Log

/**
 * 日志记录
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
object WinkLog {

    private const val TAG = "wink"

    fun d(message: String) = Log.d(TAG, message)
    fun i(message: String) = Log.i(TAG, message)
    fun w(message: String) = Log.w(TAG, message)
    fun e(message: String, ex: Exception? = null) = Log.e(TAG, message, ex)
}