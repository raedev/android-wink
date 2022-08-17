package com.raedev.wink.install

import android.content.Context
import androidx.swift.util.FileIOUtils
import androidx.swift.util.FileUtils
import com.raedev.wink.content.PluginInfo
import com.raedev.wink.content.PluginInstallInfo
import com.raedev.wink.content.PluginLoadedApk
import java.io.File

/**
 * 安装步骤
 * @author RAE
 * @date 2022/08/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class PluginInstallTemplate(protected val context: Context) {

    private val downloader: IPackageDownloader = OkHttpPackageDownloader(context)

    /** 下载回调 */
    var downloadProgressListener: ((Int) -> Unit)? = null


    /** 插件包根目录 */
    private val rootDir: File by lazy {
        val parentDir: File = context.externalCacheDir?.let {
            if (it.canWrite()) it else null
        } ?: context.cacheDir
        File(parentDir, "plugin").createDirIfNotExist()
    }

    /** 插件包下载目录 */
    private val downloadDir: File by lazy {
        File(rootDir, "download").createDirIfNotExist()
    }

    /** 插件包目录 */
    protected val pluginPackageDir: File by lazy {
        File(rootDir, "package").createDirIfNotExist()
    }


    /**
     * 执行安装
     * @param inf 安装信息
     */
    fun install(inf: PluginInstallInfo): PluginInfo {
        // 已经安装直接返回
        if (isInstalled(inf)) {
            return loadInstalledPluginInfo(inf)
        }
        // 已经下载的插件包
        val file = if (!isDownloaded(inf)) downloadPluginFile(inf) else inf.downloadFile()
        // 检查是否已经安装
        return installFormZip(inf, file)
    }

    /**
     * 已经安装的插件包信息
     */
    abstract fun loadInstalledPluginInfo(inf: PluginInstallInfo): PluginInfo

    /**
     * 安装
     */
    abstract fun installFormZip(inf: PluginInstallInfo, file: File): PluginInfo


    /**
     * 下载文件
     */
    protected open fun downloadPluginFile(inf: PluginInstallInfo): File {
        return downloader.download(inf) {
            this.downloadProgressListener?.invoke(it)
        }
    }


    /**
     * 插件是否已经下载
     */
    protected open fun isDownloaded(inf: PluginInstallInfo): Boolean {
        return inf.downloadFile().exists()
    }

    /**
     * 是否已经安装
     */
    protected open fun isInstalled(inf: PluginInstallInfo): Boolean {
        val text = FileIOUtils.readFile2String(inf.lockFile()) ?: "0"
        val installVersion = text.toIntOrNull() ?: 0
        return inf.versionCode == installVersion
    }


    /**
     * 插件包下载路径
     */
    protected fun PluginInstallInfo.downloadFile(): File {
        return File(downloadDir, "${this.name}.zip")
    }

    /**
     * 插件包下载路径
     */
    protected fun PluginInstallInfo.pluginDir(): File {
        return File(pluginPackageDir, this.name).createDirIfNotExist()
    }


    /**
     * 插件包下载路径
     */
    protected fun PluginInstallInfo.pluginFile(): File? {
        return this.pluginDir().listFiles()?.singleOrNull { it.extension == "apk" }
    }

    /**
     * 插件包下载路径
     */
    protected fun PluginInstallInfo.infoFile(): File {
        return File(this.pluginDir(), "info.json")
    }


    /**
     * 插件包下载路径
     */
    protected fun PluginInstallInfo.lockFile(): File {
        return File(this.pluginDir(), "plugin.lock")
    }

    /**
     * 目录不存在的时候自动创建
     */
    protected fun File.createDirIfNotExist(): File {
        FileUtils.createOrExistsDir(this)
        return this
    }

    /**
     * 加载插件
     */
    fun loadPlugin(pluginInfo: PluginInfo): PluginLoadedApk {
        return PluginLoadedApk(context, pluginInfo)
    }
}