package com.wink.plugin.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.rae.wink.content.component.PluginFragment
import com.rae.wink.utils.WinkLog

/**
 *
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class DemoPluginFragment : PluginFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fm_demo, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WinkLog.d("当前插件的类加载器为：${this.javaClass.classLoader}")
        WinkLog.d("当前插件的Context加载器为：${requireContext().classLoader}")
        WinkLog.d("当前插件的Resource为：${resources}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btn_test).setOnClickListener {
            Toast.makeText(requireContext(), "点击了", Toast.LENGTH_SHORT).show()
        }
    }
}