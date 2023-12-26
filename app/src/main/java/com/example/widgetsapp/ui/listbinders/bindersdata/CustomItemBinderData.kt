package com.example.widgetsapp.ui.listbinders.bindersdata

import androidx.fragment.app.Fragment

data class CustomItemBinderData (
    val fragment: Fragment,
    val recyclerViewPosition: Int? = null
)

data class StackViewBinderData (
    val fragment: Fragment,
    val recyclerViewPosition: Int? = null
)