package com.rae.wink.install

import com.rae.wink.content.PluginInstallInfo
import java.io.File

/**
 * 插件包下载器
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface IPackageDownloader {

    /**
     * 下载插件包
     * @param info 插件安装信息
     * @param progressListener 插件下载回调
     */
    fun download(info: PluginInstallInfo, progressListener: (Int) -> Unit): File
}