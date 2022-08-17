package com.rae.wink.content

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File


/**
 * 插件包信息，从插件包<info.json>文件中读取。
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Parcelize
data class PluginInfo(
    /** 插件名称 */
    var name: String,
    /** 插件标题 */
    var title: String,
    /** 依赖宿主的版本号 */
    var hostVersionCode: Int,
    /** 版本号 */
    var versionCode: Int,
    /** 显示的版本号 */
    var versionName: String,
    /** 排除的包列表 */
    var excludePackageList: List<String>? = null
) : Parcelable {

    lateinit var apkFile: File
    lateinit var packageName: String
    lateinit var packageInfo: PackageInfo

    internal fun load(context: Context, file: File) {
        this.apkFile = file
        this.packageInfo = context.packageManager.getPackageArchiveInfo(
            apkFile.path,
            PackageManager.GET_ACTIVITIES
                    or PackageManager.GET_PERMISSIONS
                    or PackageManager.GET_META_DATA
                    or PackageManager.GET_SERVICES
                    or PackageManager.GET_CONFIGURATIONS
                    or PackageManager.GET_RECEIVERS
                    or PackageManager.GET_PROVIDERS
        ) ?: throw NullPointerException("解析插件包信息错误")

        this.packageName = packageInfo.packageName
        packageInfo.applicationInfo.sourceDir = file.path
        packageInfo.applicationInfo.publicSourceDir = file.path
        packageInfo.applicationInfo.dataDir = File(file.parent, "data").path

    }
}