package com.example.widgetsapp.ui.listbinders

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.widgetsapp.R
import com.example.widgetsapp.databinding.AccountBinding
import com.example.widgetsapp.databinding.CustomItemBinding
import com.example.widgetsapp.ui.adapter.GenericRecyclerViewAdapter
import com.example.widgetsapp.ui.adapter.ListItem
import com.example.widgetsapp.ui.listbinders.bindersdata.StackViewBinderData
import com.example.widgetsapp.viewmodel.AccountDataModel
import com.example.widgetsapp.viewmodel.GroupDataModel

class AccountBinder(private val view: View, private val binderData: StackViewBinderData): GenericRecyclerViewAdapter.Binder<AccountDataModel>(view) {

    private var mBinding: AccountBinding? = null

    override fun bind(item: AccountDataModel, position: Int) {
        mBinding = DataBindingUtil.bind(view) ?: throw IllegalArgumentException("Invalid View")
        with(mBinding) {
            this?.accountName?.text  = item.accountName
            this?.accountCardView?.setOnLongClickListener { clickedView ->
                clickedView.startDragAndDrop(
                    ClipData(
                        "account_tile",
                        emptyArray(),
                        ClipData.Item("account_tile")
                    ),
                    View.DragShadowBuilder(clickedView),
                    clickedView,
                    View.DRAG_FLAG_OPAQUE
                )
            }
        }
    }

    companion object {
        fun getAccountItemBinder(
            item: AccountDataModel,
            container: ViewGroup,
            binderData: StackViewBinderData
        ): AccountBinder {
            val layoutInflater = LayoutInflater.from(container.context)
            return DataBindingUtil.inflate<AccountBinding>(
                layoutInflater,
                R.layout.account,
                container,
                false
            ).let { binding ->
                val itemView = binding.root
                val itemBinder = AccountBinder(itemView, binderData)
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