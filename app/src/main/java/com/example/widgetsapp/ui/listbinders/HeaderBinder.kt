package com.example.widgetsapp.ui.listbinders

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.widgetsapp.R
import com.example.widgetsapp.databinding.AccountBinding
import com.example.widgetsapp.databinding.CustomItemBinding
import com.example.widgetsapp.databinding.HeaderBinding
import com.example.widgetsapp.ui.adapter.GenericRecyclerViewAdapter
import com.example.widgetsapp.ui.adapter.ListItem
import com.example.widgetsapp.ui.listbinders.bindersdata.StackViewBinderData
import com.example.widgetsapp.viewmodel.AccountDataModel
import com.example.widgetsapp.viewmodel.GroupDataModel

class HeaderBinder(private val view: View, private val binderData: StackViewBinderData): GenericRecyclerViewAdapter.Binder<GroupDataModel>(view) {

    private var mBinding: HeaderBinding? = null

    override fun bind(item: GroupDataModel, position: Int) {
        mBinding = DataBindingUtil.bind(view) ?: throw IllegalArgumentException("Invalid View")
        with(mBinding) {
            this?.groupName?.text  = item.groupName
        }
    }

    companion object {
        fun getHeaderItemBinder(
            item: GroupDataModel,
            container: ViewGroup,
            binderData: StackViewBinderData
        ): HeaderBinder {
            val layoutInflater = LayoutInflater.from(container.context)
            return DataBindingUtil.inflate<HeaderBinding>(
                layoutInflater,
                R.layout.header,
                container,
                false
            ).let { binding ->
                val itemView = binding.root
                val itemBinder = HeaderBinder(itemView, binderData)
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