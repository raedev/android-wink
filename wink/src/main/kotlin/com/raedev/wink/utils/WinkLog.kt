package com.raedev.wink.utils

import android.util.Log as ALog

/**
 * 日志记录
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
object WinkLog {
    private const val TAG = "wink"

    fun d(message: String) = ALog.d(TAG, message)
    fun i(message: String) = ALog.i(TAG, message)
    fun w(message: String) = ALog.w(TAG, message)
    fun e(message: String) = ALog.e(TAG, message)
    fun e(message: String, e: Exception?) = ALog.e(TAG, message, e)
}