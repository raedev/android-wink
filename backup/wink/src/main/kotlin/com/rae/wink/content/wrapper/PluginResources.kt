package com.rae.wink.content.wrapper

import android.content.res.*
import android.graphics.Movie
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.RequiresApi
import com.rae.wink.utils.WinkLog
import java.io.InputStream

/**
 * 插件资源管理（代理）
 */
class PluginResources(
    assets: AssetManager,
    metrics: DisplayMetrics,
    config: Configuration,
    /* 宿主的Resources */
    private val hostResources: Resources,
    /* 插件包名 */
    private val pluginPackageName: String
) : Resources(assets, metrics, config) {

    @Throws(NotFoundException::class)
    override fun getText(id: Int): CharSequence {
        return try {
            super.getText(id)
        } catch (e: NotFoundException) {
            hostResources.getText(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun getQuantityText(id: Int, quantity: Int): CharSequence {
        return try {
            super.getQuantityText(id, quantity)
        } catch (e: NotFoundException) {
            hostResources.getQuantityText(id, quantity)
        }
    }

    @Throws(NotFoundException::class)
    override fun getString(id: Int): String {
        return try {
            super.getString(id)
        } catch (e: NotFoundException) {
            hostResources.getString(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun getString(id: Int, vararg formatArgs: Any): String {
        return try {
            super.getString(id, *formatArgs)
        } catch (e: NotFoundException) {
            hostResources.getString(id, *formatArgs)
        }
    }

    @Throws(NotFoundException::class)
    override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any): String {
        return try {
            super.getQuantityString(id, quantity, *formatArgs)
        } catch (e: NotFoundException) {
            hostResources.getQuantityString(id, quantity, *formatArgs)
        }
    }

    @Throws(NotFoundException::class)
    override fun getQuantityString(id: Int, quantity: Int): String {
        return try {
            super.getQuantityString(id, quantity)
        } catch (e: NotFoundException) {
            hostResources.getQuantityString(id, quantity)
        }
    }

    override fun getText(id: Int, def: CharSequence): CharSequence {
        var ret: CharSequence? = null
        try {
            ret = super.getText(id)
        } catch (e: NotFoundException) {
            // ingore
        }
        if (ret == null) {
            ret = hostResources.getText(id, def)
        }
        return ret!!
    }

    @Throws(NotFoundException::class)
    override fun getTextArray(id: Int): Array<CharSequence> {
        return try {
            super.getTextArray(id)
        } catch (e: NotFoundException) {
            hostResources.getTextArray(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun getStringArray(id: Int): Array<String> {
        return try {
            super.getStringArray(id)
        } catch (e: NotFoundException) {
            hostResources.getStringArray(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun getIntArray(id: Int): IntArray {
        return try {
            super.getIntArray(id)
        } catch (e: NotFoundException) {
            hostResources.getIntArray(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun obtainTypedArray(id: Int): TypedArray {
        return try {
            super.obtainTypedArray(id)
        } catch (e: NotFoundException) {
            hostResources.obtainTypedArray(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun getDimension(id: Int): Float {
        return try {
            super.getDimension(id)
        } catch (e: NotFoundException) {
            hostResources.getDimension(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun getDimensionPixelOffset(id: Int): Int {
        return try {
            super.getDimensionPixelOffset(id)
        } catch (e: NotFoundException) {
            hostResources.getDimensionPixelOffset(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun getDimensionPixelSize(id: Int): Int {
        return try {
            super.getDimensionPixelSize(id)
        } catch (e: NotFoundException) {
            hostResources.getDimensionPixelSize(id)
        }
    }

    override fun getFraction(id: Int, base: Int, pbase: Int): Float {
        return try {
            super.getFraction(id, base, pbase)
        } catch (e: NotFoundException) {
            hostResources.getFraction(id, base, pbase)
        }
    }

    @Throws(NotFoundException::class)
    override fun getDrawable(id: Int): Drawable {
        return try {
            WinkLog.d("getDrawable:$id")
            super.getDrawable(id)
        } catch (e: NotFoundException) {
            WinkLog.w("getDrawable:$id")
            hostResources.getDrawable(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun getDrawable(id: Int, theme: Theme?): Drawable {
        return try {
            WinkLog.d("getDrawable2:$id")
            super.getDrawable(id, theme)
        } catch (e: NotFoundException) {
            WinkLog.w("getDrawable2:$id")
            hostResources.getDrawable(id, theme)
        }
    }

    @Throws(NotFoundException::class)
    override fun getDrawableForDensity(id: Int, density: Int): Drawable? {
        return try {
            WinkLog.d("getDrawable3:$id")
            super.getDrawableForDensity(id, density)
        } catch (e: NotFoundException) {
            WinkLog.w("getDrawable3:$id")
            hostResources.getDrawableForDensity(id, density)
        }
    }

    @Throws(NotFoundException::class)
    override fun getDrawableForDensity(id: Int, density: Int, theme: Theme?): Drawable? {
        return try {
            WinkLog.d("getDrawable4:$id")
            super.getDrawableForDensity(id, density, theme)
        } catch (e: NotFoundException) {
            WinkLog.w("getDrawable4:$id")
            hostResources.getDrawableForDensity(id, density, theme)
        }
    }

    @Throws(NotFoundException::class)
    override fun getMovie(id: Int): Movie {
        return try {
            super.getMovie(id)
        } catch (e: NotFoundException) {
            hostResources.getMovie(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun getColor(id: Int): Int {
        return try {
            super.getColor(id)
        } catch (e: NotFoundException) {
            hostResources.getColor(id)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Throws(NotFoundException::class)
    override fun getColor(id: Int, theme: Theme?): Int {
        return try {
            super.getColor(id, theme)
        } catch (e: NotFoundException) {
            hostResources.getColor(id, theme)
        }
    }

    @Throws(NotFoundException::class)
    override fun getColorStateList(id: Int): ColorStateList {
        return try {
            super.getColorStateList(id)
        } catch (e: NotFoundException) {
            hostResources.getColorStateList(id)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Throws(NotFoundException::class)
    override fun getColorStateList(id: Int, theme: Theme?): ColorStateList {
        return try {
            super.getColorStateList(id, theme)
        } catch (e: NotFoundException) {
            hostResources.getColorStateList(id, theme)
        }
    }

    @Throws(NotFoundException::class)
    override fun getBoolean(id: Int): Boolean {
        return try {
            super.getBoolean(id)
        } catch (e: NotFoundException) {
            hostResources.getBoolean(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun getInteger(id: Int): Int {
        return try {
            super.getInteger(id)
        } catch (e: NotFoundException) {
            hostResources.getInteger(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun getLayout(id: Int): XmlResourceParser {
        return try {
            WinkLog.d("getLayout:$id")
            super.getLayout(id)
        } catch (e: NotFoundException) {
            hostResources.getLayout(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun getAnimation(id: Int): XmlResourceParser {
        return try {
            super.getAnimation(id)
        } catch (e: NotFoundException) {
            hostResources.getAnimation(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun getXml(id: Int): XmlResourceParser {
        return try {
            WinkLog.d("getXml:$id")
            super.getXml(id)
        } catch (e: NotFoundException) {
            hostResources.getXml(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun openRawResource(id: Int): InputStream {
        return try {
            super.openRawResource(id)
        } catch (e: NotFoundException) {
            hostResources.openRawResource(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun openRawResource(id: Int, value: TypedValue): InputStream {
        return try {
            super.openRawResource(id, value)
        } catch (e: NotFoundException) {
            hostResources.openRawResource(id, value)
        }
    }

    @Throws(NotFoundException::class)
    override fun openRawResourceFd(id: Int): AssetFileDescriptor {
        return try {
            super.openRawResourceFd(id)
        } catch (e: NotFoundException) {
            hostResources.openRawResourceFd(id)
        }
    }

    @Throws(NotFoundException::class)
    override fun getValue(id: Int, outValue: TypedValue, resolveRefs: Boolean) {
        try {
            WinkLog.d("getValue1:$id, $outValue")
            super.getValue(id, outValue, resolveRefs)
        } catch (e: NotFoundException) {
            WinkLog.e("getValue1:$id, $outValue.")
            hostResources.getValue(id, outValue, resolveRefs)
        }
    }

    @Throws(NotFoundException::class)
    override fun getValueForDensity(
        id: Int,
        density: Int,
        outValue: TypedValue,
        resolveRefs: Boolean
    ) {
        try {
            WinkLog.d("getValue2:$id")
            super.getValueForDensity(id, density, outValue, resolveRefs)
        } catch (e: NotFoundException) {
            hostResources.getValueForDensity(id, density, outValue, resolveRefs)
        }
    }

    @Throws(NotFoundException::class)
    override fun getValue(name: String, outValue: TypedValue, resolveRefs: Boolean) {
        try {
            WinkLog.d("getValue3:$name")
            super.getValue(name, outValue, resolveRefs)
        } catch (e: NotFoundException) {
            hostResources.getValue(name, outValue, resolveRefs)
        }
    }

    override fun obtainAttributes(set: AttributeSet, attrs: IntArray): TypedArray {
        return try {
            WinkLog.d("obtainAttributes:$set")
            super.obtainAttributes(set, attrs)
        } catch (e: NotFoundException) {
            hostResources.obtainAttributes(set, attrs)
        }
    }

    @Throws(NotFoundException::class)
    override fun getResourceName(resid: Int): String {
        return try {
            WinkLog.d("getResourceName:$resid")
            super.getResourceName(resid)
        } catch (e: NotFoundException) {
            hostResources.getResourceName(resid)
        }
    }

    @Throws(NotFoundException::class)
    override fun getResourcePackageName(resid: Int): String {
        return try {
            WinkLog.d("getResourcePackageName:$resid")
            super.getResourcePackageName(resid)
        } catch (e: NotFoundException) {
            hostResources.getResourcePackageName(resid)
        }
    }

    @Throws(NotFoundException::class)
    override fun getResourceTypeName(resid: Int): String {
        return try {
            WinkLog.d("getResourceTypeName:$resid")
            super.getResourceTypeName(resid)
        } catch (e: NotFoundException) {
            hostResources.getResourceTypeName(resid)
        }
    }

    @Throws(NotFoundException::class)
    override fun getResourceEntryName(resid: Int): String {
        return try {
            WinkLog.d("getResourceEntryName:$resid")
            super.getResourceEntryName(resid)
        } catch (e: NotFoundException) {
            hostResources.getResourceEntryName(resid)
        }
    }

    override fun getIdentifier(name: String, defType: String, defPackage: String): Int {
        var result = 0
        WinkLog.d("getIdentifier: $name,$defType, $defPackage")
        if (!TextUtils.isEmpty(pluginPackageName)) {
            result = super.getIdentifier(name, defType, pluginPackageName)
        }
        if (result == 0) {
            result = super.getIdentifier(name, defType, defPackage)
            if (result == 0) {
                result = hostResources.getIdentifier(name, defType, defPackage)
            }
        }
        WinkLog.d("getIdentifier: $name,$defType, $defPackage")
        return result
    }


}