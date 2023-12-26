package com.example.widgetsapp.ui.customsview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.example.widgetsapp.R

class CustomView: FrameLayout {

    private lateinit var  rootLayout: FrameLayout
    private lateinit var tvText: TextView
    private var text = ""

    constructor(context: Context): super(context) {
        initCustomView(context)
    }

    constructor(context: Context, attr: AttributeSet?): super(context, attr) {
        getStuffFromXml(attr, context)
        initCustomView(context)
    }

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int): super(context, attr, defStyleAttr)
    {
        getStuffFromXml(attr, context)
        initCustomView(context)
    }

    fun setCustomText(text: String) {
        tvText.text = text
    }

    private fun getStuffFromXml(attrs: AttributeSet?, context: Context) {
        val data = context.obtainStyledAttributes(attrs, R.styleable.CustomView)
        text = data.getString(R.styleable.CustomView_customText).toString()
        data?.recycle()
    }

    private fun initCustomView(context: Context) {
        layoutParams = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        LayoutInflater.from(context).inflate(R.layout.custom_view, this, true)
        rootLayout = findViewById(R.id.rootLayout)
        tvText = findViewById(R.id.tv_text)
        if(text.isNotEmpty())
        {
            tvText.text = text
        }
        refreshDrawableState()
    }
}