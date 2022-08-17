package com.rae.wink.install

import android.content.Context
import android.text.TextUtils
import androidx.swift.util.FileIOUtils
import androidx.swift.util.FileUtils
import com.rae.wink.content.PluginFileOperator
import com.rae.wink.content.PluginInstallInfo
import com.rae.wink.utils.WinkLog
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import java.io.BufferedInputStream
import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

/**
 * 默认OKHttp下载器
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class OkHttpPackageDownloader(protected val context: Context) : IPackageDownloader {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()


    override fun download(info: PluginInstallInfo, progressListener: (Int) -> Unit): File {
        val fileOperator = PluginFileOperator(context, info)
        val url = info.downloadUrl
        if (TextUtils.isEmpty(url)) throw NullPointerException("插件下载地址不能为空")
        WinkLog.d("下载插件：$info")
        val downloadFile = fileOperator.pluginDownloadFile
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val body = response.body ?: throw NullPointerException("插件下载错误：$url")
        FileUtils.delete(downloadFile)
        val length = body.contentLength()
        if (length <= 0) throw IOException("下载文件为空，请检查下载地址：$url")
        val bufferStream = object : BufferedInputStream(body.byteStream(), length.toInt()) {
            override fun available(): Int {
                return length.toInt()
            }
        }
        FileIOUtils.writeFileFromIS(
            downloadFile, bufferStream
        ) { progress ->
            val pg = (progress * 100).roundToInt()
            progressListener.invoke(pg)
        }
        if (!FileUtils.isFileExists(downloadFile)) throw FileNotFoundException("插件下载错误：文件不存在！")
        val fileMD5 = FileUtils.getFileMD5ToString(downloadFile)
//        if (!info.md5.equals(fileMD5, true)) throw FileNotFoundException("文件校验失败！")
        return downloadFile
    }
}