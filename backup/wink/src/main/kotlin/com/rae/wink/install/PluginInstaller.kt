package com.rae.wink.install

import android.content.Context
import androidx.swift.util.*
import com.rae.wink.content.PluginFileOperator
import com.rae.wink.content.PluginInfo
import com.rae.wink.content.PluginInstallInfo
import com.rae.wink.content.PluginLoadedApk
import com.rae.wink.exception.PluginException
import com.rae.wink.utils.WinkLog
import okio.FileNotFoundException
import java.io.File

/**
 * 插件安装器
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PluginInstaller() {

    private lateinit var fileOperator: PluginFileOperator
    private lateinit var installInfo: PluginInstallInfo
    private lateinit var context: Context

    fun install(
        context: Context,
        installInfo: PluginInstallInfo,
        progressListener: (Int) -> Unit
    ): PluginLoadedApk {
        this.context = context
        this.installInfo = installInfo
        this.fileOperator = PluginFileOperator(context, installInfo)

        // 已经安装
        if (isInstalled()) {
            val json = FileIOUtils.readFile2String(fileOperator.pluginInfoFile)
            val pluginInfo = BeanUtils.toBean(json, PluginInfo::class.java)
            return loadPlugin(pluginInfo)
        }
        if (!fileOperator.isDownloaded()) {
            // 启动下载
            OkHttpPackageDownloader(context).download(installInfo, progressListener)
        }
        val file = fileOperator.pluginDownloadFile
        return installPluginFromPackageFile(file)
    }


    /**
     * 是否已经安装
     */
    private fun isInstalled(): Boolean {
        return fileOperator.pluginVersion == installInfo.versionCode
    }


    private fun installPluginFromPackageFile(file: File): PluginLoadedApk {
        // 宿主版本号
        val hostVersionCode = AppUtils.getAppVersionCode()
        // 解压插件包
        ZipUtils.unzipFile(file, fileOperator.pluginUnzipDir)
        // 获取插件包信息
        val jsonFile = fileOperator.getPackageInfoJson(fileOperator.pluginUnzipDir)
        if (!FileUtils.isFileExists(jsonFile)) {
            // 删除已经下载的插件包
            FileUtils.delete(file)
            throw FileNotFoundException("插件包信息缺失，请重新下载")
        }
        val packageFile =
            fileOperator.getPackageFile(fileOperator.pluginUnzipDir) ?: throw FileNotFoundException(
                "插件包缺失，请重新下载"
            )
        if (!FileUtils.isFileExists(packageFile)) {
            // 删除已经下载的插件包
            FileUtils.delete(file)
            throw FileNotFoundException("插件包缺失，请重新下载")
        }
        val json = FileIOUtils.readFile2String(jsonFile)
        WinkLog.d("插件包JSON：$json")
        val pluginInfo = BeanUtils.toBean(json, PluginInfo::class.java)
        WinkLog.d("插件包信息：$pluginInfo")
        // 宿主版本校验，下载提示交给业务去做。
        if (pluginInfo.hostVersionCode != hostVersionCode) throw PluginException("主程序版本较低(Ver$hostVersionCode, Cur${pluginInfo.hostVersionCode})，请更新后再使用。")

        // 删除当前插件目录
        FileUtils.delete(fileOperator.pluginPackageDir)
        FileUtils.delete(file)

        // 移动到插件目录
        FileUtils.moveDir(fileOperator.pluginUnzipDir, fileOperator.pluginPackageDir, null)


        // 生成版本号和锁
        fileOperator.pluginVersion = pluginInfo.versionCode
        fileOperator.pluginLockFile.createNewFile()

        return loadPlugin(pluginInfo)

    }

    private fun loadPlugin(pluginInfo: PluginInfo): PluginLoadedApk {
        pluginInfo.load(context, fileOperator.pluginPackageFile!!)
        return PluginLoadedApk(context, pluginInfo)
    }
}