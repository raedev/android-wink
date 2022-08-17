/*
 * Tencent is pleased to support the open source community by making Tencent Shadow available.
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.raedev.wink.utils

import android.util.Log
import com.tencent.shadow.core.common.ILoggerFactory
import com.tencent.shadow.core.common.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

internal class AndroidLogLoggerFactory : ILoggerFactory {
    private val loggerMap: ConcurrentMap<String, Logger> = ConcurrentHashMap()
    override fun getLogger(name: String): Logger {
        val simpleLogger = loggerMap[name]
        return if (simpleLogger != null) {
            simpleLogger
        } else {
            val newInstance: Logger = IVLogger(name)
            val oldInstance = loggerMap.putIfAbsent(name, newInstance)
            oldInstance ?: newInstance
        }
    }

    internal inner class IVLogger(private val name: String) : Logger {
        override fun getName(): String {
            return name
        }

        private fun log(level: Int, message: String?, t: Throwable?) {
            val tag = name
            when (level) {
                LOG_LEVEL_TRACE, LOG_LEVEL_DEBUG -> if (t == null) Log.d(tag, message!!) else Log.d(
                    tag,
                    message,
                    t
                )
                LOG_LEVEL_INFO -> if (t == null) Log.i(tag, message!!) else Log.i(tag, message, t)
                LOG_LEVEL_WARN -> if (t == null) Log.w(tag, message!!) else Log.w(tag, message, t)
                LOG_LEVEL_ERROR -> if (t == null) Log.e(tag, message!!) else Log.e(tag, message, t)
                else -> {}
            }
        }

        override fun isTraceEnabled(): Boolean {
            return true
        }

        override fun trace(msg: String) {
            log(LOG_LEVEL_TRACE, msg, null)
        }

        override fun trace(format: String, o: Any) {
            val tuple = MessageFormatter.format(format, o)
            log(LOG_LEVEL_TRACE, tuple.message, null)
        }

        override fun trace(format: String, o: Any, o1: Any) {
            val tuple = MessageFormatter.format(format, o, o1)
            log(LOG_LEVEL_TRACE, tuple.message, null)
        }

        override fun trace(format: String, vararg objects: Any) {
            val tuple = MessageFormatter.arrayFormat(format, objects)
            log(LOG_LEVEL_TRACE, tuple.message, null)
        }

        override fun trace(msg: String, throwable: Throwable) {
            log(LOG_LEVEL_TRACE, msg, throwable)
        }

        override fun isDebugEnabled(): Boolean {
            return true
        }

        override fun debug(msg: String) {
            log(LOG_LEVEL_DEBUG, msg, null)
        }

        override fun debug(format: String, o: Any) {
            val tuple = MessageFormatter.format(format, o)
            log(LOG_LEVEL_DEBUG, tuple.message, null)
        }

        override fun debug(format: String, o: Any, o1: Any) {
            val tuple = MessageFormatter.format(format, o, o1)
            log(LOG_LEVEL_DEBUG, tuple.message, null)
        }

        override fun debug(format: String, vararg objects: Any) {
            val tuple = MessageFormatter.arrayFormat(format, objects)
            log(LOG_LEVEL_DEBUG, tuple.message, null)
        }

        override fun debug(msg: String, throwable: Throwable) {
            log(LOG_LEVEL_DEBUG, msg, throwable)
        }

        override fun isInfoEnabled(): Boolean {
            return true
        }

        override fun info(msg: String) {
            log(LOG_LEVEL_INFO, msg, null)
        }

        override fun info(format: String, o: Any) {
            val tuple = MessageFormatter.format(format, o)
            log(LOG_LEVEL_INFO, tuple.message, null)
        }

        override fun info(format: String, o: Any, o1: Any) {
            val tuple = MessageFormatter.format(format, o, o1)
            log(LOG_LEVEL_INFO, tuple.message, null)
        }

        override fun info(format: String, vararg objects: Any) {
            val tuple = MessageFormatter.arrayFormat(format, objects)
            log(LOG_LEVEL_INFO, tuple.message, null)
        }

        override fun info(msg: String, throwable: Throwable) {
            log(LOG_LEVEL_INFO, msg, throwable)
        }

        override fun isWarnEnabled(): Boolean {
            return true
        }

        override fun warn(msg: String) {
            log(LOG_LEVEL_WARN, msg, null)
        }

        override fun warn(format: String, o: Any) {
            val tuple = MessageFormatter.format(format, o)
            log(LOG_LEVEL_WARN, tuple.message, null)
        }

        override fun warn(format: String, o: Any, o1: Any) {
            val tuple = MessageFormatter.format(format, o, o1)
            log(LOG_LEVEL_WARN, tuple.message, null)
        }

        override fun warn(format: String, vararg objects: Any) {
            val tuple = MessageFormatter.arrayFormat(format, objects)
            log(LOG_LEVEL_WARN, tuple.message, null)
        }

        override fun warn(msg: String, throwable: Throwable) {
            log(LOG_LEVEL_WARN, msg, throwable)
        }

        override fun isErrorEnabled(): Boolean {
            return true
        }

        override fun error(msg: String) {
            log(LOG_LEVEL_ERROR, msg, null)
        }

        override fun error(format: String, o: Any) {
            val tuple = MessageFormatter.format(format, o)
            log(LOG_LEVEL_ERROR, tuple.message, null)
        }

        override fun error(format: String, o: Any, o1: Any) {
            val tuple = MessageFormatter.format(format, o, o1)
            log(LOG_LEVEL_ERROR, tuple.message, null)
        }

        override fun error(format: String, vararg objects: Any) {
            val tuple = MessageFormatter.arrayFormat(format, objects)
            log(LOG_LEVEL_ERROR, tuple.message, null)
        }

        override fun error(msg: String, throwable: Throwable) {
            log(LOG_LEVEL_ERROR, msg, throwable)
        }
    }

    companion object {
        private const val LOG_LEVEL_TRACE = 5
        private const val LOG_LEVEL_DEBUG = 4
        private const val LOG_LEVEL_INFO = 3
        private const val LOG_LEVEL_WARN = 2
        private const val LOG_LEVEL_ERROR = 1
        private val sInstance = AndroidLogLoggerFactory()
        val instance: ILoggerFactory
            get() = sInstance
    }
}

internal class FormattingTuple @JvmOverloads constructor(
    val message: String?,
    val argArray: Array<Any?>? = null,
    val throwable: Throwable? = null
) {

    companion object {
        var NULL = FormattingTuple(null)
    }
}

internal object MessageFormatter {
    const val DELIM_START = '{'
    const val DELIM_STOP = '}'
    const val DELIM_STR = "{}"
    private const val ESCAPE_CHAR = '\\'

    /**
     * Performs single argument substitution for the 'messagePattern' passed as
     * parameter.
     *
     *
     * For example,
     *
     * <pre>
     * MessageFormatter.format(&quot;Hi {}.&quot;, &quot;there&quot;);
    </pre> *
     *
     *
     * will return the string "Hi there.".
     *
     *
     *
     * @param messagePattern The message pattern which will be parsed and formatted
     * @param arg            The argument to be substituted in place of the formatting anchor
     * @return The formatted message
     */
    fun format(messagePattern: String?, arg: Any?): FormattingTuple {
        return arrayFormat(messagePattern, arrayOf(arg))
    }

    /**
     * Performs a two argument substitution for the 'messagePattern' passed as
     * parameter.
     *
     *
     * For example,
     *
     * <pre>
     * MessageFormatter.format(&quot;Hi {}. My name is {}.&quot;, &quot;Alice&quot;, &quot;Bob&quot;);
    </pre> *
     *
     *
     * will return the string "Hi Alice. My name is Bob.".
     *
     * @param messagePattern The message pattern which will be parsed and formatted
     * @param arg1           The argument to be substituted in place of the first formatting
     * anchor
     * @param arg2           The argument to be substituted in place of the second formatting
     * anchor
     * @return The formatted message
     */
    fun format(messagePattern: String?, arg1: Any?, arg2: Any?): FormattingTuple {
        return arrayFormat(messagePattern, arrayOf(arg1, arg2))
    }

    fun getThrowableCandidate(vararg argArray: Any?): Throwable? {
        val lastEntry = argArray[argArray.size - 1]
        return if (lastEntry is Throwable) {
            lastEntry
        } else null
    }

    fun arrayFormat(messagePattern: String?, vararg argArray: Any?): FormattingTuple {
        val throwableCandidate = getThrowableCandidate(argArray)
        var args = argArray
        if (throwableCandidate != null) {
            args = trimmedCopy(argArray)
        }
        return arrayFormat(messagePattern, args, throwableCandidate)
    }

    private fun trimmedCopy(vararg argArray: Any?): Array<Any?> {
        val trimemdLen = argArray.size - 1
        val trimmed = arrayOfNulls<Any>(trimemdLen)
        System.arraycopy(argArray, 0, trimmed, 0, trimemdLen)
        return trimmed
    }

    fun arrayFormat(
        messagePattern: String?,
        argArray: Array<Any?>?,
        throwable: Throwable?
    ): FormattingTuple {
        if (messagePattern == null) {
            return FormattingTuple(null, argArray, throwable)
        }
        if (argArray == null) {
            return FormattingTuple(messagePattern)
        }
        var i = 0
        var j: Int
        // use string builder for better multicore performance
        val sbuf = StringBuilder(messagePattern.length + 50)
        var L: Int
        L = 0
        while (L < argArray.size) {
            j = messagePattern.indexOf(DELIM_STR, i)
            i = if (j == -1) {
                // no more variables
                return if (i == 0) { // this is a simple string
                    FormattingTuple(messagePattern, argArray, throwable)
                } else { // add the tail string which contains no variables and return
                    // the result.
                    sbuf.append(messagePattern, i, messagePattern.length)
                    FormattingTuple(sbuf.toString(), argArray, throwable)
                }
            } else {
                if (isEscapedDelimeter(messagePattern, j)) {
                    if (!isDoubleEscaped(messagePattern, j)) {
                        L-- // DELIM_START was escaped, thus should not be incremented
                        sbuf.append(messagePattern, i, j - 1)
                        sbuf.append(DELIM_START)
                        j + 1
                    } else {
                        // The escape character preceding the delimiter start is
                        // itself escaped: "abc x:\\{}"
                        // we have to consume one backward slash
                        sbuf.append(messagePattern, i, j - 1)
                        deeplyAppendParameter(
                            sbuf,
                            argArray[L],
                            HashMap()
                        )
                        j + 2
                    }
                } else {
                    // normal case
                    sbuf.append(messagePattern, i, j)
                    deeplyAppendParameter(
                        sbuf,
                        argArray[L],
                        HashMap()
                    )
                    j + 2
                }
            }
            L++
        }
        // append the characters following the last {} pair.
        sbuf.append(messagePattern, i, messagePattern.length)
        return FormattingTuple(sbuf.toString(), argArray, throwable)
    }

    fun isEscapedDelimeter(messagePattern: String, delimeterStartIndex: Int): Boolean {
        if (delimeterStartIndex == 0) {
            return false
        }
        val potentialEscape = messagePattern[delimeterStartIndex - 1]
        return if (potentialEscape == ESCAPE_CHAR) {
            true
        } else {
            false
        }
    }

    fun isDoubleEscaped(messagePattern: String, delimeterStartIndex: Int): Boolean {
        return if (delimeterStartIndex >= 2 && messagePattern[delimeterStartIndex - 2] == ESCAPE_CHAR) {
            true
        } else {
            false
        }
    }

    // special treatment of array values was suggested by 'lizongbo'
    private fun deeplyAppendParameter(
        sbuf: StringBuilder,
        o: Any?,
        seenMap: MutableMap<Array<Any>, Any?>
    ) {
        if (o == null) {
            sbuf.append("null")
            return
        }
        if (!o.javaClass.isArray) {
            safeObjectAppend(sbuf, o)
        } else {
            // check for primitive array types because they
            // unfortunately cannot be cast to Object[]
            if (o is BooleanArray) {
                booleanArrayAppend(sbuf, o)
            } else if (o is ByteArray) {
                byteArrayAppend(sbuf, o)
            } else if (o is CharArray) {
                charArrayAppend(sbuf, o)
            } else if (o is ShortArray) {
                shortArrayAppend(sbuf, o)
            } else if (o is IntArray) {
                intArrayAppend(sbuf, o)
            } else if (o is LongArray) {
                longArrayAppend(sbuf, o)
            } else if (o is FloatArray) {
                floatArrayAppend(sbuf, o)
            } else if (o is DoubleArray) {
                doubleArrayAppend(sbuf, o)
            } else {
                objectArrayAppend(sbuf, o as Array<Any>, seenMap)
            }
        }
    }

    private fun safeObjectAppend(sbuf: StringBuilder, o: Any) {
        try {
            val oAsString = o.toString()
            sbuf.append(oAsString)
        } catch (t: Throwable) {
            sbuf.append("[FAILED toString()]")
        }
    }

    private fun objectArrayAppend(
        sbuf: StringBuilder,
        a: Array<Any>,
        seenMap: MutableMap<Array<Any>, Any?>
    ) {
        sbuf.append('[')
        if (!seenMap.containsKey(a)) {
            seenMap[a] = null
            val len = a.size
            for (i in 0 until len) {
                deeplyAppendParameter(sbuf, a[i], seenMap)
                if (i != len - 1) sbuf.append(", ")
            }
            // allow repeats in siblings
            seenMap.remove(a)
        } else {
            sbuf.append("...")
        }
        sbuf.append(']')
    }

    private fun booleanArrayAppend(sbuf: StringBuilder, a: BooleanArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i])
            if (i != len - 1) sbuf.append(", ")
        }
        sbuf.append(']')
    }

    private fun byteArrayAppend(sbuf: StringBuilder, a: ByteArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i])
            if (i != len - 1) sbuf.append(", ")
        }
        sbuf.append(']')
    }

    private fun charArrayAppend(sbuf: StringBuilder, a: CharArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i])
            if (i != len - 1) sbuf.append(", ")
        }
        sbuf.append(']')
    }

    private fun shortArrayAppend(sbuf: StringBuilder, a: ShortArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i])
            if (i != len - 1) sbuf.append(", ")
        }
        sbuf.append(']')
    }

    private fun intArrayAppend(sbuf: StringBuilder, a: IntArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i])
            if (i != len - 1) sbuf.append(", ")
        }
        sbuf.append(']')
    }

    private fun longArrayAppend(sbuf: StringBuilder, a: LongArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i])
            if (i != len - 1) sbuf.append(", ")
        }
        sbuf.append(']')
    }

    private fun floatArrayAppend(sbuf: StringBuilder, a: FloatArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i])
            if (i != len - 1) sbuf.append(", ")
        }
        sbuf.append(']')
    }

    private fun doubleArrayAppend(sbuf: StringBuilder, a: DoubleArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i])
            if (i != len - 1) sbuf.append(", ")
        }
        sbuf.append(']')
    }
}