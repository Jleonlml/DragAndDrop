package com.example.widgetsapp.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import com.example.widgetsapp.R
import com.example.widgetsapp.ui.listbinders.StackViewBinder
import com.example.widgetsapp.ui.listbinders.NormalItemBinder
import com.example.widgetsapp.ui.listbinders.bindersdata.NormalItemBinderData
import com.example.widgetsapp.ui.listbinders.bindersdata.StackViewBinderData
import com.example.widgetsapp.viewmodel.ItemDataModel
import com.example.widgetsapp.viewmodel.StackViewDataModel

class RecyclerViewAdapter(private val fragment: Fragment): GenericRecyclerViewAdapter<ListItem>(diffCallback =  DiffCallback()) {
    override fun getItemViewType(position: Int): Int =
        currentList().getOrNull(position).let { item->
            when(item) {
                is ListItem.NormalItem ->ItemType.Type1.ordinal
                is ListItem.StackViewItem ->ItemType.Type2.ordinal
                else -> { ItemType.Unknown.ordinal}
            }
        }

    override fun getBinder(parent: ViewGroup, viewType: Int, view: View): Binder<ListItem> =
        when (viewType) {
            ItemType.Type1.ordinal -> NormalItemBinder(view, getItem1BinderData()) as Binder<ListItem>
            else -> {
                StackViewBinder(view, getStackViewBinderData()) as Binder<ListItem>
            }
        }

    private fun getItem1BinderData() : NormalItemBinderData {
        return NormalItemBinderData(
            fragment = fragment
        )
    }

    private fun getStackViewBinderData() : StackViewBinderData {
        return StackViewBinderData(
            fragment = fragment
        )
    }

    override fun getLayoutId(viewType: Int): Int {
        return when(viewType) {
            ItemType.Type1.ordinal -> R.layout.recycler_view_item_1
            ItemType.Type2.ordinal -> R.layout.stack_view
            else -> { 0 }
        }
    }
}

enum class  ItemType {
    Type1, Type2, Unknown
}


sealed interface ListItem {
    data class NormalItem(val normalData: ItemDataModel) : ListItem
    data class StackViewItem(val stackViewData: StackViewDataModel) : ListItem
}

class DiffCallback: DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem == newItem


    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem == newItem
}