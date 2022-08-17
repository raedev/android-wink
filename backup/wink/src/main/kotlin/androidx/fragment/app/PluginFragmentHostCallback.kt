package androidx.fragment.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import java.io.FileDescriptor
import java.io.PrintWriter


/**
 *  插件 Fragment 的 mHost 对象，委托了大部分操作给原来的 mHost
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PluginFragmentHostCallback(
    private val origin: FragmentHostCallback<FragmentActivity>
) :
    FragmentHostCallback<FragmentActivity>(origin.activity as FragmentActivity) {

    override fun onGetHost(): FragmentActivity? {
        return activity as FragmentActivity?
    }

    override fun getActivity(): Activity? {
        return origin.activity
    }

    override fun getContext(): Context {
        return origin.context
    }

    override fun onDump(
        prefix: String,
        fd: FileDescriptor?,
        writer: PrintWriter,
        args: Array<out String>?
    ) {
        origin.onDump(prefix, fd, writer, args)
    }

    override fun onShouldSaveFragmentState(fragment: Fragment): Boolean {
        return origin.onShouldSaveFragmentState(fragment)
    }

    override fun onSupportInvalidateOptionsMenu() {
        origin.onSupportInvalidateOptionsMenu()
    }

    override fun onHasWindowAnimations(): Boolean {
        return origin.onHasWindowAnimations()
    }

    override fun onGetWindowAnimations(): Int {
        return origin.onGetWindowAnimations()
    }

    override fun onFindViewById(id: Int): View? {
        return origin.onFindViewById(id)
    }

    override fun onHasView(): Boolean {
        return origin.onHasView()
    }

    override fun getHandler(): Handler {
        return origin.handler
    }

    @Deprecated("Deprecated in Java")
    override fun instantiate(context: Context, className: String, arguments: Bundle?): Fragment {
        return origin.instantiate(context, className, arguments)
    }

    override fun onGetLayoutInflater(): LayoutInflater {
        return origin.onGetLayoutInflater()
    }

    override fun onStartActivityFromFragment(
        fragment: Fragment,
        intent: Intent?,
        requestCode: Int
    ) {
        origin.onStartActivityFromFragment(fragment, intent, requestCode)
    }

    override fun onStartActivityFromFragment(
        fragment: Fragment,
        intent: Intent?,
        requestCode: Int,
        options: Bundle?
    ) {
        origin.onStartActivityFromFragment(fragment, intent, requestCode, options)
    }

    @Deprecated("Deprecated in Java")
    override fun onStartIntentSenderFromFragment(
        fragment: Fragment,
        intent: IntentSender?,
        requestCode: Int,
        fillInIntent: Intent?,
        flagsMask: Int,
        flagsValues: Int,
        extraFlags: Int,
        options: Bundle?
    ) {
        origin.onStartIntentSenderFromFragment(
            fragment,
            intent,
            requestCode,
            fillInIntent,
            flagsMask,
            flagsValues,
            extraFlags,
            options
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsFromFragment(
        fragment: Fragment,
        permissions: Array<out String>,
        requestCode: Int
    ) {
        origin.onRequestPermissionsFromFragment(fragment, permissions, requestCode)
    }

    override fun onShouldShowRequestPermissionRationale(permission: String): Boolean {
        return origin.onShouldShowRequestPermissionRationale(permission)
    }

}