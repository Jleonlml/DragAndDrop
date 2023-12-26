package com.example.widgetsapp.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.example.widgetsapp.R
import com.example.widgetsapp.databinding.RecyclerViewItem1Binding
import com.example.widgetsapp.viewmodel.ItemDataModel

class SimpleAdapter: GenericRecyclerViewAdapter<ItemDataModel>() {
    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getBinder(parent: ViewGroup, viewType: Int, view: View): Binder<ItemDataModel> {
        return getBinder(view, parent.context)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.recycler_view_item_1
    }

    private fun getBinder(view: View, context: Context): Binder<ItemDataModel> {
        return object : Binder<ItemDataModel>(view) {
            private val mBinding = RecyclerViewItem1Binding.bind(view)
            override fun bind(item: ItemDataModel, position: Int) {
                mBinding.tvText.text = item.itemText
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_1 = 1
        private const val VIEW_TYPE_2 = 2
    }
}
