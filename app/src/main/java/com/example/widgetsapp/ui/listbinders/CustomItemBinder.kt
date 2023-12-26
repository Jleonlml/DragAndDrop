package com.example.widgetsapp.ui.listbinders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.widgetsapp.R
import com.example.widgetsapp.databinding.CustomItemBinding
import com.example.widgetsapp.ui.adapter.GenericRecyclerViewAdapter
import com.example.widgetsapp.ui.adapter.ListItem
import com.example.widgetsapp.ui.listbinders.bindersdata.StackViewBinderData

class CustomItemBinder(private val view: View, private val binderData: StackViewBinderData): GenericRecyclerViewAdapter.Binder<ListItem.StackViewItem>(view) {

    private var mBinding: CustomItemBinding? = null

    override fun bind(item: ListItem.StackViewItem, position: Int) {
        mBinding = DataBindingUtil.bind(view) ?: throw IllegalArgumentException("Invalid View")
    }

    companion object {
        fun getCustomItemBinder(
            item: ListItem.StackViewItem,
            container: ViewGroup,
            binderData: StackViewBinderData
        ): CustomItemBinder {
            val layoutInflater = LayoutInflater.from(container.context)
            return DataBindingUtil.inflate<CustomItemBinding>(
                layoutInflater,
                R.layout.custom_item,
                container,
                false
            ).let { binding ->
                val itemView = binding.root
                val itemBinder = CustomItemBinder(itemView, binderData)
                itemView.post {
                    itemBinder.bind(
                        item,
                        binderData.recyclerViewPosition?: 0
                    )
                }
                itemBinder
            }
        }
    }
}