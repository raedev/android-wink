package com.raedev.wink.install

import android.content.Context
import androidx.swift.util.*
import com.raedev.wink.content.PluginInfo
import com.raedev.wink.content.PluginInstallInfo
import com.raedev.wink.exception.PluginException
import com.raedev.wink.utils.WinkLog
import okio.FileNotFoundException
import java.io.File

/**
 * 插件安装器
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PluginInstaller(context: Context) : PluginInstallTemplate(context) {

    override fun loadInstalledPluginInfo(inf: PluginInstallInfo): PluginInfo {
        val pluginFile = inf.pluginFile()
        val jsonFile = inf.infoFile()
        val json = FileIOUtils.readFile2String(jsonFile)
        val pluginInfo = BeanUtils.toBean(json, PluginInfo::class.java)
        pluginInfo.load(context, pluginFile!!)
        return pluginInfo
    }

    override fun installFormZip(inf: PluginInstallInfo, file: File): PluginInfo {
        // 宿主版本号
        val hostVersionCode = AppUtils.getAppVersionCode()
        val pluginDir = inf.pluginDir()
        val unzipDir = File(inf.downloadFile().parentFile, "unzip").createDirIfNotExist()
        // 解压插件包
        FileUtils.delete(unzipDir)
        ZipUtils.unzipFile(file, unzipDir)
        val jsonFile = File(unzipDir, "info.json")
        val pluginFile = unzipDir.listFiles()?.singleOrNull { it.extension == "apk" }
        if (!FileUtils.isFileExists(jsonFile)) {
            FileUtils.delete(file)
            throw FileNotFoundException("插件包信息缺失，请重新下载")
        }
        if (!FileUtils.isFileExists(pluginFile)) {
            FileUtils.delete(file)
            throw FileNotFoundException("插件包缺失，请重新下载")
        }
        val json = FileIOUtils.readFile2String(jsonFile)
        val pluginInfo = BeanUtils.toBean(json, PluginInfo::class.java)
        WinkLog.d("插件包信息：$pluginInfo; JSON = $json")

        // 宿主版本校验，下载提示交给业务去做。
        if (pluginInfo.hostVersionCode != hostVersionCode) {
            throw PluginException("主程序版本较低(Ver$hostVersionCode, Cur${pluginInfo.hostVersionCode})，请更新后再使用。")
        }

        // 删除当前插件目录
        FileUtils.delete(pluginDir)
        FileUtils.delete(file)

        // 移动到插件目录
        FileUtils.moveDir(unzipDir, pluginDir, null)

        // 生成版本号锁
        FileIOUtils.writeFileFromString(inf.lockFile(), pluginInfo.versionCode.toString())

        return pluginInfo.also { it.load(context, inf.pluginFile()!!) }
    }
}