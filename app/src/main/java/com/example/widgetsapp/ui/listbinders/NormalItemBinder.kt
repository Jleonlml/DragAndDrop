package com.example.widgetsapp.ui.listbinders

import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.widgetsapp.databinding.RecyclerViewItem1Binding
import com.example.widgetsapp.ui.adapter.GenericRecyclerViewAdapter
import com.example.widgetsapp.ui.adapter.ListItem
import com.example.widgetsapp.ui.listbinders.bindersdata.NormalItemBinderData

class NormalItemBinder(private val view: View, private val binderData: NormalItemBinderData): GenericRecyclerViewAdapter.Binder<ListItem.NormalItem>(view) {

    private var mBinding: RecyclerViewItem1Binding? = null

    override fun bind(item: ListItem.NormalItem, position: Int) {
        DataBindingUtil.bind<RecyclerViewItem1Binding>(view)?.let { binding ->
            mBinding = binding
            with(binding) {
                tvText.text = item.normalData.itemText
            }
        }
    }
}