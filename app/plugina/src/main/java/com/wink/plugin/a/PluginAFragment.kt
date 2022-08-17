package com.wink.plugin.a

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.raedev.wink.content.component.PluginFragment
import com.wink.plugin.common.CommonTest

/**
 * 插件A
 * @author RAE
 * @date 2022/08/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PluginAFragment : PluginFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return createLayout(R.layout.fm_plugin_a, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewGroup = view as ViewGroup
        for (i in 0 until viewGroup.childCount) {
            viewGroup.getChildAt(i).setOnClickListener {
                when (it.id) {
                    R.id.btn_demo -> CommonTest.say(
                        requireContext(),
                        "这里是插件调用公共类：${CommonTest.name}"
                    )
                    R.id.btn_dialog -> onDialogClick()
                    else -> Toast.makeText(requireContext(), "点击了：${it}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onDialogClick() {
        PluginADialogFragment().show(childFragmentManager, "dialog")
    }
}


class PluginADialogFragment : DialogFragment() {

    override fun getTheme(): Int {
        return androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fm_plugin_a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btn_demo).also {
            it.text = "这里是对话框"
        }
        view.findViewById<Button>(R.id.btn_dialog).also {
            it.text = "关闭"
            it.setOnClickListener {
                dismiss()
            }
        }
    }

}