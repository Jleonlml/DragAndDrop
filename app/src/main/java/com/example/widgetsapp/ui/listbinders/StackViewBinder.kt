package com.example.widgetsapp.ui.listbinders

import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.widgetsapp.R
import com.example.widgetsapp.databinding.StackViewBinding
import com.example.widgetsapp.ui.adapter.GenericRecyclerViewAdapter
import com.example.widgetsapp.ui.adapter.ListItem
import com.example.widgetsapp.ui.listbinders.bindersdata.StackViewBinderData
import com.example.widgetsapp.ui.model.widgetsListModel

class StackViewBinder(private val view: View, private val binderData: StackViewBinderData): GenericRecyclerViewAdapter.Binder<ListItem.StackViewItem>(view) {

    private lateinit var mBinding: StackViewBinding

    override fun bind(item: ListItem.StackViewItem, position: Int) {
        mBinding = DataBindingUtil.bind(view) ?: throw IllegalArgumentException("Invalid View")
        with(mBinding) {
            item.stackViewData.groups?.map { group ->
                val binder = when(group.accounts.size) {
                    1 -> {
                        AccountBinder.getAccountItemBinder(item.stackViewData.groups[group.index].accounts[0], mBinding.stackView, binderData)
                    }
                    else -> {
                        GroupBinder.getGroupItemBinder(item.stackViewData.groups[group.index], mBinding.stackView, binderData)
                    }
                }
                binder.itemView.apply {
                    setTag(R.id.stackView, binder)
                    id = View.generateViewId()
                }
                val subList = mutableListOf<View>()
                if (binder is GroupBinder)
                {
                    val headerBinder = HeaderBinder.getHeaderItemBinder(item.stackViewData.groups[group.index], mBinding.stackView, binderData)
                    headerBinder.itemView.apply {
                        setTag(R.id.stackView, headerBinder)
                        id = View.generateViewId()
                    }
                    subList.add(headerBinder.itemView)
                    item.stackViewData.groups[group.index].accounts.forEach { account ->
                        val subBinder = AccountBinder.getAccountItemBinder(account, mBinding.stackView, binderData)
                        subList.add(subBinder.itemView.apply {
                            setTag(R.id.stackView, subBinder)
                            id = View.generateViewId()
                        })
                    }
                }
                widgetsListModel(binder.itemView, subList)
            }.let { list ->
                if (list != null) {
                    stackView.setWidgets(list)
                }
            }
        }
    }
}