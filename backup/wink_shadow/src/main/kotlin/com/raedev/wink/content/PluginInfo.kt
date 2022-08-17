package com.raedev.wink.content

import android.os.Parcelable
import com.tencent.shadow.core.manager.installplugin.InstalledPlugin
import kotlinx.parcelize.Parcelize

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
) : Parcelable {


    constructor(p: InstalledPlugin.PluginPart) : this(
        p.businessName,
        p.businessName,
        0, 0, ""
    )


}