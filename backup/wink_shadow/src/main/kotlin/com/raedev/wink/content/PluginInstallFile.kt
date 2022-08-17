package com.raedev.wink.content

import android.content.Context
import androidx.swift.util.FileUtils
import java.io.File

/**
 * 插件安装文件目录操作
 * @author RAE
 * @date 2022/08/16
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal class PluginInstallFile(context: Context, private val inf: PluginInstallInfo) {

    /** 插件包根目录 */
    private val pluginRootDir: File by lazy {
        val dir = context.cacheDir
        val extDir = context.externalCacheDir
        val parentDir: File = extDir?.canWrite().let { extDir } ?: dir
        File(parentDir, "plugin").createDirIfNotExist()
    }


    /** 插件目录 */
    val pluginPackageDir: File by lazy {
        val packageDir = File(pluginRootDir, "package").createDirIfNotExist()
        File(packageDir, inf.name)
    }

    /** 插件临时目录 */
    private val pluginTempDir: File by lazy {
        val packageDir = File(pluginRootDir, "temp").createDirIfNotExist()
        File(packageDir, inf.name)
    }


    /** 插件临时目录 */
    val pluginUnzipDir: File by lazy {
        File(pluginTempDir, inf.name).createDirIfNotExist()
    }


    /** 插件包下载目录 */
    private val pluginDownloadDir: File by lazy {
        File(pluginRootDir, "download").createDirIfNotExist()
    }


    /** 下载插件包文件路径 */
    val pluginDownloadFile: File by lazy {
        File(pluginDownloadDir, inf.name + ".zip")
    }

    fun getPackageInfoJson(dir: File): File {
        return File(dir, "info.json")
    }

    /** 目录不存在的时候自动创建 */
    private fun File.createDirIfNotExist(): File {
        if (!this.exists()) this.mkdir()
        return this
    }

    /**
     * 插件包是否已经下载
     */
    fun isDownloaded(): Boolean {
        if (FileUtils.isFileExists(pluginDownloadFile)) {
            return true
//            return inf.md5.equals(FileUtils.getFileMD5ToString(pluginDownloadFile), true)
        }
        return false
    }
}