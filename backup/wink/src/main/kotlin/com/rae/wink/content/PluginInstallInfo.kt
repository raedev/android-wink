package com.rae.wink.content

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 插件安装信息，一般是远程服务器管理的插件包信息
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Parcelize
data class PluginInstallInfo(
    /** 插件名称 */
    var name: String,
    /** 插件标题 */
    var title: String,
    /** 依赖宿主的版本号 */
    var hostVersionCode: Int,
    /** 版本号 */
    var versionCode: Int,
    /** 插件包下载路径 */
    var downloadUrl: String,
    /** 插件包MD5 */
    var md5: String,
) : Parcelable