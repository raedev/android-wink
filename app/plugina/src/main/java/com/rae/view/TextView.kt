package com.rae.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 *
 * @author RAE
 * @date 2022/08/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class TextView : AppCompatTextView {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }


    private fun initView() {
        setBackgroundColor(Color.RED)
        setPadding(20, 20, 20, 20)
    }


}